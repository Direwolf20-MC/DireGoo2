package com.direwolf20.diregoo.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBase extends Block {
    public GooBase() {
        super(
                Properties.create(Material.IRON).hardnessAndResistance(2.0f).tickRandomly()
        );
    }


    /**
     * Performs a random tick on a block.
     */
    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        int x = random.nextInt(Direction.values().length);
        Direction direction = Direction.values()[x];
        System.out.println("randomTick Firing at " + pos + " Direction: " + direction);
        BlockPos checkPos = pos.offset(direction);
        if (!worldIn.getBlockState(checkPos).equals(this.getDefaultState())) {
            worldIn.setBlockState(checkPos,this.getDefaultState());
        }
    }
}
