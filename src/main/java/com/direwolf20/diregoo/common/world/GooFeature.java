package com.direwolf20.diregoo.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.Random;

public class GooFeature extends OreFeature {
    public GooFeature(Codec<OreFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    /**
     * Matches the vanilla version, but can place blocks in the sky rather than only at the top of solid blocks.
     */
    @Override
    public boolean generate(ISeedReader p_241855_1_, ChunkGenerator p_241855_2_, Random p_241855_3_, BlockPos p_241855_4_, OreFeatureConfig p_241855_5_) {
        float f = p_241855_3_.nextFloat() * (float) Math.PI;
        float f1 = (float) p_241855_5_.size / 8.0F;
        int i = MathHelper.ceil(((float) p_241855_5_.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double d0 = (double) p_241855_4_.getX() + Math.sin((double) f) * (double) f1;
        double d1 = (double) p_241855_4_.getX() - Math.sin((double) f) * (double) f1;
        double d2 = (double) p_241855_4_.getZ() + Math.cos((double) f) * (double) f1;
        double d3 = (double) p_241855_4_.getZ() - Math.cos((double) f) * (double) f1;
        int j = 2;
        double d4 = (double) (p_241855_4_.getY() + p_241855_3_.nextInt(3) - 2);
        double d5 = (double) (p_241855_4_.getY() + p_241855_3_.nextInt(3) - 2);
        int k = p_241855_4_.getX() - MathHelper.ceil(f1) - i;
        int l = p_241855_4_.getY() - 2 - i;
        int i1 = p_241855_4_.getZ() - MathHelper.ceil(f1) - i;
        int j1 = 2 * (MathHelper.ceil(f1) + i);
        int k1 = 2 * (2 + i);

        for (int l1 = k; l1 <= k + j1; ++l1) {
            for (int i2 = i1; i2 <= i1 + j1; ++i2) {
                return this.func_207803_a(p_241855_1_, p_241855_3_, p_241855_5_, d0, d1, d2, d3, d4, d5, k, l, i1, j1, k1);
            }
        }

        return false;
    }
}
