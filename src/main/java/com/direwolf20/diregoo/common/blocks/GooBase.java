package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.client.particles.ModParticles;
import com.direwolf20.diregoo.common.data.GeneratorBlockTags;
import com.direwolf20.diregoo.common.entities.GooEntity;
import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.direwolf20.diregoo.common.events.ChunkSave;
import com.direwolf20.diregoo.common.events.ServerEvents;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
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
        BlockState oldState = ChunkSave.getStateFromPos(pos, blockSave, world.getChunk(pos).getPos());
        if (render)
            world.addEntity(new GooEntity(world, pos, world.getBlockState(pos), gooRenderLife, calcSideRender));
        if (oldState == null) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        if (oldState.isValidPosition(world, pos)) //Make sure the block can go there, example redstone repeaters need a solid block under them, snow, etc.
            world.setBlockState(pos, oldState);
        else { //If not, drop that block as an item entity.
            List<ItemStack> drops = Block.getDrops(oldState, world, pos, null);
            for (ItemStack drop : drops) {
                if (drop != null) {
                    Block.spawnAsEntity(world, pos, drop);
                }
            }
            System.out.println("Invalid Position for " + oldState + " Dropping Items: " + drops);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        ChunkSave.pop(pos, world.getChunk(pos).getPos());
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
            if (frozenState > 0)
                worldIn.setBlockState(pos, state.with(FROZEN, frozenState), 1);
            else
                ServerEvents.addToList(pos, Direction.UP, (ServerWorld) worldIn, state);
            //worldIn.setBlockState(pos, state.with(FROZEN, frozenState));
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
        if (blockSave.getGooDeathEvent()) {
            boolean animate = worldIn.isPlayerWithin(pos.getX(), pos.getY(), pos.getZ(), 25);
            if (Config.BATCH_GOO_SPREAD.get()) {
                ServerEvents.addToClearList(pos, worldIn, animate);
            } else {
                resetBlock(worldIn, pos, animate, 20, false, blockSave);
            }
            return;
        }
        if (handleFrozen(pos, state, worldIn, rand)) return;
        if (!shouldGooSpread(state, worldIn, pos, rand))
            return;
        spreadGoo(state, worldIn, pos, rand, blockSave);
        //forceExtraTick(worldIn, gooPos);
    }

    public static boolean shouldAnimateSpread(World worldIn, BlockPos pos) {
        boolean animate = false;
        if (Config.ANIMATE_SPREAD.get())
            animate = worldIn.isPlayerWithin(pos.getX(), pos.getY(), pos.getZ(), 40);
        return animate;
    }

    public static void forceExtraTick(ServerWorld world, BlockPos pos, boolean animate) {
        if (pos != BlockPos.ZERO)
            if (Config.SPREAD_TICK_DELAY.get() != -1) {
                if (animate)
                    world.getPendingBlockTicks().scheduleTick(pos, ModBlocks.GOO_BLOCK.get(), gooSpreadAnimationTime + Config.SPREAD_TICK_DELAY.get());
                else
                    world.getPendingBlockTicks().scheduleTick(pos, ModBlocks.GOO_BLOCK.get(), Config.SPREAD_TICK_DELAY.get());
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
                    worldIn.setBlockState(pos, state.with(ACTIVE, true), 1); //Using flag 1 here prevents the client from doing any re-rendering, HUGE FPS gains.
                return false; //If the adjacent block is anything other than goo its not surrounded
            }
        }
        if (state.get(ACTIVE) && state.get(FROZEN) == 0)
            worldIn.setBlockState(pos, state.with(ACTIVE, false), 1);
        return true;
    }

    public boolean handlePoison(BlockState oldState, ServerWorld worldIn, BlockPos fromPos, BlockPos checkPos, BlockSave blockSave) {
        if (oldState.getBlock().equals(ModBlocks.GOO_BLOCK_POISON.get())) {
            int newGeneration = (oldState.get(GooBlockPoison.GENERATION) == 5) ? 5 : oldState.get(GooBlockPoison.GENERATION) + 1;
            worldIn.setBlockState(fromPos, ModBlocks.GOO_BLOCK_POISON.get().getDefaultState().with(GooBlockPoison.GENERATION, newGeneration));
            worldIn.getPendingBlockTicks().scheduleTick(fromPos, ModBlocks.GOO_BLOCK_POISON.get(), 5);
            resetBlock(worldIn, checkPos, true, 80, false, blockSave);
            return true;
        }
        return false;
    }

    public BlockPos spreadGoo(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand, BlockSave blockSave) {
        int x = rand.nextInt(Direction.values().length);
        Direction direction = Direction.values()[x];

        BlockPos checkPos = pos.offset(direction);
        BlockState oldState = worldIn.getBlockState(checkPos);

        if (handlePoison(oldState, worldIn, pos, checkPos, blockSave)) {
            return BlockPos.ZERO;
        }

        if (canSpreadHere(checkPos, oldState, worldIn, blockSave)) {
            List<GooSpreadEntity> list = worldIn.getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(checkPos.getX(), checkPos.getY(), checkPos.getZ(), checkPos.getX() + 0.25d, checkPos.getY() + 0.25d, checkPos.getZ() + 0.25d));
            if (!list.isEmpty())
                return BlockPos.ZERO;
            setBlockToGoo(oldState, worldIn, checkPos, direction, blockSave);
        } else {
            return BlockPos.ZERO;
        }
        return checkPos;
    }

    public void setBlockToGoo(BlockState oldState, World worldIn, BlockPos checkPos, Direction direction, BlockSave blockSave) {
        if (Config.BATCH_GOO_SPREAD.get()) {
            ServerEvents.addToList(checkPos, direction, (ServerWorld) worldIn, this.getDefaultState());
        } else {
            if (shouldAnimateSpread(worldIn, checkPos)) {
                worldIn.addEntity(new GooSpreadEntity(worldIn, checkPos, this.getDefaultState(), gooSpreadAnimationTime, direction.getOpposite().getIndex()));
            } else {
                saveBlockData(worldIn, checkPos, oldState, blockSave);
                worldIn.setBlockState(checkPos, this.getDefaultState());
            }
        }
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
