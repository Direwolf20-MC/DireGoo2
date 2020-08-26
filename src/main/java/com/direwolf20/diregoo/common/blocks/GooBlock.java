package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBlock extends GooBase {

    @Override
    public boolean customPreChecks(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(100) > Config.SPREADCHANCEGOO.get())
            return false;
        if (!Config.CAN_SPREAD_GOO.get())
            return false;
        return true;
    }

}
