package com.direwolf20.diregoo.client.particles.lasergunparticle;

import com.direwolf20.diregoo.client.particles.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;
import java.util.Locale;

public class LaserGunParticleData implements IParticleData {
    public final float size;
    public final float r, g, b;
    public final float maxAgeMul;
    public final boolean depthTest;
    public final double targetX;
    public final double targetY;
    public final double targetZ;

    public static LaserGunParticleData laserparticle(double targetX, double targetY, double targetZ, float size, float r, float g, float b) {
        return laserparticle(targetX, targetY, targetZ, size, r, g, b, 1);
    }

    public static LaserGunParticleData laserparticle(double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAgeMul) {
        return laserparticle(targetX, targetY, targetZ, size, r, g, b, maxAgeMul, true);
    }

    public static LaserGunParticleData laserparticle(double targetX, double targetY, double targetZ, float size, float r, float g, float b, boolean depth) {
        return laserparticle(targetX, targetY, targetZ, size, r, g, b, 1, depth);
    }

    public static LaserGunParticleData laserparticle(double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAgeMul, boolean depthTest) {
        return new LaserGunParticleData(targetX, targetY, targetZ, size, r, g, b, maxAgeMul, depthTest);
    }

    private LaserGunParticleData(double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAgeMul, boolean depthTest) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.maxAgeMul = maxAgeMul;
        this.depthTest = depthTest;
    }


    @Nonnull
    @Override
    public ParticleType<LaserGunParticleData> getType() {
        return ModParticles.LASERGUNPARTICLE;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeDouble(targetX);
        buf.writeDouble(targetY);
        buf.writeDouble(targetZ);
        buf.writeFloat(size);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(maxAgeMul);
        buf.writeBoolean(depthTest);
    }

    @Nonnull
    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %s",
                this.getType().getRegistryName(), this.size, this.r, this.g, this.b, this.maxAgeMul, this.depthTest);
    }

    public static final IParticleData.IDeserializer<LaserGunParticleData> DESERIALIZER = new IParticleData.IDeserializer<LaserGunParticleData>() {
        @Nonnull
        @Override
        public LaserGunParticleData deserialize(@Nonnull ParticleType<LaserGunParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double targetX = reader.readDouble();
            reader.expect(' ');
            double targetY = reader.readDouble();
            reader.expect(' ');
            double targetZ = reader.readDouble();
            reader.expect(' ');
            float size = reader.readFloat();
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float mam = reader.readFloat();
            boolean depth = true;
            if (reader.canRead()) {
                reader.expect(' ');
                depth = reader.readBoolean();
            }
            return new LaserGunParticleData(targetX, targetY, targetZ, size, r, g, b, mam, depth);
        }

        @Override
        public LaserGunParticleData read(@Nonnull ParticleType<LaserGunParticleData> type, PacketBuffer buf) {
            return new LaserGunParticleData(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean());
        }
    };
}
