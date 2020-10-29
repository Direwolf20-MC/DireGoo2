package com.direwolf20.diregoo.common.world;

import com.direwolf20.diregoo.Config;
import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class GenHandler {

    public static ConfiguredFeature<?, ?> GooBlocksAir;
    public static ConfiguredFeature<?, ?> GooBlocksUnderground;
    public static ConfiguredFeature<?, ?> GooBlocksTerrainUnderground;

    public static void registerConfiguredFeatures() {
        Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;

        GooBlocksAir = ModBlocks.GOOFEATURE.get()
                .withConfiguration(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.AIR), ModBlocks.GOO_BLOCK.get().getDefaultState(), 4))
                .withPlacement(Placement.field_242907_l.configure(new TopSolidRangeConfig(Config.GEN_NORMAL_ABOVEGROUND_YMIN.get(), Config.GEN_NORMAL_ABOVEGROUND_YMIN.get(), Config.GEN_NORMAL_ABOVEGROUND_YMAX.get()))) //Bottom Offset, Top Offset, Maximum
                .func_242729_a(Config.GEN_NORMAL_ABOVEGROUND_CHANCE.get()); //% Chance to spawn - Bigger Number == More Rare
        Registry.register(registry, new ResourceLocation(DireGoo.MOD_ID, "gooblock_gen_air"), GooBlocksAir);
    }

    public static void addStuffToBiomes(BiomeLoadingEvent event) {
        RegistryKey<Biome> biome = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
        if (Config.CAN_GEN_NORMAL_ABOVEGROUND.get()) {
            if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.VOID) && isValidBiome(event.getCategory())) {
                event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, GooBlocksAir);
            }
        }
    }

    private static boolean isValidBiome(Biome.Category biomeCategory) {
        //If this does weird things to unclassified biomes (Category.NONE), then we should also mark that biome as invalid
        return biomeCategory != Biome.Category.THEEND && biomeCategory != Biome.Category.NETHER;
    }
}