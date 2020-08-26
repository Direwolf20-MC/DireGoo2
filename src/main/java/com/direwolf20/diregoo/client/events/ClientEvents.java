package com.direwolf20.diregoo.client.events;

import com.direwolf20.diregoo.client.renderer.AntiGooRender;
import com.direwolf20.diregoo.client.renderer.GooScannerRender;
import com.direwolf20.diregoo.common.items.AntigooPaste;
import com.direwolf20.diregoo.common.items.GooScanner;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {
    @SubscribeEvent
    static void renderWorldLastEvent(RenderWorldLastEvent evt) {
        PlayerEntity myplayer = Minecraft.getInstance().player;

        if (myplayer.getHeldItem(Hand.MAIN_HAND).getItem() instanceof AntigooPaste || myplayer.getHeldItem(Hand.OFF_HAND).getItem() instanceof AntigooPaste)
            AntiGooRender.renderAntiGoo(evt);
        if (myplayer.getHeldItem(Hand.MAIN_HAND).getItem() instanceof GooScanner || myplayer.getHeldItem(Hand.OFF_HAND).getItem() instanceof GooScanner)
            GooScannerRender.renderGoo(evt);
    }


}
