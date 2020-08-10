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
    private final HashMap<BlockPos, BlockState> blockMap = new HashMap<>();
    private final HashMap<BlockPos, CompoundNBT> teMap = new HashMap<>();
    private final Set<BlockPos> antigooList = new HashSet<>();


    public BlockSave() {
        super(NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        blockMap.clear();
        teMap.clear();
        antigooList.clear();

        ListNBT list = nbt.getList("blockmap", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            BlockPos blockpos = NBTUtil.readBlockPos(list.getCompound(i).getCompound("pos"));
            BlockState state = NBTUtil.readBlockState(list.getCompound(i).getCompound("state"));

            blockMap.put(blockpos, state);
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
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT nbt = new ListNBT();

        for (Map.Entry<BlockPos, BlockState> blockData : blockMap.entrySet()) {
            CompoundNBT comp = new CompoundNBT();
            comp.put("pos", NBTUtil.writeBlockPos(blockData.getKey()));
            comp.put("state", NBTUtil.writeBlockState(blockData.getValue()));
            nbt.add(comp);
        }
        compound.put("blockmap", nbt);

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

    public HashMap<BlockPos, BlockState> getBlockMap() {
        return blockMap;
    }

    public void push(BlockPos pos, BlockState state) {
        this.blockMap.put(pos, state);
        this.markDirty();
    }

    public void pop(BlockPos pos) {
        this.blockMap.remove(pos);
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
        return success;
    }

    public boolean removeAnti(BlockPos pos, World world) {
        boolean success = this.antigooList.remove(pos);
        if (success)
            PacketHandler.sendToAll(new AntigooSync(this.antigooList), world);
        return success;
    }

    public boolean checkAnti(BlockPos pos) {
        return this.antigooList.contains(pos);
    }

    public Set<BlockPos> getAntiGooList() {
        return antigooList;
    }

    public BlockState getStateFromPos(BlockPos pos) {
        return this.blockMap.get(pos);
    }

    public CompoundNBT getTEFromPos(BlockPos pos) {
        return this.teMap.get(pos);
    }
}
