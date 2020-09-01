package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBlockBurst extends GooBase {

    @Override
    public boolean customPreChecks(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(100) > Config.SPREADCHANCEBURST.get())
            return false;
        if (!Config.CAN_SPREAD_BURST.get())
            return false;
        return true;
    }

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
            boolean animate = false;
            if (Config.ANIMATE_SPREAD.get())
                animate = worldIn.isPlayerWithin(pos.getX(), pos.getY(), pos.getZ(), 20);
            BlockPos newGooPos = spreadGoo(state, worldIn, gooPos, rand, animate);
            if (!newGooPos.equals(BlockPos.ZERO))
                gooPos = newGooPos;
        }
    }
}
