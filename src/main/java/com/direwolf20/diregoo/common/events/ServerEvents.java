package com.direwolf20.diregoo.common.events;

import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.network.PacketHandler;
import com.direwolf20.diregoo.common.network.packets.AntigooSync;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEvents {

    @SubscribeEvent
    public static void UpdateWorldSave(BlockEvent.BreakEvent event) {
        World world = event.getWorld().getWorld();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        BlockSave blockSave = BlockSave.get(world);
        if (blockSave.checkAnti(pos))
            blockSave.removeAnti(pos, world);
        if (state.getBlock() instanceof GooBase) {
            blockSave.pop(pos);
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
}
