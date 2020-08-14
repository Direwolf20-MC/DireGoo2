package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

public class GooEntity extends EntityBase {
    @ObjectHolder(DireGoo.MOD_ID + ":gooentity")
    public static EntityType<GooEntity> TYPE;
    public static int MAXLIFE = 80;

    public GooEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public GooEntity(World world, BlockPos spawnPos) {
        this(TYPE, world);

        setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        targetPos = spawnPos;
    }

    @Override
    protected int getMaxLife() {
        return MAXLIFE;
    }

    @Override
    protected void registerData() {
    }

    @Override
    protected void onSetDespawning() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}