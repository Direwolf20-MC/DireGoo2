package com.direwolf20.diregoo.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class ProjectileBase extends Entity {
    protected int despawning = -1;
    protected double targetX;
    protected double targetY;
    protected double targetZ;

    public ProjectileBase(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (despawning == -1 && shouldSetDespawning()) {
            despawning = 0;
            onSetDespawning();
        } else if (despawning != -1 && ++despawning > 1)
            remove();

        Vector3d vector3d = this.getMotion();
        double speedMultiplier = getSpeedMultiplier();
        double d3 = vector3d.x * speedMultiplier;
        double d4 = vector3d.y * speedMultiplier;
        double d0 = vector3d.z * speedMultiplier;

        double d5 = this.getPosX() + d3;
        double d1 = this.getPosY() + d4;
        double d2 = this.getPosZ() + d0;

        if (!this.hasNoGravity()) {
            d1 = d1 - (double) this.getGravityVelocity();
        }

        this.setPosition(d5, d1, d2);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    protected boolean shouldSetDespawning() {
        return ticksExisted > getMaxLife();
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        despawning = compound.getInt("despawning");
        ticksExisted = compound.getInt("ticksExisted");
        targetX = compound.getDouble("targetX");
        targetY = compound.getDouble("targetY");
        targetZ = compound.getDouble("targetZ");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("despawning", despawning);
        compound.putInt("ticksExisted", ticksExisted);
        compound.putDouble("targetX", targetX);
        compound.putDouble("targetY", targetY);
        compound.putDouble("targetZ", targetZ);
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    protected int getMaxLife() {
        return 40;
    }

    @Override
    protected void registerData() {
    }

    protected void onSetDespawning() {
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected float getGravityVelocity() {
        float vel = (float) MathHelper.lerp((float) this.ticksExisted / this.getMaxLife(), 0.005, 0.8);
        return vel;
    }

    protected float getSpeedMultiplier() {
        return 1;
    }
}
