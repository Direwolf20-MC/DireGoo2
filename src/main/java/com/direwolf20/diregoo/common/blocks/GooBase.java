package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.common.entities.GooEntity;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GooBase extends Block {
    public GooBase() {
        super(
                Properties.create(Material.IRON).hardnessAndResistance(2.0f).tickRandomly()
        );
    }

    //Reset the block
    public static void resetBlock(ServerWorld world, BlockPos pos) {
        BlockSave blockSave = BlockSave.get(world);
        BlockState oldState = blockSave.getStateFromPos(pos);
        world.addEntity(new GooEntity(world, pos));
        if (oldState == null) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        world.setBlockState(pos, oldState);
        blockSave.pop(pos);

        CompoundNBT oldNBT = blockSave.getTEFromPos(pos);
        if (oldNBT.isEmpty()) return;
        TileEntity te = world.getTileEntity(pos);
        te.func_230337_a_(oldState, oldNBT);
        blockSave.popTE(pos);

    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isRemote) return ActionResultType.SUCCESS; //Server Side Only
        resetBlock((ServerWorld) world, pos);
        return ActionResultType.SUCCESS;
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 3))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        int x = random.nextInt(Direction.values().length);
        Direction direction = Direction.values()[x];
        //Direction direction = Direction.NORTH;
        System.out.println("randomTick Firing at " + pos + " Direction: " + direction);
        BlockPos checkPos = pos.offset(direction);
        BlockState oldState = worldIn.getBlockState(checkPos);

        BlockSave blockSave = BlockSave.get(worldIn);
        //System.out.println(blockSave);
        if (!oldState.equals(this.getDefaultState())) {
            TileEntity te = worldIn.getTileEntity(checkPos);
            CompoundNBT nbtData = new CompoundNBT();
            if (te != null) {
                te.write(nbtData);
                worldIn.removeTileEntity(checkPos);
            }
            worldIn.setBlockState(checkPos, this.getDefaultState());
            if (!oldState.equals(Blocks.AIR.getDefaultState()))
                blockSave.push(checkPos, oldState);
            if (nbtData != new CompoundNBT())
                blockSave.pushTE(checkPos, nbtData);
        }
    }
}
