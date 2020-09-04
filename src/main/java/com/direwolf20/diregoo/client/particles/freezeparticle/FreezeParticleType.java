package com.direwolf20.diregoo.client.particles.freezeparticle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FreezeParticleType extends BasicParticleType {
    public FreezeParticleType() {
        super(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FreezeParticleFactory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public FreezeParticleFactory(IAnimatedSprite p_i50522_1_) {
            this.spriteSet = p_i50522_1_;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FreezeParticle particle = new FreezeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.selectSpriteRandomly(this.spriteSet);
            return particle;
        }
    }
}