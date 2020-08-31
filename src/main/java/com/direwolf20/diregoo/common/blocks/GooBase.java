package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.common.entities.GooEntity;
import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class GooBase extends Block {

    public static final IntegerProperty FROZEN = IntegerProperty.create("frozen", 0, 3);


    public GooBase() {
        super(
                Properties.create(Material.IRON).hardnessAndResistance(2.0f).tickRandomly()
        );
    }

    //Reset the block
    public static void resetBlock(ServerWorld world, BlockPos pos, boolean render, int gooRenderLife) {
        BlockSave blockSave = BlockSave.get(world);
        BlockState oldState = blockSave.getStateFromPos(pos);
        if (render)
            world.addEntity(new GooEntity(world, pos, world.getBlockState(pos), gooRenderLife));
        if (oldState == null) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        world.setBlockState(pos, oldState);
        blockSave.pop(pos);

        CompoundNBT oldNBT = blockSave.getTEFromPos(pos);
        if (oldNBT == null) return;
        TileEntity te = world.getTileEntity(pos);
        te.read(oldState, oldNBT);
        blockSave.popTE(pos);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isRemote) return ActionResultType.FAIL; //Server Side Only
        world.getPendingBlockTicks().scheduleTick(pos, this, 0);
        return ActionResultType.FAIL;
    }

    public static boolean isPlayerInRange(ServerWorld world, BlockPos pos) {
        List<ServerPlayerEntity> playerList = world.getPlayers();
        for (ServerPlayerEntity player : playerList) {
            BlockPos playerPos = new BlockPos(player.getPositionVec()); //new BlockPos(264, 63, -173);
            if (pos.withinDistance(playerPos, (double) Config.PLAYER_SPREAD_RANGE.get()))
                return true;
        }

        return false;
    }

    public static void freezeGoo(ServerWorld worldIn, BlockPos pos) {
        BlockState oldState = worldIn.getBlockState(pos);
        if (!(oldState.getBlock() instanceof GooBase)) {
            return;
        }
        worldIn.setBlockState(pos, oldState.with(FROZEN, 3));
    }

    public static boolean handleFrozen(BlockPos pos, BlockState state, World worldIn) {
        int frozenState = state.get(FROZEN);
        if (frozenState > 0) {
            frozenState--;
            worldIn.setBlockState(pos, state.with(FROZEN, frozenState));
            return true;
        }
        return false;
    }

    /**
     * Override this for custom goo types
     */
    public boolean customPreChecks(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        return true;
    }

    /**
     * Checks to see if goo should spread, before attempting to spread
     */
    public boolean shouldGooSpread(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!Config.CAN_SPREAD_ALL.get())
            return false; //Check the config options to see if goo spreading is disabled

        if (!worldIn.isAreaLoaded(pos, 3))
            return false; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading

        if (!isPlayerInRange(worldIn, pos))
            return false;

        if (isSurrounded(worldIn, pos))
            return false;

        if (!customPreChecks(state, worldIn, pos, rand))
            return false;

        return true;
    }

    /**
     * Determines if goo can spread to the target POS in the world, this runs after calculating where to spread to
     */
    public boolean canSpreadHere(BlockPos pos, BlockState oldState, World world) {
        if (!world.isAreaLoaded(pos, 3))
            return false; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        if (oldState.getBlock() instanceof GooBase)
            return false; //No eating other goo blocks
        if (oldState.getBlockHardness(world, pos) < 0)
            return false; //No Eating bedrock
        if (pos.getY() < 0 || pos.getY() > 254)
            return false; //No spreading below Y=0

        BlockSave blockSave = BlockSave.get(world);
        if (blockSave.checkAnti(pos))
            return false; //Check the antiGoo list
        return true;
    }

    /**
     * Performs a tick on a block. This method is called by randomTick by default. It can also be scheduled with world.getPendingBlockTicks().scheduleTick
     */
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!shouldGooSpread(state, worldIn, pos, rand))
            return;
        if (handleFrozen(pos, state, worldIn)) return;
        BlockPos gooPos = spreadGoo(state, worldIn, pos, rand, true);
        forceExtraTick(worldIn, gooPos);
    }

    public void forceExtraTick(ServerWorld world, BlockPos pos) {
        if (pos != BlockPos.ZERO)
            if (Config.SPREAD_TICK_DELAY.get() != -1) {
                world.getPendingBlockTicks().scheduleTick(pos, this, Config.SPREAD_TICK_DELAY.get());
            }
    }

    /**
     * Checks to see if a gooBlock is surrounded by any other kind of gooblock - if so don't bother doing any other calculations
     */
    public static boolean isSurrounded(ServerWorld worldIn, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (worldIn.getBlockState(pos.offset(direction)).equals(ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GooBlockPoison.GENERATION, 0)))
                return false; //If the adjacent block is Generation 0 poison, it's not surrounded.
            if (!(worldIn.getBlockState(pos.offset(direction)).getBlock() instanceof GooBase)) {
                return false; //If the adjacent block is anything other than goo its not surrounded
            }
        }
        return true;
    }

    public BlockPos spreadGoo(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand, boolean animate) {
        int x = rand.nextInt(Direction.values().length);
        Direction direction = Direction.values()[x];

        BlockPos checkPos = pos.offset(direction);
        BlockState oldState = worldIn.getBlockState(checkPos);

        if (oldState.equals(ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GooBlockPoison.GENERATION, 0))) {
            worldIn.setBlockState(pos, ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GooBlockPoison.GENERATION, 1));
            worldIn.getPendingBlockTicks().scheduleTick(pos, ModBlocks.GOO_BLOCK_POISON.get(), 5);
            resetBlock(worldIn, checkPos, true, 80);
            return BlockPos.ZERO;
        }

        if (canSpreadHere(checkPos, oldState, worldIn)) {
            if (animate) {
                List<GooSpreadEntity> list = worldIn.getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(checkPos.getX(), checkPos.getY(), checkPos.getZ(), checkPos.getX() + 0.25d, checkPos.getY() + 0.25d, checkPos.getZ() + 0.25d));
                if (!list.isEmpty())
                    return BlockPos.ZERO;
            }
            if (handleSpecialCases(worldIn, oldState, checkPos, animate, direction))
                return BlockPos.ZERO;
            setBlockToGoo(oldState, worldIn, checkPos, animate, direction);
        } else {
            return BlockPos.ZERO;
        }
        return checkPos;
    }

    public void setBlockToGoo(BlockState oldState, World worldIn, BlockPos checkPos, boolean animate, Direction direction) {
        if (animate) {
            worldIn.addEntity(new GooSpreadEntity(worldIn, checkPos, this.getDefaultState(), oldState, 20, direction.getOpposite().getIndex()));
        } else {
            saveBlockData(worldIn, checkPos, oldState);
            worldIn.setBlockState(checkPos, this.getDefaultState());
        }
    }

    public static void saveBlockData(World worldIn, BlockPos checkPos, BlockState oldState) {
        BlockSave blockSave = BlockSave.get(worldIn);
        TileEntity te = worldIn.getTileEntity(checkPos);
        CompoundNBT nbtData = new CompoundNBT();
        if (te != null) {
            te.write(nbtData);
            worldIn.removeTileEntity(checkPos);
            blockSave.pushTE(checkPos, nbtData);
        }
        if (!oldState.equals(Blocks.AIR.getDefaultState()))
            blockSave.push(checkPos, oldState);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FROZEN);
    }

    public boolean handleSpecialCases(World world, BlockState blockState, BlockPos blockPos, boolean animate, Direction direction) {
        if (blockState.getBlock() == Blocks.PISTON || blockState.getBlock() == Blocks.STICKY_PISTON) {
            if (blockState.get(PistonBlock.EXTENDED)) {
                BlockState newstate = blockState.with(PistonBlock.EXTENDED, false);
                BlockPos additionalPos = blockPos.offset(blockState.get(PistonBlock.FACING));
                setBlockToGoo(newstate, world, blockPos, animate, direction);
                setBlockToGoo(Blocks.AIR.getDefaultState(), world, additionalPos, animate, direction);
                return true;
            } else {
                return false;
            }
        }
        if (blockState.getBlock() == Blocks.PISTON_HEAD) {
            BlockPos additionalPos = blockPos.offset(blockState.get(PistonBlock.FACING).getOpposite());
            BlockState oldState = world.getBlockState(additionalPos);
            if (oldState.getBlock() == Blocks.PISTON || oldState.getBlock() == Blocks.STICKY_PISTON) {
                BlockState newstate = oldState.with(PistonBlock.EXTENDED, false);
                setBlockToGoo(newstate, world, additionalPos, animate, direction);
                setBlockToGoo(Blocks.AIR.getDefaultState(), world, blockPos, animate, direction);
            }
            return true;
        }
        return false;
    }
}
