package com.direwolf20.diregoo.client.particles;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.client.particles.lasergunparticle.LaserGunParticleType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DireGoo.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRenderDispatcher {

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent evt) {
        Minecraft.getInstance().particles.registerFactory(ModParticles.LASERGUNPARTICLE, LaserGunParticleType.FACTORY::new);
    }
}
