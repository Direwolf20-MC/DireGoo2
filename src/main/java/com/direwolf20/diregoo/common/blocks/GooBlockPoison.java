package com.direwolf20.diregoo.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBlockPoison extends GooBase {


    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 3))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        if (rand.nextDouble() < 0.5) //The percent chance it decays rather than spreads. Lower == more spread.
            this.resetBlock(worldIn, pos, false, 80);
        else {
            BlockPos gooPos = spreadGoo(state, worldIn, pos, rand);
            worldIn.getPendingBlockTicks().scheduleTick(gooPos, this, 5);
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, 5);
        }
    }

    @Override
    public BlockPos spreadGoo(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        int x = rand.nextInt(Direction.values().length);
        Direction direction = Direction.values()[x];

        BlockPos checkPos = pos.offset(direction);
        BlockState oldState = worldIn.getBlockState(checkPos);

        if (oldState.getBlock() instanceof GooBlock && !(oldState.getBlock() instanceof GooBlockPoison)) {
            worldIn.setBlockState(checkPos, ModBlocks.GOO_BLOCK_POISON.get().getDefaultState());
            return checkPos;
        }
        return BlockPos.ZERO;
    }
}
