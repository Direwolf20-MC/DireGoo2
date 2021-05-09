package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class GNTEntity extends TNTEntity {
    @ObjectHolder(DireGoo.MOD_ID + ":gntentity")
    public static EntityType<GNTEntity> TYPE;

    private short radius;

    public GNTEntity(EntityType<GNTEntity> entityType, World world) {
        super(entityType, world);
    }

    public GNTEntity(World worldIn, double x, double y, double z, short radius, @Nullable LivingEntity igniter) {
        this(TYPE, worldIn);
        this.setPosition(x, y, z);
        double d0 = worldIn.rand.nextDouble() * (double) ((float) Math.PI * 2F);
        this.setMotion(-Math.sin(d0) * 0.02D, (double) 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.igniter = igniter;
        this.radius = radius;
    }

    public short getRadius() {
        return radius;
    }

    @Override
    protected void explode() {
        BlockSave blockSave = BlockSave.get(world);
        BlockPos hitPos = new BlockPos(getPosX(), getPosY(), getPosZ());
        BlockPos minPos = hitPos.add(-radius, -radius, -radius);
        BlockPos maxPos = hitPos.add(radius, radius, radius);
        List<BlockPos> area = BlockPos.getAllInBox(minPos, maxPos)
                .filter(blockPos -> (world.getBlockState(blockPos).getBlock() instanceof GooBase) && (blockPos.distanceSq(this.getPosition()) < radius * radius))
                .map(BlockPos::toImmutable)
                .collect(Collectors.toList());
        for (BlockPos pos : area)
            GooBase.resetBlock((ServerWorld) world, pos, false, 40, true, blockSave);
        List<GooSpreadEntity> gooSpreadEntities = world.getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(minPos, maxPos));
        for (GooSpreadEntity gooSpreadEntity : gooSpreadEntities)
            gooSpreadEntity.remove();
        radius++;
        minPos = hitPos.add(-radius, -radius, -radius);
        maxPos = hitPos.add(radius, radius, radius);
        List<BlockPos> area2 = BlockPos.getAllInBox(minPos, maxPos)
                .filter(blockPos -> world.getBlockState(blockPos).getBlock() instanceof GooBase)
                .map(BlockPos::toImmutable)
                .collect(Collectors.toList());
        for (BlockPos pos : area2)
            GooBase.freezeGoo((ServerWorld) world, pos);
        world.playSound((PlayerEntity) null, getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 2.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("radius", (short) this.getRadius());
        super.writeAdditional(compound);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.radius = compound.getShort("radius");
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
