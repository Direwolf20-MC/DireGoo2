package com.direwolf20.diregoo.common.network.packets;

import com.direwolf20.diregoo.client.renderer.AntiGooRender;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class AntigooSync {
    private final ArrayList<BlockPos> antigooList;
    //private final int priorDurability;

    public AntigooSync(ArrayList<BlockPos> updateList) {
        this.antigooList = updateList;
    }

    public static void encode(AntigooSync msg, PacketBuffer buffer) {
        ArrayList<BlockPos> thisList = msg.antigooList;
        CompoundNBT tag = new CompoundNBT();
        ListNBT nbtList = new ListNBT();
        for (BlockPos pos : thisList) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("pos", NBTUtil.writeBlockPos(pos));
            nbtList.add(nbt);
        }
        tag.put("list", nbtList);
        buffer.writeCompoundTag(tag);
    }

    public static AntigooSync decode(PacketBuffer buffer) {
        CompoundNBT tag = buffer.readCompoundTag();
        ListNBT nbtList = tag.getList("list", Constants.NBT.TAG_COMPOUND);
        ArrayList<BlockPos> antigooList = new ArrayList<>();
        for (int i = 0; i < nbtList.size(); i++) {
            BlockPos blockPos = NBTUtil.readBlockPos(nbtList.getCompound(i).getCompound("pos"));
            antigooList.add(blockPos);
        }
        return new AntigooSync(antigooList);
    }

    public static class Handler {
        public static void handle(AntigooSync msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> clientPacketHandler(msg)));
            ctx.get().setPacketHandled(true);
        }
    }

    public static void clientPacketHandler(AntigooSync msg) {
        AntiGooRender.updateGooList(msg.antigooList);
    }
}
