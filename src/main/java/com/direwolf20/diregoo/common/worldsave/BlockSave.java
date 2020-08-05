package com.direwolf20.diregoo.common.worldsave;

import com.direwolf20.diregoo.DireGoo;
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
import java.util.Map;

public class BlockSave extends WorldSavedData {
    private static final String NAME = DireGoo.MOD_ID + "_blocksave";
    private final HashMap<BlockPos, BlockState> blockMap = new HashMap<>();

    public BlockSave() {
        super(NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        blockMap.clear();

        ListNBT list = nbt.getList("blockmap", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            BlockPos blockpos = NBTUtil.readBlockPos(list.getCompound(i).getCompound("pos"));
            BlockState state = NBTUtil.readBlockState(list.getCompound(i).getCompound("state"));

            blockMap.put(blockpos, state);
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

    public BlockState getStateFromPos(BlockPos pos) {
        return this.blockMap.get(pos);
    }
}
