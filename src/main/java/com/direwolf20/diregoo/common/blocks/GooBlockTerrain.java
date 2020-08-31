package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
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

    @Override
    public boolean isSurrounded(ServerWorld worldIn, BlockPos pos, BlockState state) {
        for (Direction direction : Direction.values()) {
            BlockPos tempPos = pos.offset(direction);
            BlockState tempState = worldIn.getBlockState(tempPos);
            if (!(tempState.getBlock() instanceof GooBase) && !(tempState.getBlock().isAir(tempState, worldIn, tempPos))) {
                if (!state.get(ACTIVE))
                    worldIn.setBlockState(pos, state.with(ACTIVE, true));
                return false; //If the adjacent block is anything other than goo its not surrounded
            }
        }
        if (state.get(ACTIVE))
            worldIn.setBlockState(pos, state.with(ACTIVE, false));
        return true;
    }

}
