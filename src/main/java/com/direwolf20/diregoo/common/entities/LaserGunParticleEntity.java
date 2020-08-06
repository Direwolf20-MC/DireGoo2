package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

public class LaserGunParticleEntity extends Entity {
    @ObjectHolder(DireGoo.MOD_ID + ":lasergunparticleentity")

    public static EntityType<LaserGunParticleEntity> TYPE;

    private int despawning = -1;
    private double targetX;
    private double targetY;
    private double targetZ;

    public LaserGunParticleEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public LaserGunParticleEntity(World world, double spawnX, double spawnY, double spawnZ, double tx, double ty, double tz) {
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
        if (despawning == -1 && shouldSetDespawning()) {
            despawning = 0;
            onSetDespawning();
        } else if (despawning != -1 && ++despawning > 1)
            remove();

        Vector3d vector3d = this.getMotion();
        double d3 = vector3d.x;
        double d4 = vector3d.y;
        double d0 = vector3d.z;

        double d5 = this.getPosX() + d3;
        double d1 = this.getPosY() + d4;
        double d2 = this.getPosZ() + d0;

        this.setPosition(d5, d1, d2);

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
        return 80;
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
}
