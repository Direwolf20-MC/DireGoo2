package com.direwolf20.diregoo.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBlockPoison extends Block {

    public static final IntegerProperty GENERATION = IntegerProperty.create("generation", 0, 5);

    public GooBlockPoison() {
        super(
                Properties.create(Material.IRON).hardnessAndResistance(2.0f).tickRandomly()
        );
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 3))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        if (state.get(GENERATION) == 0)
            return; //Placed versions of this block do nothing. It activates when another goo tries to eat it
        if (rand.nextDouble() <= decayChance(state)) { //The percent chance it decays rather than spreads. Lower == more spread.
            GooBase.resetBlock(worldIn, pos, true, 20);
        } else {
            //System.out.println("Generation " + state.get(GENERATION) + " is spreading");
            spreadGoo(state, worldIn, pos, rand);
        }
    }

    private double decayChance(BlockState gooState) {
        int generation = gooState.get(GENERATION);
        switch (generation) {
            case 1:
                return 0.01;
            case 2:
                return 0.05;
            case 3:
                return 0.10;
            case 4:
                return 0.15;
            case 5:
                return 0.40;
        }
        return 1.0;
    }

    public BlockPos spreadGoo(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(GENERATION) <= 4) {
            for (Direction direction : Direction.values()) {
                BlockPos checkPos = pos.offset(direction);
                BlockState oldState = worldIn.getBlockState(checkPos);

                if (oldState.getBlock() instanceof GooBlock && !(oldState.getBlock() instanceof GooBlockPoison)) {
                    int generation = state.get(GENERATION);
                    if (generation < 5)
                        generation++;
                    worldIn.setBlockState(checkPos, ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GENERATION, generation));
                    worldIn.setBlockState(pos, ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GENERATION, generation));
                    worldIn.getPendingBlockTicks().scheduleTick(checkPos, this, 5);
                }
                worldIn.getPendingBlockTicks().scheduleTick(pos, this, 5);
            }
            return BlockPos.ZERO;
        } else {
            int x = rand.nextInt(Direction.values().length);
            Direction direction = Direction.values()[x];

            BlockPos checkPos = pos.offset(direction);
            BlockState oldState = worldIn.getBlockState(checkPos);

            if (oldState.getBlock() instanceof GooBlock && !(oldState.getBlock() instanceof GooBlockPoison)) {
                int generation = state.get(GENERATION);
                if (generation < 5)
                    generation++;
                worldIn.setBlockState(checkPos, ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GENERATION, generation));
                worldIn.setBlockState(pos, ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GENERATION, generation));
                worldIn.getPendingBlockTicks().scheduleTick(checkPos, this, 5);
            }
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, 5);
            return BlockPos.ZERO;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(GENERATION);
    }
}
