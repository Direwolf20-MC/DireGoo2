package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class GooZapper extends FELaserBase {
    public GooZapper() {
        super(new Properties().maxStackSize(1).group(DireGoo.itemGroup), Config.ITEM_ZAPPER_RFMAX.get());
        this.RFCost = Config.ITEM_ZAPPER_RFCOST.get();
        this.FireRange = Config.ITEM_ZAPPER_RANGE.get();
    }

    @Override
    public void laserAction(ServerWorld world, BlockPos pos) {
        GooBase.resetBlock((ServerWorld) world, pos, true, 20);
    }
}
