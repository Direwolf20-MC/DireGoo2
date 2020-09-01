package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBlockTerrain extends GooBase {


    @Override
    public boolean isAdjacentValid(ServerWorld worldIn, BlockPos pos, BlockState state) {
        if (state.getBlock().isAir(state, worldIn, pos)) return false;
        return super.isAdjacentValid(worldIn, pos, state);
    }

    @Override
    public boolean customPreChecks(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(100) > Config.SPREADCHANCETERRAIN.get())
            return false;
        if (!Config.CAN_SPREAD_TERRAIN.get())
            return false;
        return true;
    }

}
