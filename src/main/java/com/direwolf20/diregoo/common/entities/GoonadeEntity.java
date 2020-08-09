package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.items.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;

public class GoonadeEntity extends ProjectileBase {
    @ObjectHolder(DireGoo.MOD_ID + ":goonadeentity")

    public static EntityType<GoonadeEntity> TYPE;

    public GoonadeEntity(EntityType<GoonadeEntity> type, World world) {
        super(type, world);
    }

    public GoonadeEntity(World world, LivingEntity thrower) {
        super(TYPE, world, thrower);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!world.isRemote) {
            BlockPos pos = new BlockPos(result.getHitVec().getX(), result.getHitVec().getY(), result.getHitVec().getZ());
            BlockState inBlockState = world.getBlockState(pos);
            if (inBlockState.getBlock() instanceof GooBase)
                GooBase.resetBlock((ServerWorld) world, pos);
            remove();

        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GOONADE.get();
    }

    @Override
    public boolean hasNoGravity() {
        return false;
    }

}
