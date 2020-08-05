package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DireGoo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    private ModEntities() {
    }


    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(
                EntityType.Builder.<GooEntity>create(GooEntity::new, EntityClassification.MISC)
                        .setTrackingRange(64)
                        .setUpdateInterval(1)
                        .setShouldReceiveVelocityUpdates(false)
                        .setCustomClientFactory(((spawnEntity, world) -> new GooEntity(GooEntity.TYPE, world)))
                        .build("")
                        .setRegistryName(DireGoo.MOD_ID + ":gooentity")
        );
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt) {
        RenderingRegistry.registerEntityRenderingHandler(GooEntity.TYPE, GooEntityRender::new);
    }
}
