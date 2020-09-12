package com.direwolf20.diregoo.common.events;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.direwolf20.diregoo.common.network.PacketHandler;
import com.direwolf20.diregoo.common.network.packets.AntigooSync;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
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
import java.util.HashSet;
import java.util.Map;
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
    
    private static final HashMap<BlockPos, Direction> todoList = new HashMap<>();
    private static final HashMap<BlockPos, BlockState> posState = new HashMap<>();
    private static final HashMap<BlockPos, Boolean> posAnimate = new HashMap<>();
    private static final HashMap<BlockPos, Boolean> posClearList = new HashMap<>();
    private static ServerWorld serverWorld;
    private static Set<ChunkPos> chunkCount = new HashSet<>();

    @SubscribeEvent
    public static void ServerTickHandler(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!todoList.isEmpty() && serverWorld != null) {
                if (serverWorld.getGameTime() % 20 == 0) {
                    System.out.println("Chunk Count: " + chunkCount.size());
                    System.out.println("Processing Goo: " + todoList.size());
                    BlockSave blockSave = BlockSave.get(serverWorld);
                    for (Map.Entry<BlockPos, Direction> entry : todoList.entrySet()) {
                        BlockPos checkPos = entry.getKey();
                        BlockState oldState = serverWorld.getBlockState(checkPos);
                        if (posAnimate.get(checkPos)) {
                            serverWorld.addEntity(new GooSpreadEntity(serverWorld, checkPos, posState.get(checkPos), 20, entry.getValue().getOpposite().getIndex()));
                        } else {
                            GooBase.saveBlockData(serverWorld, checkPos, oldState, blockSave);
                            serverWorld.setBlockState(checkPos, posState.get(checkPos));
                        }
                    }
                    todoList.clear();
                    chunkCount.clear();
                    posState.clear();
                    posAnimate.clear();
                }
            }
            if (!posClearList.isEmpty() && serverWorld != null) {
                if (serverWorld.getGameTime() % 20 == 0) {
                    System.out.println("Clearing Chunk Count: " + chunkCount.size());
                    System.out.println("Clearing Goo: " + posClearList.size());
                    BlockSave blockSave = BlockSave.get(serverWorld);
                    for (Map.Entry<BlockPos, Boolean> entry : posClearList.entrySet()) {
                        GooBase.resetBlock(serverWorld, entry.getKey(), entry.getValue(), 20, false, blockSave);
                    }
                    posClearList.clear();
                    chunkCount.clear();
                }
            }

        }
    }

    public static void addToList(BlockPos pos, Direction direction, ServerWorld world, BlockState gooState, boolean animate) {
        if (!todoList.containsKey(pos) && todoList.size() <= Config.MAX_BLOCK_CHANGES.get() * 20) {
            ChunkPos chunkPos = world.getChunk(pos).getPos();
            if (chunkCount.size() < Config.MAX_CHUNK_CHANGES.get() * 4) {
                chunkCount.add(chunkPos);
                todoList.put(pos, direction);
                posState.put(pos, gooState);
                posAnimate.put(pos, animate);
            }
        }
        serverWorld = world;
    }

    public static void addToClearList(BlockPos pos, ServerWorld world, boolean animate) {
        if (!posClearList.containsKey(pos) && posClearList.size() <= Config.MAX_BLOCK_CHANGES.get() * 20) {
            ChunkPos chunkPos = world.getChunk(pos).getPos();
            if (chunkCount.size() < Config.MAX_CHUNK_CHANGES.get() * 4) {
                chunkCount.add(chunkPos);
                posClearList.put(pos, animate);
            }
        }
        serverWorld = world;
    }
}
