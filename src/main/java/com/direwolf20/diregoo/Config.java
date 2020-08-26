package com.direwolf20.diregoo;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_GOO_SPREAD = "goo_spread";
    public static final String SUBCATEGORY_GOO = "normal_goo";
    public static final String SUBCATEGORY_GOO_TERRAIN = "terrain_goo";
    public static final String SUBCATEGORY_GOO_BURST = "burst_goo";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue PLAYER_SPREAD_RANGE;
    public static ForgeConfigSpec.IntValue TURRET_RANGE;
    public static ForgeConfigSpec.IntValue TURRET_RFCOST;
    public static ForgeConfigSpec.IntValue SPREAD_TICK_DELAY;
    public static ForgeConfigSpec.IntValue MINBURST;
    public static ForgeConfigSpec.IntValue MAXBURST;
    public static ForgeConfigSpec.IntValue SPREADCHANCEGOO;
    public static ForgeConfigSpec.IntValue SPREADCHANCETERRAIN;
    public static ForgeConfigSpec.IntValue SPREADCHANCEBURST;
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_ALL;
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_GOO;
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_BURST;
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_TERRAIN;
    public static ForgeConfigSpec.DoubleValue ANTIGOOFIELDGENRF;

    public static ForgeConfigSpec.IntValue TILEMAXENERGY;


    static {

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        setupGeneralConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goo Spread settings").push(CATEGORY_GOO_SPREAD);
        setupGooSpreadConfig();
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupGeneralConfig() {
        TURRET_RANGE = COMMON_BUILDER.comment("The range of the turret block (AKA Radius)")
                .defineInRange("turretRange", 5, 0, 25);
        TURRET_RFCOST = COMMON_BUILDER.comment("The RF cost per shot (Per block removed)")
                .defineInRange("turretRFCost", 10000, 0, Integer.MAX_VALUE);
        ANTIGOOFIELDGENRF = COMMON_BUILDER.comment("The RF cost per block protected by the Anti Goo Field Generator")
                .defineInRange("antiGooFieldCost", 1, 0, Double.MAX_VALUE);
        TILEMAXENERGY = COMMON_BUILDER.comment("The max amount of RF in the Tile Entities")
                .defineInRange("tileMaxEnergy", 1000000, 0, Integer.MAX_VALUE);
    }

    private static void setupGooSpreadConfig() {
        PLAYER_SPREAD_RANGE = COMMON_BUILDER.comment("The range from players in which Goo can spread.")
                .defineInRange("playerSpreadRange", 100, 0, Integer.MAX_VALUE);
        SPREAD_TICK_DELAY = COMMON_BUILDER.comment("The max delay (in ticks) for goo to spread - the higher the number the slower the spread")
                .defineInRange("spreadTickDelay", 150, -1, Integer.MAX_VALUE);
        CAN_SPREAD_ALL = COMMON_BUILDER.comment("Can the goo spread. Set the false to disable all goo spreading.")
                .define("canSpreadAll", true);

        COMMON_BUILDER.comment("Normal Goo Specific Settings").push(SUBCATEGORY_GOO);
        SPREADCHANCEGOO = COMMON_BUILDER.comment("The chance that goo will spread when it randomly ticks. The lower this is, the slower goo spreads.")
                .defineInRange("spreadChanceGoo", 100, 1, 100);
        CAN_SPREAD_GOO = COMMON_BUILDER.comment("Can the normal goo spread. Set the false to disable only normal goo spreading.")
                .define("canSpreadGoo", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Terrain Goo Specific Settings").push(SUBCATEGORY_GOO_TERRAIN);
        SPREADCHANCETERRAIN = COMMON_BUILDER.comment("The chance that terrain goo will spread when it randomly ticks. The lower this is, the slower goo spreads.")
                .defineInRange("spreadChanceTerrain", 100, 1, 100);
        CAN_SPREAD_TERRAIN = COMMON_BUILDER.comment("Can the terrain goo spread. Set the false to disable only terrain goo spreading.")
                .define("canSpreadTerrain", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Burst Goo Specific Settings").push(SUBCATEGORY_GOO_BURST);
        SPREADCHANCEBURST = COMMON_BUILDER.comment("The chance that burst goo will spread when it randomly ticks. The lower this is, the slower goo spreads.")
                .defineInRange("spreadChanceBurst", 25, 1, 100);
        CAN_SPREAD_BURST = COMMON_BUILDER.comment("Can the burst goo spread. Set the false to disable only burst goo spreading.")
                .define("canSpreadBurst", true);
        MINBURST = COMMON_BUILDER.comment("The minimum number of blocks BurstGoo can do at once - it will randomly pick a number between Min and Max")
                .defineInRange("minBurst", 5, 1, 25);
        MAXBURST = COMMON_BUILDER.comment("The maximum number of blocks BurstGoo can do at once - it will randomly pick a number between Min and Max")
                .defineInRange("maxBurst", 15, 1, 25);
        COMMON_BUILDER.pop();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }


}

