package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.client.entityrenders.GNTEntityRender;
import com.direwolf20.diregoo.client.entityrenders.GooEntityRender;
import com.direwolf20.diregoo.client.entityrenders.GooSpreadEntityRender;
import com.direwolf20.diregoo.client.entityrenders.LaserGunParticleEntityRender;
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
                        .size(0.25f, 0.25f)
                        .setShouldReceiveVelocityUpdates(false)
                        .setCustomClientFactory(((spawnEntity, world) -> new GooEntity(GooEntity.TYPE, world)))
                        .build("")
                        .setRegistryName(DireGoo.MOD_ID + ":gooentity")
        );
        event.getRegistry().register(
                EntityType.Builder.<GooSpreadEntity>create(GooSpreadEntity::new, EntityClassification.MISC)
                        .setTrackingRange(64)
                        .setUpdateInterval(1)
                        .size(0.25f, 0.25f)
                        .setShouldReceiveVelocityUpdates(false)
                        .setCustomClientFactory(((spawnEntity, world) -> new GooSpreadEntity(GooSpreadEntity.TYPE, world)))
                        .build("")
                        .setRegistryName(DireGoo.MOD_ID + ":goospreadentity")
        );
        event.getRegistry().register(
                EntityType.Builder.<LaserGunParticleEntity>create(LaserGunParticleEntity::new, EntityClassification.MISC)
                        .setTrackingRange(64)
                        .setUpdateInterval(1)
                        .size(0.1f, 0.1f)
                        .setShouldReceiveVelocityUpdates(false)
                        .setCustomClientFactory(((spawnEntity, world) -> new LaserGunParticleEntity(LaserGunParticleEntity.TYPE, world)))
                        .build("")
                        .setRegistryName(DireGoo.MOD_ID + ":lasergunparticleentity")
        );
        event.getRegistry().register(
                EntityType.Builder.<GoonadeEntity>create(GoonadeEntity::new, EntityClassification.MISC)
                        .setTrackingRange(64)
                        .setUpdateInterval(1)
                        .setShouldReceiveVelocityUpdates(false)
                        .setCustomClientFactory(((spawnEntity, world) -> new GoonadeEntity(GoonadeEntity.TYPE, world)))
                        .build("")
                        .setRegistryName(DireGoo.MOD_ID + ":goonadeentity")
        );
        event.getRegistry().register(
                EntityType.Builder.<GoonadeFreezeEntity>create(GoonadeFreezeEntity::new, EntityClassification.MISC)
                        .setTrackingRange(64)
                        .setUpdateInterval(1)
                        .setShouldReceiveVelocityUpdates(false)
                        .setCustomClientFactory(((spawnEntity, world) -> new GoonadeFreezeEntity(GoonadeFreezeEntity.TYPE, world)))
                        .build("")
                        .setRegistryName(DireGoo.MOD_ID + ":goonadefreezeentity")
        );
        event.getRegistry().register(
                EntityType.Builder.<GNTEntity>create(GNTEntity::new, EntityClassification.MISC)
                        .setTrackingRange(64)
                        .setUpdateInterval(1)
                        .setShouldReceiveVelocityUpdates(false)
                        .setCustomClientFactory(((spawnEntity, world) -> new GNTEntity(GNTEntity.TYPE, world)))
                        .build("")
                        .setRegistryName(DireGoo.MOD_ID + ":gntentity")
        );
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt) {
        RenderingRegistry.registerEntityRenderingHandler(GooEntity.TYPE, GooEntityRender::new);
        RenderingRegistry.registerEntityRenderingHandler(GooSpreadEntity.TYPE, GooSpreadEntityRender::new);
        RenderingRegistry.registerEntityRenderingHandler(LaserGunParticleEntity.TYPE, LaserGunParticleEntityRender::new);
        RenderingRegistry.registerEntityRenderingHandler(GoonadeEntity.TYPE, SpriteRendererGoo::new);
        RenderingRegistry.registerEntityRenderingHandler(GoonadeFreezeEntity.TYPE, SpriteRendererGoo::new);
        RenderingRegistry.registerEntityRenderingHandler(GNTEntity.TYPE, GNTEntityRender::new);
    }
}
