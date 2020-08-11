package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.common.entities.GooEntity;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class GooBase extends Block {
    public GooBase() {
        super(
                Properties.create(Material.IRON).hardnessAndResistance(2.0f).tickRandomly()
        );
    }

    //Reset the block
    public static void resetBlock(ServerWorld world, BlockPos pos, boolean render) {
        BlockSave blockSave = BlockSave.get(world);
        BlockState oldState = blockSave.getStateFromPos(pos);
        if (render)
            world.addEntity(new GooEntity(world, pos));
        if (oldState == null) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        world.setBlockState(pos, oldState);
        blockSave.pop(pos);

        CompoundNBT oldNBT = blockSave.getTEFromPos(pos);
        if (oldNBT == null) return;
        TileEntity te = world.getTileEntity(pos);
        te.func_230337_a_(oldState, oldNBT);
        blockSave.popTE(pos);

    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isRemote) return ActionResultType.PASS; //Server Side Only

        world.getPendingBlockTicks().scheduleTick(pos, this, 0);
        return ActionResultType.PASS;
    }

    public static boolean isPlayerInRange(ServerWorld world, BlockPos pos) {
        List<ServerPlayerEntity> playerList = world.getPlayers();
        for (ServerPlayerEntity player : playerList) {
            BlockPos playerPos = new BlockPos(player.getPositionVec());
            if (pos.withinDistance(playerPos, (double) Config.PLAYER_SPREAD_RANGE.get()))
                return true;
        }

        return false;
    }

    /**
     * Performs a tick on a block. This method is called by randomTick by default. It can also be scheduled with world.getPendingBlockTicks().scheduleTick
     */
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 3))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading

        /*if (rand.nextInt(100) <= 50)
            return;*/

        if (!isPlayerInRange(worldIn, pos)) {
            //System.out.println("Stopping spread at " + pos + " because no players in range");
            return;
        }

        if (isSurrounded(worldIn, pos)) {
            //System.out.println(pos + " is surrounded!");
            return;
        }

        if (Config.CAN_SPREAD.get()) {
            BlockPos gooPos = spreadGoo(state, worldIn, pos, rand);
            if (gooPos != BlockPos.ZERO)
                worldIn.getPendingBlockTicks().scheduleTick(gooPos, this, Config.SPREAD_TICK_DELAY.get());
        }


    }

    public boolean isSurrounded(ServerWorld worldIn, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (!worldIn.getBlockState(pos.offset(direction)).getBlock().equals(this)) {
                return false;
            }
        }

        return true;
    }

    public boolean canSpreadHere(BlockPos pos, BlockState oldState, World world) {
        if (oldState.equals(this.getDefaultState()))
            return false;
        BlockSave blockSave = BlockSave.get(world);
        if (blockSave.checkAnti(pos))
            return false;
        return true;
    }

    public BlockPos spreadGoo(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        int x = rand.nextInt(Direction.values().length);
        Direction direction = Direction.values()[x];

        BlockPos checkPos = pos.offset(direction);
        BlockState oldState = worldIn.getBlockState(checkPos);
        //System.out.println(direction + ":" + checkPos);

        //System.out.println(blockSave);
        if (canSpreadHere(checkPos, oldState, worldIn)) {
            BlockSave blockSave = BlockSave.get(worldIn);
            TileEntity te = worldIn.getTileEntity(checkPos);
            CompoundNBT nbtData = new CompoundNBT();
            if (te != null) {
                te.write(nbtData);
                worldIn.removeTileEntity(checkPos);
                blockSave.pushTE(checkPos, nbtData);
            }
            worldIn.setBlockState(checkPos, this.getDefaultState());
            if (!oldState.equals(Blocks.AIR.getDefaultState()))
                blockSave.push(checkPos, oldState);

        }
        return checkPos;
    }
}
