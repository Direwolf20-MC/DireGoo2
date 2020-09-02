package com.direwolf20.diregoo.client.events;

import com.direwolf20.diregoo.client.renderer.AntiGooRender;
import com.direwolf20.diregoo.client.renderer.GooScannerRender;
import com.direwolf20.diregoo.client.renderer.RenderZapperLaser;
import com.direwolf20.diregoo.common.items.AntigooPaste;
import com.direwolf20.diregoo.common.items.GooScanner;
import com.direwolf20.diregoo.common.items.GooZapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ClientEvents {
    @SubscribeEvent
    static void renderWorldLastEvent(RenderWorldLastEvent evt) {
        PlayerEntity myplayer = Minecraft.getInstance().player;

        if (myplayer.getHeldItem(Hand.MAIN_HAND).getItem() instanceof AntigooPaste || myplayer.getHeldItem(Hand.OFF_HAND).getItem() instanceof AntigooPaste)
            AntiGooRender.renderAntiGoo(evt);
        if (myplayer.getHeldItem(Hand.MAIN_HAND).getItem() instanceof GooScanner || myplayer.getHeldItem(Hand.OFF_HAND).getItem() instanceof GooScanner)
            GooScannerRender.renderGoo(evt);

        List<AbstractClientPlayerEntity> players = Minecraft.getInstance().world.getPlayers();
        for (PlayerEntity player : players) {
            if (player.getDistanceSq(myplayer) > 500)
                continue;

            ItemStack heldItem = getZapper(player);
            if (player.isHandActive() && heldItem.getItem() instanceof GooZapper) {
                RenderZapperLaser.renderLaser(evt, player, Minecraft.getInstance().getRenderPartialTicks());
            }
        }
    }

    public static ItemStack getZapper(PlayerEntity player) {
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!(heldItem.getItem() instanceof GooZapper)) {
            heldItem = player.getHeldItemOffhand();
            if (!(heldItem.getItem() instanceof GooZapper)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }
}
