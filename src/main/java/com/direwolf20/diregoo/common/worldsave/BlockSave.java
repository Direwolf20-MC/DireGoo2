package com.direwolf20.diregoo.common.worldsave;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.network.PacketHandler;
import com.direwolf20.diregoo.common.network.packets.AntigooSync;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockSave extends WorldSavedData {
    private static final String NAME = DireGoo.MOD_ID + "_blocksave";
    private final HashMap<Long, Short> blockMap = new HashMap<>();
    private final HashMap<Short, BlockState> blockMapPalette = new HashMap<>();
    private final HashMap<BlockPos, CompoundNBT> teMap = new HashMap<>();
    private final Set<BlockPos> antigooList = new HashSet<>();


    public BlockSave() {
        super(NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        long startTime = System.nanoTime();
        blockMap.clear();
        blockMapPalette.clear();
        teMap.clear();
        antigooList.clear();

        ListNBT list = nbt.getList("blockmap", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            Long blockpos = list.getCompound(i).getLong("pos");
            short key = list.getCompound(i).getShort("key");

            blockMap.put(blockpos, key);
        }
        System.out.println("Read: " + list.size() + "GooBlocks");

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
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed time for Read = " + elapsedTime / 1000000);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        long startTime = System.nanoTime();
        ListNBT nbt = new ListNBT();

        for (Map.Entry<Long, Short> blockData : blockMap.entrySet()) {
            CompoundNBT comp = new CompoundNBT();
            comp.putLong("pos", blockData.getKey());
            comp.putShort("key", blockData.getValue());
            nbt.add(comp);
        }
        compound.put("blockmap", nbt);

        ListNBT nbtPalette = new ListNBT();
        for (Map.Entry<Short, BlockState> blockData : blockMapPalette.entrySet()) {
            CompoundNBT comp = new CompoundNBT();
            comp.putShort("key", blockData.getKey());
            comp.put("state", NBTUtil.writeBlockState(blockData.getValue()));
            nbtPalette.add(comp);
        }
        compound.put("blockmappalette", nbtPalette);

        System.out.println("Write: " + nbt.size() + " GooBlocks");
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
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed time for Write = " + elapsedTime / 1000000);
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

    public HashMap<Long, Short> getBlockMap() {
        return blockMap;
    }

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

    public void push(BlockPos pos, BlockState state) {
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
    }

    public void pushTE(BlockPos pos, CompoundNBT nbt) {
        this.teMap.put(pos, nbt);
        this.markDirty();
    }

    public void popTE(BlockPos pos) {
        this.teMap.remove(pos);
        this.markDirty();
    }

    public boolean addAnti(BlockPos pos, World world) {
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

    public boolean checkAnti(BlockPos pos) {
        return this.antigooList.contains(pos);
    }

    public Set<BlockPos> getAntiGooList() {
        return antigooList;
    }

    public BlockState getStateFromPos(BlockPos pos) {
        return this.blockMapPalette.get(this.blockMap.get(pos.toLong()));
    }

    public CompoundNBT getTEFromPos(BlockPos pos) {
        return this.teMap.get(pos);
    }
}
