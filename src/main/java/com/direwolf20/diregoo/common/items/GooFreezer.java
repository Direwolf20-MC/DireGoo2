package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.util.FreezingCollect;
import com.direwolf20.diregoo.common.util.VectorHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

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
    public void laserAction(ServerWorld world, BlockPos pos, LivingEntity player) {
        BlockRayTraceResult lookingAt = VectorHelper.getLookingAt((PlayerEntity) player, RayTraceContext.FluidMode.NONE, getRange());
        List<BlockPos> coords = FreezingCollect.collect(player, lookingAt, world);
        for (BlockPos freezepos : coords) {
            GooBase.freezeGoo(world, freezepos);
        }
    }
}
