package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.container.AntiGooFieldGenContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AntiGooFieldGenTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    /*public ChargerEnergyStorage energyStorage;
    private LazyOptional<ChargerEnergyStorage> energy;*/
    // Handles tracking changes, kinda messy but apparently this is how the cool kids do it these days
    public final IIntArray antiGooFieldGenData = new IIntArray() {
        @Override
        public int get(int index) {
            /*switch (index) {
                case 0:
                    return AntiGooFieldGenTileEntity.this.energyStorage.getEnergyStored() / 32;
                case 1:
                    return AntiGooFieldGenTileEntity.this.energyStorage.getMaxEnergyStored() / 32;
                case 2:
                    return AntiGooFieldGenTileEntity.this.counter;
                case 3:
                    return AntiGooFieldGenTileEntity.this.maxBurn;
                default:
                    throw new IllegalArgumentException("Invalid index: " + index);
            }*/
            return 0;
        }

        @Override
        public void set(int index, int value) {
            throw new IllegalStateException("Cannot set values through IIntArray");
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public AntiGooFieldGenTileEntity() {
        super(ModBlocks.ANTI_GOO_FIELD_GEN_TILE.get());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert world != null;
        return new AntiGooFieldGenContainer(this, this.antiGooFieldGenData, i, playerInventory);
    }

    @Override
    public void tick() {
        //Client only
        if (world.isRemote) {
            //System.out.println("I'm here!");
        }

        //Server Only
        if (!world.isRemote) {
            //System.out.println("I'm here!");
        }
    }


    //Misc Methods for TE's
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

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
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
