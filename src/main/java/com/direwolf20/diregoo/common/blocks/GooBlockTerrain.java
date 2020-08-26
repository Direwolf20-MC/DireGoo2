package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBlockTerrain extends GooBase {


    @Override
    public boolean canSpreadHere(BlockPos pos, BlockState oldState, World world) {
        if (oldState.getBlock().isAir(oldState, world, pos)) return false;
        return super.canSpreadHere(pos, oldState, world);
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
