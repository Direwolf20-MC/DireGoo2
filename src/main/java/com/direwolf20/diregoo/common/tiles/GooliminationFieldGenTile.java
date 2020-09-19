package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.container.GooliminationFieldGenContainer;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GooliminationFieldGenTile extends FETileBase implements ITickableTileEntity, INamedContainerProvider {

    private boolean isActive = false;

    public GooliminationFieldGenTile() {
        super(ModBlocks.GOOLIMINATION_TILE.get(), 1000000000);
    }

    public boolean isActive(World world) {
        if (!world.isRemote()) {
            BlockSave blockSave = BlockSave.get(world);
            if (isActive != blockSave.getGooDeathEvent()) {
                isActive = blockSave.getGooDeathEvent();
                markDirtyClient();
            }
        }
        return isActive;
    }

    public void activate(ServerWorld world) {
        BlockSave blockSave = BlockSave.get(world);
        blockSave.setGooDeathEvent(true);
        this.isActive = true;
        markDirtyClient();
    }

    public void deactivate(ServerWorld world) {
        BlockSave blockSave = BlockSave.get(world);
        blockSave.setGooDeathEvent(false);
        this.isActive = false;
        markDirtyClient();
    }

    @Override
    public void tick() {
        //Client only
        if (world.isRemote) {
            //System.out.println("I'm here!");
        }

        //Server Only
        if (!world.isRemote) {
            energyStorage.receiveEnergy(100000, false); //Testing
            if (isActive(world)) {
                int rfCost = 1000000;
                if (energyStorage.getEnergyStored() >= rfCost)
                    energyStorage.consumeEnergy(rfCost, false);
                else {
                    deactivate((ServerWorld) world);
                }
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        isActive = tag.getBoolean("active");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putBoolean("active", isActive);
        return super.write(tag);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.up(10).north(10).east(10), pos.down(10).south(10).west(10));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Goolimination Field Generator");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert world != null;
        return new GooliminationFieldGenContainer(this, this.FETileData, i, playerInventory);
    }
}
