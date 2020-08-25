package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBlockBurst extends GooBase {
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!shouldGooSpread(state, worldIn, pos, rand))
            return;
        if (!worldIn.isAreaLoaded(pos, 10))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        if (handleFrozen(pos, state, worldIn)) return;
        BlockPos gooPos = pos;
        int burstAmt = rand.nextInt(Config.MAXBURST.get() - Config.MINBURST.get()) + Config.MINBURST.get();
        for (int i = 0; i < burstAmt; i++) {
            BlockPos newGooPos = spreadGoo(state, worldIn, gooPos, rand);
            if (!newGooPos.equals(BlockPos.ZERO))
                gooPos = newGooPos;
        }
    }
}
