package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.container.AntiGooFieldGenContainer;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AntiGooFieldGenTileEntity extends FETileBase implements ITickableTileEntity, INamedContainerProvider {

    private boolean isActive = false;
    private boolean renderArea = false;
    private Set<BlockPos> protectedBlocksList = new HashSet<>();
    private int[] ranges = {0, 0, 0, 0, 0, 0}; //N,S,W,E,U,D

    public AntiGooFieldGenTileEntity() {
        super(ModBlocks.ANTI_GOO_FIELD_GEN_TILE.get());
    }

    public int[] getRanges() {
        return ranges;
    }

    public void setRanges(int[] ranges) {
        this.ranges = ranges;
    }

    public boolean isActive() {
        return isActive;
    }

    public Set<BlockPos> getProtectedBlocksList() {
        return protectedBlocksList;
    }

    public boolean isRenderArea() {
        return renderArea;
    }

    public void setRenderArea(boolean renderArea) {
        this.renderArea = renderArea;
        markDirtyClient();
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert world != null;
        return new AntiGooFieldGenContainer(this, this.FETileData, i, playerInventory);
    }

    public double getNumBlocks() {
        return (ranges[0] + ranges[1] + 1) * (ranges[2] + ranges[3] + 1) * (ranges[4] + ranges[5] + 1);
    }

    @Override
    public void tick() {
        //Client only
        if (world.isRemote) {
            //System.out.println("I'm here!");
        }

        //Server Only
        if (!world.isRemote) {
            energyStorage.receiveEnergy(1000, false); //Test
            if (isActive) {
                double rfCost = getNumBlocks() * Config.ANTIGOOFIELDGENRF.get();
                if (energyStorage.getEnergyStored() >= rfCost)
                    energyStorage.consumeEnergy((int) Math.floor(rfCost), false);
                else {
                    removeField();
                }
            }
        }
    }

    public void createBlockList() {
        protectedBlocksList = BlockPos.getAllInBox(this.pos.add(-ranges[2], -ranges[5], -ranges[0]), this.pos.add(ranges[3], ranges[4], ranges[1]))
                .map(BlockPos::toImmutable)
                .collect(Collectors.toSet());
    }

    public void addField() {
        double rfCost = getNumBlocks() * Config.ANTIGOOFIELDGENRF.get();
        if (energyStorage.getEnergyStored() < rfCost)
            return;
        createBlockList();
        BlockSave blockSave = BlockSave.get(world);
        blockSave.addAntiField(this.pos, protectedBlocksList, world);
        isActive = true;
        markDirtyClient();
    }

    public void removeField() {
        BlockSave blockSave = BlockSave.get(world);
        blockSave.removeAntiField(this.pos, protectedBlocksList, world);
        this.protectedBlocksList = new HashSet<>();
        isActive = false;
        markDirtyClient();
    }

    public void updateRanges(int[] newRanges) {
        if (isActive) {
            removeField();
            ranges = newRanges;
            addField();
        } else
            ranges = newRanges;
        markDirtyClient();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        isActive = tag.getBoolean("active");
        renderArea = tag.getBoolean("renderArea");
        ranges = tag.getIntArray("ranges");
        protectedBlocksList = new HashSet<>();
        ListNBT antigoo = tag.getList("protectedblockslist", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < antigoo.size(); i++) {
            BlockPos blockPos = NBTUtil.readBlockPos(antigoo.getCompound(i).getCompound("pos"));
            protectedBlocksList.add(blockPos);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putBoolean("active", isActive);
        tag.putBoolean("renderArea", renderArea);
        tag.putIntArray("ranges", ranges);
        ListNBT anti = new ListNBT();
        for (BlockPos blockPos : protectedBlocksList) {
            CompoundNBT comp = new CompoundNBT();
            comp.put("pos", NBTUtil.writeBlockPos(blockPos));
            anti.add(comp);
        }
        tag.put("protectedblockslist", anti);
        return super.write(tag);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.up(10).north(10).east(10), pos.down(10).south(10).west(10));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Anti Goo Field Gen Tile");
    }
}
