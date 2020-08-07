package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;

public class GoonadeEntity extends ProjectileBase {
    @ObjectHolder(DireGoo.MOD_ID + ":goonadeentity")

    public static EntityType<GoonadeEntity> TYPE;

    public GoonadeEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public GoonadeEntity(World world, double spawnX, double spawnY, double spawnZ, double tx, double ty, double tz) {
        this(TYPE, world);

        targetX = tx;
        targetY = ty;
        targetZ = tz;
        setVelocity(tx, ty, tz);
        setPosition(spawnX, spawnY, spawnZ);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        BlockPos pos = new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ());
        BlockState inBlockState = world.getBlockState(pos);
        if (!inBlockState.isAir()) {
            if (inBlockState.getBlock() instanceof GooBase && !world.isRemote)
                GooBase.resetBlock((ServerWorld) world, pos);
            remove();
        }
    }

    @Override
    public boolean hasNoGravity() {
        return false;
    }

    @Override
    public float getSpeedMultiplier() {
        return 1.25f;
    }
}
