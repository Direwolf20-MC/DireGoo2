package com.direwolf20.diregoo.common.container;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.tiles.ZapperTurretTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class ZapperTurretContainer extends FEContainerBase {
    //private static final int SLOTS = 0;

    public ZapperTurretContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((ZapperTurretTileEntity) playerInventory.player.world.getTileEntity(extraData.readBlockPos()), new IntArray(4), windowId, playerInventory);
    }

    public ZapperTurretContainer(@Nullable ZapperTurretTileEntity tile, IIntArray turretData, int windowId, PlayerInventory playerInventory) {
        super(ModBlocks.ZAPPER_TURRET_CONTAINER.get(), tile, turretData, windowId, playerInventory);
        this.tile = tile;
        this.data = turretData;
        this.setup(playerInventory);
        trackIntArray(turretData);
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

}
