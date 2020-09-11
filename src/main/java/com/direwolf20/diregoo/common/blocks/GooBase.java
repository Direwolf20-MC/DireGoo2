package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.client.particles.ModParticles;
import com.direwolf20.diregoo.common.data.GeneratorBlockTags;
import com.direwolf20.diregoo.common.entities.GooEntity;
import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.direwolf20.diregoo.common.events.ChunkSave;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class GooBase extends Block {

    public static final IntegerProperty FROZEN = IntegerProperty.create("frozen", 0, 3);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final int gooSpreadAnimationTime = 20;

    public GooBase() {
        super(
                Properties.create(Material.IRON).hardnessAndResistance(2.0f).tickRandomly()
        );
    }

    //Reset the block
    public static void resetBlock(ServerWorld world, BlockPos pos, boolean render, int gooRenderLife, boolean calcSideRender, BlockSave blockSave) {
        blockSave.addBlockChange(world.getGameTime());
        blockSave.addChunkChange(world.getGameTime(), world.getChunk(pos).getPos());
        BlockState oldState = ChunkSave.getStateFromPos(pos, blockSave, world.getChunk(pos).getPos());
        if (render)
            world.addEntity(new GooEntity(world, pos, world.getBlockState(pos), gooRenderLife, calcSideRender));
        if (oldState == null) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        if (!resetSpecialCase(oldState, world, pos, render, gooRenderLife, blockSave)) {
            world.setBlockState(pos, oldState);
            ChunkSave.pop(pos, world.getChunk(pos).getPos());
        }
        CompoundNBT oldNBT = blockSave.getTEFromPos(pos);
        if (oldNBT == null) return;
        TileEntity te = world.getTileEntity(pos);
        try {
            te.read(oldState, oldNBT);
        } catch (Exception e) {
            System.out.println("Failed to restore tile data for block: " + oldState + " with NBT: " + oldNBT + ". Consider adding it to the blacklist");
        }
        blockSave.popTE(pos);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.get(ACTIVE);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        ItemStack stack = player.getHeldItemMainhand();
        if (!stack.isEmpty()) return ActionResultType.PASS;
        if (world.isRemote) return ActionResultType.SUCCESS; //Server Side Only
        world.getPendingBlockTicks().scheduleTick(pos, this, 0);
        return ActionResultType.SUCCESS;
    }

    public boolean isPlayerInRange(ServerWorld world, BlockPos pos) {
        return world.isPlayerWithin(pos.getX(), pos.getY(), pos.getZ(), Config.PLAYER_SPREAD_RANGE.get());
    }

    public static void freezeGoo(ServerWorld worldIn, BlockPos pos) {
        BlockState oldState = worldIn.getBlockState(pos);
        if (!(oldState.getBlock() instanceof GooBase)) {
            return;
        }
        worldIn.setBlockState(pos, oldState.with(FROZEN, 3).with(ACTIVE, true));
    }

    public static boolean handleFrozen(BlockPos pos, BlockState state, World worldIn, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 3))
            return true; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        if (rand.nextInt(100) > Config.FREEZE_MELT_CHANCE.get()) return true;
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

        if (isSurrounded(worldIn, pos, state))
            return false;

        if (!customPreChecks(state, worldIn, pos, rand))
            return false;

        return true;
    }

    /**
     * Determines if goo can spread to the target POS in the world, this runs after calculating where to spread to
     */
    public boolean canSpreadHere(BlockPos pos, BlockState oldState, ServerWorld world, BlockSave blockSave) {
        if (!world.isAreaLoaded(pos, 3))
            return false; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
        if (!isAdjacentValid(world, pos, oldState))
            return false;

        if (blockSave.checkAnti(pos))
            return false; //Check the antiGoo list
        return true;
    }

    /**
     * Performs a tick on a block. This method is called by randomTick by default. It can also be scheduled with world.getPendingBlockTicks().scheduleTick
     */
    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        BlockSave blockSave = BlockSave.get(worldIn);
        if (blockSave.getBlockChangeThisTick(worldIn.getGameTime()) >= Config.MAX_BLOCK_CHANGES.get()) return;
        if (blockSave.getChunkChangesThisTick(worldIn.getGameTime()) >= Config.MAX_CHUNK_CHANGES.get()) return;

        if (blockSave.getGooDeathEvent()) {
            if (blockSave.getBlockChangeThisTick(worldIn.getGameTime()) >= Config.MAX_BLOCK_CHANGES.get() / 2) return;
            if (blockSave.getChunkChangesThisTick(worldIn.getGameTime()) >= Config.MAX_CHUNK_CHANGES.get() / 2) return;
            boolean animate = worldIn.isPlayerWithin(pos.getX(), pos.getY(), pos.getZ(), 25);
            resetBlock(worldIn, pos, animate, 20, false, blockSave);
            return;
        }
        /*blockSave.addBlockChange(worldIn.getGameTime());
        blockSave.addChunkChange(worldIn.getGameTime(), worldIn.getChunk(pos).getPos());*/
        if (handleFrozen(pos, state, worldIn, rand)) return;
        if (!shouldGooSpread(state, worldIn, pos, rand))
            return;
        boolean animate = false;
        if (Config.ANIMATE_SPREAD.get())
            animate = worldIn.isPlayerWithin(pos.getX(), pos.getY(), pos.getZ(), 20);
        BlockPos gooPos = spreadGoo(state, worldIn, pos, rand, animate, blockSave);
        forceExtraTick(worldIn, gooPos);
    }

    public void forceExtraTick(ServerWorld world, BlockPos pos) {
        if (pos != BlockPos.ZERO)
            if (Config.SPREAD_TICK_DELAY.get() != -1) {
                if (Config.ANIMATE_SPREAD.get())
                    world.getPendingBlockTicks().scheduleTick(pos, this, gooSpreadAnimationTime + Config.SPREAD_TICK_DELAY.get());
                else
                    world.getPendingBlockTicks().scheduleTick(pos, this, Config.SPREAD_TICK_DELAY.get());
            }
    }

    /**
     * MultiPurpose function. #1 used to check if goo can spread into this block. #2 used to check if goo should 'deactivate' or 'reactivate'
     */
    public boolean isAdjacentValid(ServerWorld worldIn, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof GooBase)
            return false; //No eating other goo blocks
        if (state.getBlockHardness(worldIn, pos) < 0)
            return false; //No Eating bedrock
        if (state.isIn(GeneratorBlockTags.GOORESISTANT))
            return false; //No Eating our GooRestistant blocktag.
        if (pos.getY() < 0 || pos.getY() > 254)
            return false; //No spreading below Y=0
        return true;
    }

    /**
     * Checks to see if a gooBlock is surrounded by any other kind of gooblock - if so don't bother doing any other calculations
     */
    public boolean isSurrounded(ServerWorld worldIn, BlockPos pos, BlockState state) {
        for (Direction direction : Direction.values()) {
            BlockPos testPos = pos.offset(direction);
            BlockState testState = worldIn.getBlockState(testPos);
            if (isAdjacentValid(worldIn, testPos, testState)) {
                if (!state.get(ACTIVE))
                    worldIn.setBlockState(pos, state.with(ACTIVE, true));
                return false; //If the adjacent block is anything other than goo its not surrounded
            }
        }
        if (state.get(ACTIVE) && state.get(FROZEN) == 0)
            worldIn.setBlockState(pos, state.with(ACTIVE, false));
        return true;
    }

    public BlockPos spreadGoo(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand, boolean animate, BlockSave blockSave) {
        int x = rand.nextInt(Direction.values().length);
        Direction direction = Direction.values()[x];

        BlockPos checkPos = pos.offset(direction);
        BlockState oldState = worldIn.getBlockState(checkPos);

        if (oldState.getBlock().equals(ModBlocks.GOO_BLOCK_POISON.get())) {
            int newGeneration = (oldState.get(GooBlockPoison.GENERATION) == 5) ? 5 : oldState.get(GooBlockPoison.GENERATION) + 1;
            worldIn.setBlockState(pos, ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GooBlockPoison.GENERATION, newGeneration));
            worldIn.getPendingBlockTicks().scheduleTick(pos, ModBlocks.GOO_BLOCK_POISON.get(), 5);
            resetBlock(worldIn, checkPos, true, 80, false, blockSave);
            blockSave.addBlockChange(worldIn.getGameTime());
            blockSave.addChunkChange(worldIn.getGameTime(), worldIn.getChunk(pos).getPos());
            return BlockPos.ZERO;
        }

        if (canSpreadHere(checkPos, oldState, worldIn, blockSave)) {
            if (animate) {
                List<GooSpreadEntity> list = worldIn.getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(checkPos.getX(), checkPos.getY(), checkPos.getZ(), checkPos.getX() + 0.25d, checkPos.getY() + 0.25d, checkPos.getZ() + 0.25d));
                if (!list.isEmpty())
                    return BlockPos.ZERO;
            }
            if (handleSpecialCases(worldIn, oldState, checkPos, animate, direction, blockSave))
                return BlockPos.ZERO;
            setBlockToGoo(oldState, worldIn, checkPos, animate, direction, blockSave);
        } else {
            return BlockPos.ZERO;
        }
        return checkPos;
    }

    public void setBlockToGoo(BlockState oldState, World worldIn, BlockPos checkPos, boolean animate, Direction direction, BlockSave blockSave) {
        if (animate) {
            worldIn.addEntity(new GooSpreadEntity(worldIn, checkPos, this.getDefaultState(), gooSpreadAnimationTime, direction.getOpposite().getIndex()));
        } else {
            saveBlockData(worldIn, checkPos, oldState, blockSave);
            worldIn.setBlockState(checkPos, this.getDefaultState());
        }
        blockSave.addBlockChange(worldIn.getGameTime());
        blockSave.addChunkChange(worldIn.getGameTime(), worldIn.getChunk(checkPos).getPos());
    }

    public static void saveBlockData(World worldIn, BlockPos checkPos, BlockState oldState, BlockSave blockSave) {
        TileEntity te = worldIn.getTileEntity(checkPos);
        CompoundNBT nbtData = new CompoundNBT();
        if (te != null) {
            te.write(nbtData);
            worldIn.removeTileEntity(checkPos);
            blockSave.pushTE(checkPos, nbtData);
        }
        if (!oldState.equals(Blocks.AIR.getDefaultState()))
            ChunkSave.push(checkPos, oldState, blockSave, worldIn.getChunk(checkPos).getPos());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FROZEN);
        builder.add(ACTIVE);
    }

    public boolean handleSpecialCases(World world, BlockState blockState, BlockPos blockPos, boolean animate, Direction direction, BlockSave blockSave) {
        if (blockState.getBlock() == Blocks.PISTON || blockState.getBlock() == Blocks.STICKY_PISTON) {
            if (blockState.get(PistonBlock.EXTENDED)) {
                BlockState newstate = blockState.with(PistonBlock.EXTENDED, false);
                BlockPos additionalPos = blockPos.offset(blockState.get(PistonBlock.FACING));
                setBlockToGoo(newstate, world, blockPos, animate, direction, blockSave);
                setBlockToGoo(Blocks.AIR.getDefaultState(), world, additionalPos, animate, direction, blockSave);
                blockSave.addBlockChange(world.getGameTime());
                blockSave.addChunkChange(world.getGameTime(), world.getChunk(blockPos).getPos());
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
                setBlockToGoo(newstate, world, additionalPos, animate, direction, blockSave);
                setBlockToGoo(Blocks.AIR.getDefaultState(), world, blockPos, animate, direction, blockSave);
            }
            blockSave.addBlockChange(world.getGameTime());
            blockSave.addChunkChange(world.getGameTime(), world.getChunk(blockPos).getPos());
            return true;
        }
        if (blockState.getBlock() instanceof DoorBlock) {
            if (blockState.get(DoorBlock.HALF).equals(DoubleBlockHalf.LOWER)) {
                setBlockToGoo(blockState, world, blockPos, animate, direction, blockSave);
                setBlockToGoo(Blocks.AIR.getDefaultState(), world, blockPos.up(), animate, direction, blockSave);
            } else {
                setBlockToGoo(blockState, world, blockPos.down(), animate, direction, blockSave);
                setBlockToGoo(Blocks.AIR.getDefaultState(), world, blockPos, animate, direction, blockSave);
            }
            blockSave.addBlockChange(world.getGameTime());
            blockSave.addChunkChange(world.getGameTime(), world.getChunk(blockPos).getPos());
            return true;
        }
        if (blockState.getBlock() instanceof BedBlock) {
            if (blockState.get(BedBlock.PART).equals(BedPart.HEAD)) {
                BlockPos additionalPos = blockPos.offset(blockState.get(BedBlock.HORIZONTAL_FACING).getOpposite());
                setBlockToGoo(blockState, world, blockPos, animate, direction, blockSave);
                setBlockToGoo(Blocks.AIR.getDefaultState(), world, additionalPos, animate, direction, blockSave);
            } else {
                BlockPos additionalPos = blockPos.offset(blockState.get(BedBlock.HORIZONTAL_FACING));
                BlockState newState = world.getBlockState(additionalPos);
                setBlockToGoo(newState, world, additionalPos, animate, direction, blockSave);
                setBlockToGoo(Blocks.AIR.getDefaultState(), world, blockPos, animate, direction, blockSave);
            }
            blockSave.addBlockChange(world.getGameTime());
            blockSave.addChunkChange(world.getGameTime(), world.getChunk(blockPos).getPos());
            return true;
        }
        return false;
    }

    public static boolean resetSpecialCase(BlockState oldState, ServerWorld world, BlockPos pos, boolean render, int gooRenderLife, BlockSave blockSave) {
        if (oldState.getBlock() instanceof DoorBlock) {
            if (oldState.get(DoorBlock.HALF).equals(DoubleBlockHalf.LOWER)) {
                world.setBlockState(pos, oldState);
                if (world.getBlockState(pos.up()).getBlock() instanceof GooBase && render)
                    world.addEntity(new GooEntity(world, pos.up(), world.getBlockState(pos.up()), gooRenderLife, true));
                world.setBlockState(pos.up(), oldState.with(DoorBlock.HALF, DoubleBlockHalf.UPPER));
                ChunkSave.pop(pos, world.getChunk(pos).getPos());
                blockSave.addBlockChange(world.getGameTime());
                blockSave.addChunkChange(world.getGameTime(), world.getChunk(pos).getPos());
                return true;
            }
        }
        if (oldState.getBlock() instanceof BedBlock) {
            if (oldState.get(BedBlock.PART).equals(BedPart.HEAD)) {
                BlockPos additionalPos = pos.offset(oldState.get(BedBlock.HORIZONTAL_FACING).getOpposite());
                world.setBlockState(pos, oldState);
                if (world.getBlockState(additionalPos).getBlock() instanceof GooBase && render)
                    world.addEntity(new GooEntity(world, additionalPos, world.getBlockState(additionalPos), gooRenderLife, true));
                world.setBlockState(additionalPos, oldState.with(BedBlock.PART, BedPart.FOOT));
                ChunkSave.pop(pos, world.getChunk(pos).getPos());
                blockSave.addBlockChange(world.getGameTime());
                blockSave.addChunkChange(world.getGameTime(), world.getChunk(pos).getPos());
                return true;
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            if (!(worldIn.getBlockState(fromPos).getBlock() instanceof GooBase))
                isSurrounded((ServerWorld) worldIn, pos, state);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    @Deprecated
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(FROZEN) == 0) return;
        Vector3d pos2 = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        Random random = new Random();
        int sideRandom = random.nextInt(Direction.values().length);
        Direction direction = Direction.values()[sideRandom]; //Pick a side at random, this is the side the particle will spawn on
        double d0 = pos2.getX();
        double d1 = pos2.getY();
        double d2 = pos2.getZ();
        if (direction.getXOffset() != 0) { //if the chose side is X, then randomly pick the y/z positions. Move the X position to just outside the edge of the block
            d0 = pos2.getX() + ((double) direction.getXOffset() / 2 + (0.1 * direction.getXOffset()));
            d1 = pos2.getY() + random.nextDouble() - 0.5;
            d2 = pos2.getZ() + random.nextDouble() - 0.5;
        } else if (direction.getYOffset() != 0) {
            d0 = pos2.getX() + random.nextDouble() - 0.5;
            d1 = pos2.getY() + ((double) direction.getYOffset() / 2 + (0.1 * direction.getYOffset()));
            if (d1 > pos2.getY()) return; //Don't spawn particles on top of block they look funny
            d2 = pos2.getZ() + random.nextDouble() - 0.5;
        } else if (direction.getZOffset() != 0) {
            d0 = pos2.getX() + random.nextDouble() - 0.5;
            d1 = pos2.getY() + random.nextDouble() - 0.5;
            d2 = pos2.getZ() + ((double) direction.getZOffset() / 2 + (0.1 * direction.getZOffset()));
        }
        float r = rand.nextFloat();
        float b = 1f;
        float g = r + ((b - r) / 2);
        worldIn.addParticle(ModParticles.FREEZE_PARTICLE, d0, d1, d2, r, g, b);
    }
}
