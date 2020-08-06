package com.direwolf20.diregoo.client.particles.lasergunparticle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;

public class LaserGunParticleType extends ParticleType<LaserGunParticleData> {
    public LaserGunParticleType() {
        super(false, LaserGunParticleData.DESERIALIZER);
    }

    @Override
    public Codec<LaserGunParticleData> func_230522_e_() {
        return null;
    }

    public static class FACTORY implements IParticleFactory<LaserGunParticleData> {
        private final IAnimatedSprite sprites;

        public FACTORY(IAnimatedSprite sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle makeParticle(LaserGunParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LaserGunParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, xSpeed, ySpeed, zSpeed, data.size, data.r, data.g, data.b, data.depthTest, data.maxAgeMul, this.sprites);
        }
    }
}
