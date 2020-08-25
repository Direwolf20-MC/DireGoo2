package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.client.particles.ModParticles;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
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
            Vector3d pos = this.getPositionVec().add(0.5, 0.5, 0.5); //Start at center of block
            Random random = new Random();
            int sideRandom = random.nextInt(Direction.values().length);
            Direction direction = Direction.values()[sideRandom]; //Pick a side at random, this is the side the particle will spawn on
            double d0 = pos.getX();
            double d1 = pos.getY();
            double d2 = pos.getZ();
            if (direction.getXOffset() != 0) { //if the chose side is X, then randomly pick the y/z positions. Move the X position to just outside the edge of the block
                d0 = pos.getX() + ((double) direction.getXOffset() / 2 + (0.01 * direction.getXOffset()));
                d1 = pos.getY() + random.nextDouble() - 0.5;
                d2 = pos.getZ() + random.nextDouble() - 0.5;
            } else if (direction.getYOffset() != 0) {
                d0 = pos.getX() + random.nextDouble() - 0.5;
                d1 = pos.getY() + ((double) direction.getYOffset() / 2 + (0.01 * direction.getYOffset()));
                d2 = pos.getZ() + random.nextDouble() - 0.5;
            } else if (direction.getZOffset() != 0) {
                d0 = pos.getX() + random.nextDouble() - 0.5;
                d1 = pos.getY() + random.nextDouble() - 0.5;
                d2 = pos.getZ() + ((double) direction.getZOffset() / 2 + (0.01 * direction.getZOffset()));
            }
            double r = 0, g = 0, b = 0;
            if (dataManager.get(gooBlockState).get().equals(ModBlocks.GOO_BLOCK.get().getDefaultState())) {
                r = 0.1;
                g = 0.1;
                b = 1;
            }
            if (dataManager.get(gooBlockState).get().equals(ModBlocks.GOO_BLOCK_TERRAIN.get().getDefaultState())) {
                r = 0.1;
                g = 0.5;
                b = 0.1;
            }
            if (dataManager.get(gooBlockState).get().equals(ModBlocks.GOO_BLOCK_POISON.get().getDefaultState())) {
                r = 0.75;
                g = 0.1;
                b = 0.75;
            }
            if (dataManager.get(gooBlockState).get().equals(ModBlocks.GOO_BLOCK_BURST.get().getDefaultState())) {
                r = 0.85;
                g = 0.05;
                b = 0.05;
            }
            this.world.addParticle(ModParticles.GOO_DRIP_PARTICLE, d0, d1, d2, r, g, b);
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