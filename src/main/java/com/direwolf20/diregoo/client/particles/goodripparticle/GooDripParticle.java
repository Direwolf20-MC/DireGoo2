package com.direwolf20.diregoo.client.particles.goodripparticle;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

// Steal
public class GooDripParticle extends SpriteTexturedParticle {
    BlockState blockState;

    public GooDripParticle(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double speedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, speedIn);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
        this.motionX = 0;
        this.motionY = -0.1;
        this.motionZ = 0;
        this.maxAge = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
        this.particleAlpha = .8f;
        this.setColor((float) xSpeedIn, (float) ySpeedIn, (float) speedIn);
        this.maxAge = 40;
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    //Without this - the particles turn dark when they impact a solid block
    @Override
    protected int getBrightnessForRender(float p_189214_1_) {
        return 14680192;
    }

    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        BlockPos particlePos = new BlockPos(this.posX, this.posY, this.posZ);
        BlockState inBlockState = world.getBlockState(particlePos);
        if (!inBlockState.getBlock().isAir(inBlockState, world, particlePos)) {
            this.motionY = 0;
        }
        this.checkAge();
        if (!this.isExpired) {
            this.motionY -= (double) this.particleGravity;
            this.move(this.motionX, this.motionY, this.motionZ);
            if (!this.isExpired) {
                this.motionX *= 0.9800000190734863D;
                this.motionY *= 0.9800000190734863D;
                this.motionZ *= 0.9800000190734863D;
            }
        }
    }

    protected void checkAge() {
        if (this.maxAge-- <= 0) {
            this.setExpired();
        }

    }
}
