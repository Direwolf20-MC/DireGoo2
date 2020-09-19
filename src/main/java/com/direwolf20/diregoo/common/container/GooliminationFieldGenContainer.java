package com.direwolf20.diregoo.common.container;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.tiles.GooliminationFieldGenTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class GooliminationFieldGenContainer extends FEContainerBase {

    public GooliminationFieldGenContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((GooliminationFieldGenTile) playerInventory.player.world.getTileEntity(extraData.readBlockPos()), new IntArray(4), windowId, playerInventory);
    }

    public GooliminationFieldGenContainer(@Nullable GooliminationFieldGenTile tile, IIntArray gooliminationFieldGenData, int windowId, PlayerInventory playerInventory) {
        super(ModBlocks.GOOLIMINATION_CONTAINER.get(), tile, gooliminationFieldGenData, windowId, playerInventory);
        this.tile = tile;
        this.data = gooliminationFieldGenData;
        this.setup(playerInventory);
        trackIntArray(gooliminationFieldGenData);
    }

    @Override
    public void setup(PlayerInventory inventory) {
        super.setup(inventory);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        BlockPos pos = this.tile.getPos();
        return this.tile != null && !this.tile.isRemoved() && playerIn.getDistanceSq(new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

}
