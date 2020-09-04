package com.direwolf20.diregoo.common.util;

import com.direwolf20.diregoo.common.blocks.GooBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles collecting the blocks for the goo freezer.
 *
 * @implNote Currently done using static reference but having it as a dynamic class
 * may work in the future as more methods and functionality is added.
 */
public class FreezingCollect {
    public static List<BlockPos> collect(LivingEntity player, BlockRayTraceResult startBlock, World world) {
        List<BlockPos> coordinates = new ArrayList<>();
        BlockPos startPos = startBlock.getPos();

        Direction side = startBlock.getFace();
        boolean vertical = side.getAxis().isVertical();
        Direction up = vertical ? player.getHorizontalFacing() : Direction.UP;
        Direction down = up.getOpposite();
        Direction right = vertical ? up.rotateY() : side.rotateYCCW();
        Direction left = right.getOpposite();

        coordinates.add(startPos.offset(up).offset(left));
        coordinates.add(startPos.offset(up));
        coordinates.add(startPos.offset(up).offset(right));
        coordinates.add(startPos.offset(left));
        coordinates.add(startPos);
        coordinates.add(startPos.offset(right));
        coordinates.add(startPos.offset(down).offset(left));
        coordinates.add(startPos.offset(down));
        coordinates.add(startPos.offset(down).offset(right));

        return coordinates.stream().filter(e -> isValid(player, e, world)).collect(Collectors.toList());
    }

    private static boolean isValid(LivingEntity player, BlockPos pos, World world) {
        BlockState state = world.getBlockState(pos);

        // Validate the block is a GooBase
        if (state.getBlock() instanceof GooBase)
            return true;

        return false;
    }
}
