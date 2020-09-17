package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.common.entities.GNTEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.TNTBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GNTBlock extends TNTBlock {
    public int radius = 20;

    public GNTBlock() {
        super(Block.Properties.create(Material.TNT).zeroHardnessAndResistance().sound(SoundType.PLANT));
        this.setDefaultState(this.getDefaultState().with(UNSTABLE, Boolean.valueOf(false)));
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            GNTEntity tntentity = new GNTEntity(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, (short) radius, explosionIn.getExplosivePlacedBy());
            tntentity.setFuse((short) (worldIn.rand.nextInt(tntentity.getFuse() / 4) + tntentity.getFuse() / 8));
            worldIn.addEntity(tntentity);
        }
    }

    @Override
    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) {
        if (!world.isRemote) {
            GNTEntity tntentity = new GNTEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, (short) radius, igniter);
            tntentity.setFuse((short) (40));
            world.playSound((PlayerEntity) null, tntentity.getPosX(), tntentity.getPosY(), tntentity.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.addEntity(tntentity);
        }
    }
}
