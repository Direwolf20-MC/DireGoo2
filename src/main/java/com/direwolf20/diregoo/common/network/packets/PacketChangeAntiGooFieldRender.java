package com.direwolf20.diregoo.common.network.packets;

import com.direwolf20.diregoo.common.tiles.AntiGooFieldGenTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeAntiGooFieldRender {
    private final boolean active;
    BlockPos tePos;

    public PacketChangeAntiGooFieldRender(boolean active, BlockPos pos) {
        this.active = active;
        this.tePos = pos;
    }

    public static void encode(PacketChangeAntiGooFieldRender msg, PacketBuffer buffer) {
        buffer.writeBoolean(msg.active);
        buffer.writeBlockPos(msg.tePos);
    }

    public static PacketChangeAntiGooFieldRender decode(PacketBuffer buffer) {
        return new PacketChangeAntiGooFieldRender(buffer.readBoolean(), buffer.readBlockPos());
    }

    public static class Handler {
        public static void handle(PacketChangeAntiGooFieldRender msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                World world = ctx.get().getSender().world;
                TileEntity te = world.getTileEntity(msg.tePos);
                if (!(te instanceof AntiGooFieldGenTileEntity))
                    return;

                ((AntiGooFieldGenTileEntity) te).setRenderArea(msg.active);

            });

            ctx.get().setPacketHandled(true);
        }
    }
}
