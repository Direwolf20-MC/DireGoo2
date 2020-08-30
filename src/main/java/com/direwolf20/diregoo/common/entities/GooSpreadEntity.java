package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Optional;

public class GooSpreadEntity extends EntityBase {
    @ObjectHolder(DireGoo.MOD_ID + ":goospreadentity")
    public static EntityType<GooSpreadEntity> TYPE;

    private static final DataParameter<Optional<BlockState>> gooBlockState = EntityDataManager.createKey(GooSpreadEntity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Integer> MAXLIFE = EntityDataManager.createKey(GooSpreadEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DIRECTION = EntityDataManager.createKey(GooSpreadEntity.class, DataSerializers.VARINT);

    public GooSpreadEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public GooSpreadEntity(World world, BlockPos spawnPos, BlockState gooBlock, int maxLife, int directionFrom) {
        this(TYPE, world);

        setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        targetPos = spawnPos;
        dataManager.set(gooBlockState, Optional.of(gooBlock));
        dataManager.set(MAXLIFE, maxLife);
        dataManager.set(DIRECTION, directionFrom);
    }

    public BlockState getGooBlockState() {
        return dataManager.get(gooBlockState).get();
    }

    public Direction getDirection() {
        return Direction.values()[dataManager.get(DIRECTION)];
    }

    @Override
    protected void registerData() {
        dataManager.register(gooBlockState, Optional.of(Blocks.AIR.getDefaultState()));
        dataManager.register(MAXLIFE, 80);
        dataManager.register(DIRECTION, 0);
    }

    @Override
    protected int getMaxLife() {
        return dataManager.get(MAXLIFE);
    }

    @Override
    protected void onSetDespawning() {
        if (!world.isRemote) {
            BlockState oldState = world.getBlockState(this.targetPos);
            GooBase.saveBlockData(world, this.targetPos, oldState);
            world.setBlockState(this.targetPos, dataManager.get(gooBlockState).get());
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        dataManager.set(gooBlockState, Optional.ofNullable(NBTUtil.readBlockState(compound.getCompound("gooblockstate"))));
        dataManager.set(MAXLIFE, compound.getInt("maxlife"));
        dataManager.set(DIRECTION, compound.getInt("direction"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.put("gooblockstate", NBTUtil.writeBlockState(dataManager.get(gooBlockState).get()));
        compound.putInt("maxlife", dataManager.get(MAXLIFE));
        compound.putInt("direction", dataManager.get(DIRECTION));
        super.writeAdditional(compound);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}