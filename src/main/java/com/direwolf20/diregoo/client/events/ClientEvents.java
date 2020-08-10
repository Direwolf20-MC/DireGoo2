package com.direwolf20.diregoo.client.events;

import com.direwolf20.diregoo.client.renderer.AntiGooRender;
import com.direwolf20.diregoo.common.items.AntigooDust;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ClientEvents {
    @SubscribeEvent
    static void renderWorldLastEvent(RenderWorldLastEvent evt) {
        List<AbstractClientPlayerEntity> players = Minecraft.getInstance().world.getPlayers();
        PlayerEntity myplayer = Minecraft.getInstance().player;

        if (myplayer.getHeldItem(Hand.MAIN_HAND).getItem() instanceof AntigooDust || myplayer.getHeldItem(Hand.OFF_HAND).getItem() instanceof AntigooDust)
            AntiGooRender.renderAntiGoo(evt);
    }


}
