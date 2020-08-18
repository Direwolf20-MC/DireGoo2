package com.direwolf20.diregoo.common.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;

public abstract class FETileBase extends TileEntity implements ITickableTileEntity {

    // Handles tracking changes, kinda messy but apparently this is how the cool kids do it these days
    public final IIntArray FETileData = new IIntArray() {
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

    public FETileBase(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
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
}
