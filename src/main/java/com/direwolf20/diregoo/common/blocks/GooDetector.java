package com.direwolf20.diregoo.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.stream.Stream;

import static net.minecraft.block.RedstoneDiodeBlock.POWERED;

public class GooDetector extends Block {
    protected static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(6.5, 2, 6.5, 9.5, 3, 9.5),
            Block.makeCuboidShape(0, 0, 0, 16, 2, 16),
            Block.makeCuboidShape(7, 6, 2, 9, 8, 14),
            Block.makeCuboidShape(2, 6, 7, 14, 8, 9),
            Block.makeCuboidShape(7, 2, 7, 9, 13, 9)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    public GooDetector() {
        super(
                Properties.create(Material.IRON).hardnessAndResistance(2.0f)
        );
        this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, Boolean.valueOf(false)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public boolean checkForGoo(World worldIn, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (worldIn.getBlockState(pos.offset(direction)).getBlock() instanceof GooBase)
                return true;
        }
        return false;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        worldIn.setBlockState(pos, state.with(POWERED, checkForGoo(worldIn, pos)));
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.get(POWERED) ? 15 : 0;
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.get(POWERED) ? 15 : 0;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }
}
