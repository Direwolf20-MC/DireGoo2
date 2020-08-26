package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.items.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.stream.Collectors;

public class GoonadeFreezeEntity extends ProjectileBase {
    @ObjectHolder(DireGoo.MOD_ID + ":goonadefreezeentity")

    public static EntityType<GoonadeFreezeEntity> TYPE;

    public GoonadeFreezeEntity(EntityType<GoonadeFreezeEntity> type, World world) {
        super(type, world);
    }

    public GoonadeFreezeEntity(World world, LivingEntity thrower) {
        super(TYPE, world, thrower);
    }

    public GoonadeFreezeEntity(World world, double x, double y, double z) {
        super(TYPE, world, x, y, z);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!world.isRemote) {
            BlockPos hitPos = new BlockPos(result.getHitVec().getX(), result.getHitVec().getY(), result.getHitVec().getZ());
            List<BlockPos> area = BlockPos.getAllInBox(hitPos.add(-2, -2, -2), hitPos.add(2, 2, 2))
                    .filter(blockPos -> world.getBlockState(blockPos).getBlock() instanceof GooBase)
                    .map(BlockPos::toImmutable)
                    .collect(Collectors.toList());
            for (BlockPos pos : area)
                GooBase.freezeGoo((ServerWorld) world, pos);
            remove();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GOONADE_FREEZE.get();
    }

    @Override
    public boolean hasNoGravity() {
        return false;
    }

}
