package com.direwolf20.diregoo.common.events;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.direwolf20.diregoo.common.network.PacketHandler;
import com.direwolf20.diregoo.common.network.packets.AntigooSync;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import com.google.common.collect.HashMultimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ServerEvents {

    @SubscribeEvent
    public static void UpdateWorldSave(BlockEvent.BreakEvent event) {
        World world = event.getPlayer().world;
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        BlockSave blockSave = BlockSave.get(world);
        if (blockSave.checkAnti(pos))
            blockSave.removeAnti(pos, world);
        if (state.getBlock() instanceof GooBase) {
            ChunkSave.pop(pos, world.getChunk(pos).getPos());
            blockSave.popTE(pos);
        }
    }

    @SubscribeEvent
    public static void SyncRenderPacket(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        World world = player.getEntityWorld();

        BlockSave blockSave = BlockSave.get(world);
        PacketHandler.sendTo(new AntigooSync(blockSave.getAntiGooList()), (ServerPlayerEntity) player);
    }

    private static Queue<ChunkPos> chunkQueue = new LinkedList<>();
    private static HashMultimap<ChunkPos, BlockPos> chunkMap = HashMultimap.create();
    private static final HashMap<BlockPos, Direction> todoList = new HashMap<>();
    private static final HashMap<BlockPos, BlockState> posState = new HashMap<>();

    private static final HashMap<BlockPos, Boolean> posClearList = new HashMap<>();
    private static ServerWorld serverWorld;

    public static int chunkQueueSize() {
        return chunkQueue.size();
    }

    public static int blockChangeSize() {
        return todoList.size();
    }

    public static int clearChangeSize() {
        return posClearList.size();
    }

    @SubscribeEvent
    public static void ServerTickHandler(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!todoList.isEmpty() && serverWorld != null) {
                if (serverWorld.getGameTime() % Config.GOO_SPREAD_FREQUENCY.get() != 0)
                    return; //Only run every X ticks (Config)
                BlockSave blockSave = BlockSave.get(serverWorld);
                int maxRuns = Config.MAX_CHUNK_CHANGES.get(); //Get how many chunks to process via config
                int iterations = Math.min(chunkQueue.size(), maxRuns); //If we have less than the max chunks to process
                //System.out.println("Processing Chunks: " + iterations + " out of " + chunkQueue.size());
                for (int i = 0; i < iterations; i++) {
                    ChunkPos processChunk = chunkQueue.remove(); //Remove the oldest chunk from the queue
                    Set<BlockPos> posList = chunkMap.get(processChunk); //Get all the blocks in that chunk that are due to be processed
                    //System.out.println("Blocks in iteration " + i + ": " + posList.size());
                    for (BlockPos checkPos : posList) { //For each blockPos in the list
                        BlockState oldState = serverWorld.getBlockState(checkPos); //note the blockstate of the block to be eaten
                        if (oldState.getBlock() instanceof GooBase) {
                            int frozenState = oldState.get(GooBase.FROZEN);
                            if (frozenState > 0) {
                                frozenState--;
                                serverWorld.setBlockState(checkPos, oldState.with(GooBase.FROZEN, frozenState));
                            }
                        } else {
                            Direction direction = todoList.get(checkPos); //Get the position that this goo spread from
                            //Spread the goo, use an entity if animating. First check to make sure the goo block this spread from is still there...
                            BlockState sourceGoo = serverWorld.getBlockState(checkPos.offset(direction.getOpposite()));
                            Block maybeGoo = sourceGoo.getBlock();
                            boolean canSpreadHere = false;
                            if (maybeGoo instanceof GooBase) {
                                canSpreadHere = ((GooBase) maybeGoo).canSpreadHere(checkPos, oldState, serverWorld, blockSave);
                            }
                            if (sourceGoo.equals(posState.get(checkPos)) && canSpreadHere) {
                                if (GooBase.shouldAnimateSpread(serverWorld, checkPos)) {
                                    serverWorld.addEntity(new GooSpreadEntity(serverWorld, checkPos, posState.get(checkPos), GooBase.gooSpreadAnimationTime, direction.getOpposite().getIndex()));
                                    GooBase.forceExtraTick(serverWorld, checkPos, true);
                                } else {
                                    GooBase.saveBlockData(serverWorld, checkPos, oldState, blockSave);
                                    serverWorld.setBlockState(checkPos, posState.get(checkPos));
                                    GooBase.forceExtraTick(serverWorld, checkPos, false);
                                }
                            }
                        }
                        //Remove this blockpos from all the lists
                        todoList.remove(checkPos);
                        posState.remove(checkPos);
                        //posAnimate.remove(checkPos);
                    }
                    chunkMap.removeAll(processChunk); //Remove the ChunkPos from the chunkMap
                }
            }
            if (!posClearList.isEmpty() && serverWorld != null) {
                BlockSave blockSave = BlockSave.get(serverWorld);
                int multiplier = blockSave.countGooDeathMarkers() > 0 ? blockSave.countGooDeathMarkers() : 1;
                if (serverWorld.getGameTime() % (Config.GOO_SPREAD_FREQUENCY.get() / multiplier) != 0)
                    return; //Only run every X ticks
                int maxRuns = Config.MAX_CHUNK_CHANGES.get();
                int iterations = Math.min(chunkQueue.size(), maxRuns);
                //System.out.println("Processing Chunks: " + iterations + " out of " + chunkQueue.size());
                for (int i = 0; i < iterations; i++) {
                    ChunkPos processChunk = chunkQueue.remove();
                    Set<BlockPos> posList = chunkMap.get(processChunk);
                    //System.out.println("Blocks in iteration " + i + ": " + posList.size());
                    for (BlockPos checkPos : posList) {
                        GooBase.resetBlock(serverWorld, checkPos, posClearList.get(checkPos), 20, false, blockSave);
                        posClearList.remove(checkPos);
                    }
                    chunkMap.removeAll(processChunk);
                }
            }

        }
    }

    public static void addToList(BlockPos pos, Direction direction, ServerWorld world, BlockState gooState) {
        //Used during gooSpread
        if (!todoList.containsKey(pos)) { //Make sure this position isn't already queue'd
            if (chunkQueue.size() < Config.MAX_CHUNK_QUEUE.get()) { //Make sure we haven't reached the max queue'd chunks via config
                ChunkPos chunkPos = world.getChunk(pos).getPos(); //Check the chunk pos based on the blockpos
                if (!chunkQueue.contains(chunkPos)) //Add the chunkPos to the queue if its not there already.
                    chunkQueue.add(chunkPos);
                if (chunkMap.get(chunkPos).size() >= Config.MAX_BLOCK_CHANGES.get())
                    return; //Make sure we haven't reach the max changes in this chunk.
                chunkMap.put(chunkPos, pos);  //Add to the chunkMap
                todoList.put(pos, direction); //Add to the spreading list
                posState.put(pos, gooState); //Note the blockstate of goo that caused this spread - Normal/Terrain/Burst/etc
                //posAnimate.put(pos, animate); //Gonna remove this soon
            }
        }
        serverWorld = world;
    }

    public static void addToClearList(BlockPos pos, ServerWorld world, boolean animate) {
        //Used during goo Death event
        if (!posClearList.containsKey(pos)) { //Make sure this position isn't already queue'd
            if (chunkQueue.size() < Config.MAX_CHUNK_QUEUE.get()) { //Make sure we haven't reached the max queue'd chunks via config
                ChunkPos chunkPos = world.getChunk(pos).getPos(); //Check the chunk pos based on the blockpos
                if (!chunkQueue.contains(chunkPos)) //Add the chunkPos to the queue if its not there already.
                    chunkQueue.add(chunkPos);
                if (chunkMap.get(chunkPos).size() >= Config.MAX_BLOCK_CHANGES.get())
                    return; //Make sure we haven't reach the max changes in this chunk.
                chunkMap.put(chunkPos, pos); //Add to the chunkMap
                posClearList.put(pos, animate); //Add to the clearing list
            }
        }
        serverWorld = world;
    }

    public static void clearAllLists() {
        //Used when we turn off goo spread, or enable goo death event.
        chunkQueue.clear();
        chunkMap.clear();
        todoList.clear();
        posState.clear();
        //posAnimate.clear();
        posClearList.clear();
    }
}
