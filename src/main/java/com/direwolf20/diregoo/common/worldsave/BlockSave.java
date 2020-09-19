package com.direwolf20.diregoo.common.worldsave;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.events.ServerEvents;
import com.direwolf20.diregoo.common.network.PacketHandler;
import com.direwolf20.diregoo.common.network.packets.AntigooSync;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockSave extends WorldSavedData {
    private static final String NAME = DireGoo.MOD_ID + "_blocksave";
    //private final HashMap<Long, Short> blockMap = new HashMap<>();
    public final HashMap<Short, BlockState> blockMapPalette = new HashMap<>();
    private final HashMap<BlockPos, CompoundNBT> teMap = new HashMap<>();
    private final ArrayList<BlockPos> antigooList = new ArrayList<>();
    private final SetMultimap<BlockPos, BlockPos> antigooFieldList = HashMultimap.create();
    private boolean gooDeathEvent = false;
    /*private final LinkedHashMap<Long, Integer> blockChangeCounter = new LinkedHashMap<Long, Integer>() {
        protected boolean removeEldestEntry(Map.Entry<Long, Integer> eldest) {
            return size() > 10;
        }
    };
    private final LinkedHashMap<Long, Set<ChunkPos>> chunkChangeCounter = new LinkedHashMap<Long, Set<ChunkPos>>() {
        protected boolean removeEldestEntry(Map.Entry<Long, Set<ChunkPos>> eldest) {
            return size() > 10;
        }
    };*/

    public BlockSave() {
        super(NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        long startTime = System.nanoTime();
        //blockMap.clear();
        blockMapPalette.clear();
        teMap.clear();
        antigooList.clear();

        /*ListNBT list = nbt.getList("blockmap", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            Long blockpos = list.getCompound(i).getLong("pos");
            short key = list.getCompound(i).getShort("key");

            blockMap.put(blockpos, key);
        }
        System.out.println("Read: " + list.size() + "GooBlocks");*/

        ListNBT listPalette = nbt.getList("blockmappalette", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listPalette.size(); i++) {
            short key = listPalette.getCompound(i).getShort("key");
            BlockState blockState = NBTUtil.readBlockState(listPalette.getCompound(i).getCompound("state"));

            blockMapPalette.put(key, blockState);
        }

        ListNBT telist = nbt.getList("temap", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < telist.size(); i++) {
            BlockPos blockpos = NBTUtil.readBlockPos(telist.getCompound(i).getCompound("pos"));
            CompoundNBT teData = telist.getCompound(i).getCompound("teData");

            teMap.put(blockpos, teData);
        }

        ListNBT antigoo = nbt.getList("antigoo", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < antigoo.size(); i++) {
            BlockPos blockPos = NBTUtil.readBlockPos(antigoo.getCompound(i).getCompound("pos"));
            antigooList.add(blockPos);
        }

        ListNBT antigooField = nbt.getList("antigoofield", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < antigooField.size(); i++) {
            BlockPos sourcePos = NBTUtil.readBlockPos(antigooField.getCompound(i).getCompound("sourcePos"));
            BlockPos protectedPos = NBTUtil.readBlockPos(antigooField.getCompound(i).getCompound("protectedPos"));
            antigooFieldList.put(sourcePos, protectedPos);
        }

        gooDeathEvent = nbt.getBoolean("gooDeathEvent");
        long elapsedTime = System.nanoTime() - startTime;
        //System.out.println("Elapsed time for Read = " + elapsedTime / 1000000);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        long startTime = System.nanoTime();

        /*ListNBT nbt = new ListNBT();
        for (Map.Entry<Long, Short> blockData : blockMap.entrySet()) {
            CompoundNBT comp = new CompoundNBT();
            comp.putLong("pos", blockData.getKey());
            comp.putShort("key", blockData.getValue());
            nbt.add(comp);
        }
        compound.put("blockmap", nbt);
        System.out.println("Write: " + nbt.size() + " GooBlocks");
        */

        ListNBT nbtPalette = new ListNBT();
        for (Map.Entry<Short, BlockState> blockData : blockMapPalette.entrySet()) {
            CompoundNBT comp = new CompoundNBT();
            comp.putShort("key", blockData.getKey());
            comp.put("state", NBTUtil.writeBlockState(blockData.getValue()));
            nbtPalette.add(comp);
        }
        compound.put("blockmappalette", nbtPalette);

        ListNBT nbtTE = new ListNBT();

        for (Map.Entry<BlockPos, CompoundNBT> teData : teMap.entrySet()) {
            CompoundNBT comp = new CompoundNBT();
            comp.put("pos", NBTUtil.writeBlockPos(teData.getKey()));
            comp.put("teData", teData.getValue());
            nbtTE.add(comp);
        }
        compound.put("temap", nbtTE);

        ListNBT anti = new ListNBT();
        for (BlockPos blockPos : antigooList) {
            CompoundNBT comp = new CompoundNBT();
            comp.put("pos", NBTUtil.writeBlockPos(blockPos));
            anti.add(comp);
        }
        compound.put("antigoo", anti);

        ListNBT antiField = new ListNBT();
        for (Map.Entry<BlockPos, BlockPos> blockData : antigooFieldList.entries()) {
            CompoundNBT comp = new CompoundNBT();
            comp.put("sourcePos", NBTUtil.writeBlockPos(blockData.getKey()));
            comp.put("protectedPos", NBTUtil.writeBlockPos(blockData.getValue()));
            antiField.add(comp);
        }
        compound.put("antigoofield", antiField);

        compound.putBoolean("gooDeathEvent", gooDeathEvent);
        long elapsedTime = System.nanoTime() - startTime;
        //System.out.println("Elapsed time for Write = " + elapsedTime / 1000000);
        return compound;

    }

    public static BlockSave get(World world) {
        BlockSave storage = ((ServerWorld) world).getSavedData().get(BlockSave::new, NAME);

        if (storage == null) {
            storage = new BlockSave();
            storage.markDirty();
            ((ServerWorld) world).getSavedData().set(storage);
        }

        return storage;
    }

    /*public HashMap<Long, Short> getBlockMap() {
        return blockMap;
    }*/

    public HashMap<Short, BlockState> getBlockMapPalette() {
        return blockMapPalette;
    }

    public short getPalettePosition(BlockState state) {
        for (Map.Entry<Short, BlockState> entry : blockMapPalette.entrySet()) {
            if (state.equals(entry.getValue()))
                return entry.getKey();
        }
        return -1;
    }

    /*public void push(BlockPos pos, BlockState state) {
        short palettePosition = getPalettePosition(state);
        if (palettePosition == -1) {
            blockMapPalette.put((short) blockMapPalette.values().size(), state);
            palettePosition = (short) (blockMapPalette.values().size() - 1);
        }
        this.blockMap.put(pos.toLong(), palettePosition);
        this.markDirty();
    }

    public void pop(BlockPos pos) {
        this.blockMap.remove(pos.toLong());
        this.markDirty();
    }*/

    public void pushTE(BlockPos pos, CompoundNBT nbt) {
        this.teMap.put(pos, nbt);
        this.markDirty();
    }

    public void popTE(BlockPos pos) {
        this.teMap.remove(pos);
        this.markDirty();
    }

    public boolean addAnti(BlockPos pos, World world) {
        if (this.antigooList.contains(pos)) return false;
        boolean success = this.antigooList.add(pos);
        if (success)
            PacketHandler.sendToAll(new AntigooSync(this.antigooList), world);
        this.markDirty();
        return success;
    }

    public boolean removeAnti(BlockPos pos, World world) {
        boolean success = this.antigooList.remove(pos);
        if (success)
            PacketHandler.sendToAll(new AntigooSync(this.antigooList), world);
        this.markDirty();
        return success;
    }

    public boolean addAntiField(BlockPos sourcePos, Set<BlockPos> posList, World world) {
        boolean success = antigooFieldList.putAll(sourcePos, posList);
        if (success) {
            PacketHandler.sendToAll(new AntigooSync(this.antigooList), world);
            this.markDirty();
        }
        return success;
    }

    public boolean removeAntiField(BlockPos sourcePos, Set<BlockPos> posList, World world) {
        boolean success = false;
        for (BlockPos pos : posList) {
            if (this.antigooFieldList.remove(sourcePos, pos)) ;
            success = true;
        }
        if (success) {
            PacketHandler.sendToAll(new AntigooSync(this.antigooList), world);
            this.markDirty();
        }
        return success;
    }

    public boolean checkAnti(BlockPos pos) {
        return (this.antigooList.contains(pos) || this.antigooFieldList.containsValue(pos));
    }

    public ArrayList<BlockPos> getAntiGooList() {
        return antigooList;
    }

    /*public BlockState getStateFromPos(BlockPos pos) {
        return this.blockMapPalette.get(this.blockMap.get(pos.toLong()));
    }*/

    public CompoundNBT getTEFromPos(BlockPos pos) {
        return this.teMap.get(pos);
    }

    public boolean getGooDeathEvent() {
        return gooDeathEvent;
    }

    public void setGooDeathEvent(boolean gooDeathEvent) {
        this.gooDeathEvent = gooDeathEvent;
        ServerEvents.clearAllLists();
        this.markDirty();
    }

    /*public void addBlockChange(long gametime) {
        blockChangeCounter.compute(gametime, (k, v) -> (v == null) ? 1 : v + 1);
    }

    public int getBlockChangeThisTick(long gametime) {
        return blockChangeCounter.getOrDefault(gametime, 0);
    }

    public LinkedHashMap<Long, Integer> getBlockChangeCounter() {
        return blockChangeCounter;
    }

    public void addChunkChange(long gametime, ChunkPos chunkPos) {
        if (chunkChangeCounter.containsKey(gametime)) {
            Set<ChunkPos> tempSet = chunkChangeCounter.get(gametime);
            tempSet.add(chunkPos);
            chunkChangeCounter.replace(gametime, tempSet);
        } else {
            Set<ChunkPos> tempSet = new HashSet<>();
            tempSet.add(chunkPos);
            chunkChangeCounter.put(gametime, tempSet);
        }
    }

    public int getChunkChangesThisTick(long gametime) {
        return chunkChangeCounter.getOrDefault(gametime, new HashSet<ChunkPos>()).size();
    }

    public LinkedHashMap<Long, Set<ChunkPos>> getChunkChangeCounter() {
        return chunkChangeCounter;
    }*/

}
