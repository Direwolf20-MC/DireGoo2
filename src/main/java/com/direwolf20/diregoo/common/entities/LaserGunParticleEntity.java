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

public class LaserGunParticleEntity extends ProjectileBase {
    @ObjectHolder(DireGoo.MOD_ID + ":lasergunparticleentity")

    public static EntityType<LaserGunParticleEntity> TYPE;

    public LaserGunParticleEntity(EntityType<LaserGunParticleEntity> type, World world) {
        super(type, world);
    }

    public LaserGunParticleEntity(World world, LivingEntity thrower, double spawnX, double spawnY, double spawnZ) {
        super(TYPE, world, thrower);
        setPosition(spawnX, spawnY, spawnZ);
    }

    @Override
    public void tick() {
        super.tick();
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
        return ModItems.GOO_REMOVER.get();
    }
}
