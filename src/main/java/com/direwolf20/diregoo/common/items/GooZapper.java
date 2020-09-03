package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class GooZapper extends FELaserBase {
    public GooZapper() {
        super(new Properties().maxStackSize(1).group(DireGoo.itemGroup), Config.ITEM_ZAPPER_RFMAX.get());
    }

    @Override
    public int getRange() {
        return Config.ITEM_ZAPPER_RANGE.get();
    }

    @Override
    public int getRFCost() {
        return Config.ITEM_ZAPPER_RFCOST.get();
    }

    @Override
    public void laserAction(ServerWorld world, BlockPos pos) {
        BlockSave blockSave = BlockSave.get(world);
        GooBase.resetBlock(world, pos, true, 20, true, blockSave);
    }
}
