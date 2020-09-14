package com.direwolf20.diregoo.common.events;

import com.direwolf20.diregoo.common.worldsave.BlockSave;
import it.unimi.dsi.fastutil.longs.Long2ShortOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ChunkSave {
    private static final HashMap<ChunkPos, Long2ShortOpenHashMap> blockMap = new HashMap<>();

    @SubscribeEvent
    public static void chunkDataLoad(final ChunkDataEvent.Load event) {
        //final World world = event.getWorld().getWorld();
        final ChunkPos chunkPos = event.getChunk().getPos();
        CompoundNBT nbt = event.getData();
        Long2ShortOpenHashMap tempMap = new Long2ShortOpenHashMap();
        ListNBT list = nbt.getList("blockmap", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            long blockpos = list.getCompound(i).getLong("pos");
            short key = list.getCompound(i).getShort("key");

            tempMap.put(blockpos, key);
        }
        blockMap.put(chunkPos, tempMap);
        /*if (tempMap.size() > 0)
            System.out.println("Read: " + tempMap.size() + " GooBlocks in " + chunkPos);*/
    }

    @SubscribeEvent
    public static void chunkDataSave(final ChunkDataEvent.Save event) {
        //final World world = event.getWorld().getWorld();
        final ChunkPos chunkPos = event.getChunk().getPos();
        ListNBT nbt = new ListNBT();
        Long2ShortOpenHashMap tempMap = getBlockMap(chunkPos);
        for (Map.Entry<Long, Short> blockData : tempMap.entrySet()) {
            CompoundNBT comp = new CompoundNBT();
            comp.putLong("pos", blockData.getKey());
            comp.putShort("key", blockData.getValue());
            nbt.add(comp);
        }
        CompoundNBT compound = event.getData();
        compound.put("blockmap", nbt);
        /*if (nbt.size() > 0)
            System.out.println("Write: " + nbt.size() + " GooBlocks in " + chunkPos);*/
    }

    @SubscribeEvent
    public static void chunkUnload(final ChunkEvent.Unload event) {
        //TODO : Figure out unload and load - we should remove data from the map on unload and bring it back
        /*if (getBlockMap(event.getChunk().getPos()).size() > 0)
            System.out.println("Unloading: " + getBlockMap(event.getChunk().getPos()).size() + " GooBlocks in " + event.getChunk().getPos());
        blockMap.remove(event.getChunk().getPos());*/
    }

    @SubscribeEvent
    public static void chunkload(final ChunkEvent.Load event) {
        //System.out.println("I am loading: " + event.getChunk().getPos());
    }

    public static Long2ShortOpenHashMap getBlockMap(ChunkPos chunkPos) {
        return blockMap.computeIfAbsent(chunkPos, $ -> new Long2ShortOpenHashMap());
    }

    public static void push(BlockPos pos, BlockState state, BlockSave blockSave, ChunkPos chunkPos) {
        short palettePosition = blockSave.getPalettePosition(state);
        if (palettePosition == -1) {
            blockSave.blockMapPalette.put((short) blockSave.blockMapPalette.values().size(), state);
            palettePosition = (short) (blockSave.blockMapPalette.values().size() - 1);
            System.out.println("Adding " + state + " to position " + palettePosition);
        }
        getBlockMap(chunkPos).put(pos.toLong(), palettePosition);
        blockSave.markDirty();
    }

    public static void pop(BlockPos pos, ChunkPos chunkPos) {
        getBlockMap(chunkPos).remove(pos.toLong());
    }

    public static BlockState getStateFromPos(BlockPos pos, BlockSave blockSave, ChunkPos chunkPos) {
        return !getBlockMap(chunkPos).containsKey(pos.toLong()) ? null : blockSave.blockMapPalette.get(getBlockMap(chunkPos).get(pos.toLong()));
    }

}
