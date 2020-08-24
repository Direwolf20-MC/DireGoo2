package com.direwolf20.diregoo.client.particles;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.client.particles.goodripparticle.GooDripParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

// TODO: 12/07/2020 Replaces this with a deffered register
@Mod.EventBusSubscriber(modid = DireGoo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(DireGoo.MOD_ID)
public class ModParticles {
    /*@ObjectHolder("laserparticle")
    public static ParticleType<LaserParticleData> LASERPARTICLE;

    @ObjectHolder("playerparticle")
    public static ParticleType<PlayerParticleData> PLAYERPARTICLE;*/

    @ObjectHolder("goodrip_particle")
    public static GooDripParticleType GOO_DRIP_PARTICLE;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt) {
        evt.getRegistry().registerAll(
                new GooDripParticleType().setRegistryName("goodrip_particle")
        );
    }
}
