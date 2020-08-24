package com.direwolf20.diregoo.client.particles;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.client.particles.goodripparticle.GooDripParticleType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DireGoo.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRenderDispatcher {

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent evt) {
        Minecraft.getInstance().particles.registerFactory(ModParticles.GOO_DRIP_PARTICLE, GooDripParticleType.GooDripFactory::new);
    }
}
