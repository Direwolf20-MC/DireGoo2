package com.direwolf20.diregoo.common.container;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.tiles.AntiGooFieldGenTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class AntiGooFieldGenContainer extends FEContainerBase {
    //private static final int SLOTS = 0;

    public AntiGooFieldGenContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((AntiGooFieldGenTileEntity) playerInventory.player.world.getTileEntity(extraData.readBlockPos()), new IntArray(3), windowId, playerInventory);
    }

    public AntiGooFieldGenContainer(@Nullable AntiGooFieldGenTileEntity tile, IIntArray antiGooFieldGenData, int windowId, PlayerInventory playerInventory) {
        super(ModBlocks.ANTI_GOO_FIELD_GEN_CONTAINER.get(), tile, antiGooFieldGenData, windowId, playerInventory);
        this.tile = tile;
        this.data = antiGooFieldGenData;
        this.setup(playerInventory);
        trackIntArray(antiGooFieldGenData);
    }

    @Override
    public void setup(PlayerInventory inventory) {
        //addSlot(new RestrictedSlot(handler, 0, 65, 43));
        //addSlot(new RestrictedSlot(handler, 1, 119, 43));
        super.setup(inventory);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        BlockPos pos = this.tile.getPos();
        return this.tile != null && !this.tile.isRemoved() && playerIn.getDistanceSq(new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    public int getRFPerTick() {
        return this.data.get(2);
    }

    public int getMaxPower() {
        return this.data.get(1) * 32;
    }

    public int getEnergy() {
        return this.data.get(0) * 32;
    }

}
