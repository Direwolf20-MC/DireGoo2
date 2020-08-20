package com.direwolf20.diregoo.common.network.packets;

import com.direwolf20.diregoo.common.tiles.AntiGooFieldGenTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeAntiGooFieldRange {
    private final int[] ranges;
    BlockPos tePos;

    public PacketChangeAntiGooFieldRange(int[] ranges, BlockPos pos) {
        this.ranges = ranges;
        this.tePos = pos;
    }

    public static void encode(PacketChangeAntiGooFieldRange msg, PacketBuffer buffer) {
        buffer.writeVarIntArray(msg.ranges);
        buffer.writeBlockPos(msg.tePos);
    }

    public static PacketChangeAntiGooFieldRange decode(PacketBuffer buffer) {
        return new PacketChangeAntiGooFieldRange(buffer.readVarIntArray(), buffer.readBlockPos());
    }

    public static class Handler {
        public static void handle(PacketChangeAntiGooFieldRange msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                World world = ctx.get().getSender().world;
                TileEntity te = world.getTileEntity(msg.tePos);
                if (!(te instanceof AntiGooFieldGenTileEntity))
                    return;

                ((AntiGooFieldGenTileEntity) te).updateRanges(msg.ranges);

            });

            ctx.get().setPacketHandled(true);
        }
    }
}
