package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.client.particles.ModParticles;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Optional;
import java.util.Random;

public class GooEntity extends EntityBase {
    @ObjectHolder(DireGoo.MOD_ID + ":gooentity")
    public static EntityType<GooEntity> TYPE;

    private static final DataParameter<Optional<BlockState>> gooBlockState = EntityDataManager.createKey(GooEntity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Integer> MAXLIFE = EntityDataManager.createKey(GooEntity.class, DataSerializers.VARINT);

    public GooEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public GooEntity(World world, BlockPos spawnPos, BlockState gooBlock, int maxLife) {
        this(TYPE, world);

        setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        targetPos = spawnPos;
        dataManager.set(gooBlockState, Optional.of(gooBlock));
        dataManager.set(MAXLIFE, maxLife);
    }

    public BlockState getGooBlockState() {
        return dataManager.get(gooBlockState).get();
    }

    @Override
    public void baseTick() {
        if (world.isRemote) {
            Vector3d pos = this.getPositionVec();
            Random random = new Random();
            double d0 = pos.getX() + random.nextDouble();
            double d1 = pos.getY() - 0.01;
            double d2 = pos.getZ() + random.nextDouble();
            this.world.addParticle(ModParticles.GOO_DRIP_PARTICLE, d0, d1, d2, 0.0D, -0.1D, 0.0D);
        }
        super.baseTick();
    }

    @Override
    protected void registerData() {
        dataManager.register(gooBlockState, Optional.of(Blocks.AIR.getDefaultState()));
        dataManager.register(MAXLIFE, 80);
    }

    @Override
    protected int getMaxLife() {
        return dataManager.get(MAXLIFE);
    }

    @Override
    protected void onSetDespawning() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        dataManager.set(gooBlockState, Optional.ofNullable(NBTUtil.readBlockState(compound.getCompound("gooblockstate"))));
        dataManager.set(MAXLIFE, compound.getInt("maxlife"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.put("gooblockstate", NBTUtil.writeBlockState(dataManager.get(gooBlockState).get()));
        compound.putInt("maxlife", dataManager.get(MAXLIFE));
        super.writeAdditional(compound);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}