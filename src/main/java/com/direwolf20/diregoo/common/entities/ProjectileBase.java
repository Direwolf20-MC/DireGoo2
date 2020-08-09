package com.direwolf20.diregoo.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class ProjectileBase extends ProjectileItemEntity {
    protected int despawning = -1;

    public ProjectileBase(EntityType<? extends ProjectileBase> type, World world) {
        super(type, world);
    }


    public ProjectileBase(EntityType<? extends ProjectileBase> type, World world, LivingEntity thrower) {
        super(type, thrower, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (despawning == -1 && shouldSetDespawning()) {
            despawning = 0;
            onSetDespawning();
        } else if (despawning != -1 && ++despawning > 1)
            remove();
    }

    @Override
    public void setItem(ItemStack stack) {
        super.setItem(stack);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    protected boolean shouldSetDespawning() {
        return ticksExisted > getMaxLife();
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        despawning = compound.getInt("despawning");
        ticksExisted = compound.getInt("ticksExisted");
        System.out.println(compound);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putInt("despawning", despawning);
        compound.putInt("ticksExisted", ticksExisted);
        System.out.println(compound);
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
}
