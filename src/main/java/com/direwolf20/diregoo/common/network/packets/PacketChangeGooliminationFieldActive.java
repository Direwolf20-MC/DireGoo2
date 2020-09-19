package com.direwolf20.diregoo.common.network.packets;

import com.direwolf20.diregoo.common.tiles.GooliminationFieldGenTile;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeGooliminationFieldActive {
    private final boolean active;
    BlockPos tePos;

    public PacketChangeGooliminationFieldActive(boolean active, BlockPos pos) {
        this.active = active;
        this.tePos = pos;
    }

    public static void encode(PacketChangeGooliminationFieldActive msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.active);
        buffer.writeBlockPos(msg.tePos);
    }

    public static PacketChangeGooliminationFieldActive decode(PacketBuffer buffer) {
        return new PacketChangeGooliminationFieldActive(buffer.readBoolean(), buffer.readBlockPos());
    }

    public static class Handler {
        public static void handle(PacketChangeGooliminationFieldActive msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                World world = ctx.get().getSender().world;
                TileEntity te = world.getTileEntity(msg.tePos);
                if (!(te instanceof GooliminationFieldGenTile))
                    return;

                if (msg.active)
                    ((GooliminationFieldGenTile) te).activate((ServerWorld) world);
                else
                    ((GooliminationFieldGenTile) te).deactivate((ServerWorld) world);

            });

            ctx.get().setPacketHandled(true);
        }
    }
}
