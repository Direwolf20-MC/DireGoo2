package com.direwolf20.diregoo.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class EntityBase extends Entity {
    private int despawning = -1;
    protected BlockPos targetPos;

    public EntityBase(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    protected abstract int getMaxLife();

    protected abstract void onSetDespawning();

    @Override
    public void baseTick() {
        super.baseTick();
        if (despawning == -1 && shouldSetDespawning()) {
            despawning = 0;
            onSetDespawning();
        } else if (despawning != -1 && ++despawning > 1)
            remove();
    }

    protected boolean shouldSetDespawning() {
        return ticksExisted > getMaxLife();
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        despawning = compound.getInt("despawning");
        ticksExisted = compound.getInt("ticksExisted");
        targetPos = NBTUtil.readBlockPos(compound.getCompound("targetPos"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("despawning", despawning);
        compound.putInt("ticksExisted", ticksExisted);
        compound.put("targetPos", NBTUtil.writeBlockPos(targetPos));
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }
}
