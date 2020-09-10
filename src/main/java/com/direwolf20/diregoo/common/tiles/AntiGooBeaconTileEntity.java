package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.container.AntiGooBeaconContainer;
import com.direwolf20.diregoo.common.container.ZapperTurretContainer;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AntiGooBeaconTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private boolean isActive = false;
    private Set<BlockPos> protectedBlocksList = new HashSet<>();
    private ItemStackHandler inventoryStacks = new ItemStackHandler(ZapperTurretContainer.SLOTS);
    private LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(ZapperTurretContainer.SLOTS));

    public AntiGooBeaconTileEntity() {
        super(ModBlocks.ANTI_GOO_BEACON_TILE.get());
    }

    public boolean isActive() {
        return isActive;
    }

    public Set<BlockPos> getProtectedBlocksList() {
        return protectedBlocksList;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert world != null;
        return new AntiGooBeaconContainer(this, i, playerInventory, inventoryStacks);
    }

    @Override
    public void tick() {
        //Client only
        if (world.isRemote) {
            //System.out.println("I'm here!");
        }

        //Server Only
        if (!world.isRemote) {
            if (isActive) {

            }
        }
    }

    public void createBlockList() {
        int range = 3;
        protectedBlocksList = BlockPos.getAllInBox(this.pos.add(-range, -range, -range), this.pos.add(range, range, range))
                .map(BlockPos::toImmutable)
                .collect(Collectors.toSet());
    }

    public void addField() {
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

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        inventoryStacks.deserializeNBT(tag.getCompound("inv"));
        isActive = tag.getBoolean("active");
        protectedBlocksList = new HashSet<>();
        ListNBT antigoo = tag.getList("protectedblockslist", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < antigoo.size(); i++) {
            BlockPos blockPos = NBTUtil.readBlockPos(antigoo.getCompound(i).getCompound("pos"));
            protectedBlocksList.add(blockPos);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", inventoryStacks.serializeNBT());
        tag.putBoolean("active", isActive);
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, final @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Anti Goo Field Gen Tile");
    }

    //TE Data Stuff

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        // Vanilla uses the type parameter to indicate which type of tile entity (command block, skull, or beacon?) is receiving the packet, but it seems like Forge has overridden this behavior
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(this.getBlockState(), pkt.getNbtCompound());
    }

    public void markDirtyClient() {
        markDirty();
        if (getWorld() != null) {
            BlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        }
    }
}
