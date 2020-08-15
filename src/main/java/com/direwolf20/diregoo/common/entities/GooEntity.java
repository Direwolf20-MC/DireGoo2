package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Optional;

public class GooEntity extends EntityBase {
    @ObjectHolder(DireGoo.MOD_ID + ":gooentity")
    public static EntityType<GooEntity> TYPE;
    public static int MAXLIFE = 80;

    private static final DataParameter<Optional<BlockState>> gooBlockState = EntityDataManager.createKey(GooEntity.class, DataSerializers.OPTIONAL_BLOCK_STATE);

    public GooEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public GooEntity(World world, BlockPos spawnPos, BlockState gooBlock) {
        this(TYPE, world);

        setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        targetPos = spawnPos;
        dataManager.set(gooBlockState, Optional.of(gooBlock));
    }

    public BlockState getGooBlockState() {
        return dataManager.get(gooBlockState).get();
    }

    @Override
    protected void registerData() {
        dataManager.register(gooBlockState, Optional.of(Blocks.AIR.getDefaultState()));
    }

    @Override
    protected int getMaxLife() {
        return MAXLIFE;
    }

    @Override
    protected void onSetDespawning() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        dataManager.set(gooBlockState, Optional.ofNullable(NBTUtil.readBlockState(compound.getCompound("gooblockstate"))));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.put("gooblockstate", NBTUtil.writeBlockState(dataManager.get(gooBlockState).get()));
        super.writeAdditional(compound);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}