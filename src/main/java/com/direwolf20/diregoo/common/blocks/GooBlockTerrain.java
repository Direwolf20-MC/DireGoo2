package com.direwolf20.diregoo.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GooBlockTerrain extends GooBase {

    @Override
    public boolean canSpreadHere(BlockPos pos, BlockState oldState, World world) {
        if (oldState.getBlock().isAir(oldState, world, pos)) return false;
        return super.canSpreadHere(pos, oldState, world);
    }
}
