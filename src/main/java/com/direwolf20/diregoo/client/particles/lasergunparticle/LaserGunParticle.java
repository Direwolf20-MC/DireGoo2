package com.direwolf20.diregoo.client.particles.lasergunparticle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class LaserGunParticle extends SpriteTexturedParticle {
    private double sourceX;
    private double sourceY;
    private double sourceZ;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int speedModifier;
    private Random rand = new Random();
    private int particlePicker;
    protected final IAnimatedSprite spriteSet;

    //public static final ResourceLocation iceParticle = new ResourceLocation(MiningGadgets.MOD_ID + ":textures/particle/snowflake1.png");


    public LaserGunParticle(ClientWorld world, double sourceX, double sourceY, double sourceZ, double targetX, double targetY, double targetZ, double xSpeed, double ySpeed, double zSpeed,
                            float size, float red, float green, float blue, boolean collide, float maxAge, IAnimatedSprite sprite) {
        super(world, sourceX, sourceY, sourceZ);
        motionX = xSpeed;
        motionY = ySpeed;
        motionZ = zSpeed;
        particleRed = red;
        particleGreen = green;
        particleBlue = blue;
        particleGravity = 0;
        this.maxAge = Math.round(maxAge);

        setSize(0.001F, 0.001F);

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        this.particleScale = size;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.canCollide = false;
        this.setGravity(0f);
        particlePicker = 1;
        this.spriteSet = sprite;
        this.setSprite(sprite.get(particlePicker, 4));
    }

    @Override
    public void renderParticle(IVertexBuilder p_225606_1_, ActiveRenderInfo p_225606_2_, float p_225606_3_) {
        super.renderParticle(p_225606_1_, p_225606_2_, p_225606_3_);
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // [VanillaCopy] of super, without drag when onGround is true
    @Override
    public void tick() {
        double moveX;
        double moveY;
        double moveZ;

        //Just in case something goes weird, we remove the particle if its been around too long.
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        }
        BlockState inBlock = world.getBlockState(new BlockPos(posX, posY, posZ));
        if (!inBlock.getMaterial().equals(Material.AIR)) {
            System.out.println("I just hit a " + inBlock);
            this.setExpired();
            return;
        }
        //prevPos is used in the render. if you don't do this your particle rubber bands (Like lag in an MMO).
        //This is used because ticks are 20 per second, and FPS is usually 60 or higher.
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        //Vector3d sourcePos = new Vector3d(sourceX, sourceY, sourceZ);
        //Vector3d targetPos = new Vector3d(targetX, targetY, targetZ);

        //Get the current position of the particle, and figure out the vector of where it's going
        //Vector3d partPos = new Vector3d(this.posX, this.posY, this.posZ);
        //Vector3d targetDirection = new Vector3d(targetPos.getX() - this.posX, targetPos.getY() - this.posY, targetPos.getZ() - this.posZ);

        //The total distance between the particle and target
        //double totalDistance = targetPos.distanceTo(partPos);
        //if (totalDistance < 0.1)
        //    this.setExpired();

        //double speedAdjust = 20;

        moveX = targetX * .125;
        moveY = targetY * .125;
        moveZ = targetZ * .125;


        //BlockPos nextPos = new BlockPos(this.posX + moveX, this.posY + moveY, this.posZ + moveZ);

        if (age > 40)
            //if (world.getBlockState(nextPos).getBlock() == ModBlocks.RENDERBLOCK)
            this.canCollide = false;
        //Perform the ACTUAL move of the particle.
        this.move(moveX, moveY, moveZ);
    }

    public void setGravity(float value) {
        particleGravity = value;
    }

    public void setSpeed(float mx, float my, float mz) {
        motionX = mx;
        motionY = my;
        motionZ = mz;
    }

}
