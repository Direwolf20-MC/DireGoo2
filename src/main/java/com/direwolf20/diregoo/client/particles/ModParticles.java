package com.direwolf20.diregoo.client.particles;


import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.client.particles.lasergunparticle.LaserGunParticleData;
import com.direwolf20.diregoo.client.particles.lasergunparticle.LaserGunParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

// TODO: 12/07/2020 Replaces this with a deffered register
@Mod.EventBusSubscriber(modid = DireGoo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(DireGoo.MOD_ID)
public class ModParticles {
    @ObjectHolder("lasergunparticle")
    public static ParticleType<LaserGunParticleData> LASERGUNPARTICLE;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt) {
        evt.getRegistry().registerAll(
                new LaserGunParticleType().setRegistryName("lasergunparticle"));
    }
}

