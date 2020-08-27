package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class GooRender extends Block {
    public static final IntegerProperty GROWTH = IntegerProperty.create("growth", 1, 9);

    public GooRender() {
        super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2.0f));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isRemote) return ActionResultType.SUCCESS; //Server Side Only
        //world.setBlockState(pos, state.with(GROWTH, newState));
        int x = new Random().nextInt(Direction.values().length);
        Direction direction = Direction.values()[x];
        direction = Direction.EAST;
        BlockPos newPos = pos.offset(direction);
        List<GooSpreadEntity> list = world.getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(newPos.getX(), newPos.getY(), newPos.getZ(), newPos.getX() + 0.25d, newPos.getY() + 0.25d, newPos.getZ() + 0.25d));
        if (list.isEmpty())
            world.addEntity(new GooSpreadEntity(world, newPos, ModBlocks.GOO_BLOCK.get().getDefaultState(), 50, direction.getOpposite().getIndex()));
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(GROWTH);
    }
}
