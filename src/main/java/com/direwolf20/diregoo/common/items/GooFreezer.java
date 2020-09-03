package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class GooFreezer extends FELaserBase {
    public GooFreezer() {
        super(new Properties().maxStackSize(1).group(DireGoo.itemGroup), Config.ITEM_FREEZER_RFMAX.get());
    }

    @Override
    public int getRange() {
        return Config.ITEM_FREEZER_RANGE.get();
    }

    @Override
    public int getRFCost() {
        return Config.ITEM_FREEZER_RFCOST.get();
    }

    @Override
    public void laserAction(ServerWorld world, BlockPos pos) {
        GooBase.freezeGoo(world, pos);
    }
}
