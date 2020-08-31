package com.direwolf20.diregoo;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

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
    public static ForgeConfigSpec.BooleanValue ANIMATE_SPREAD;
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
        CAN_SPREAD_ALL = COMMON_BUILDER.comment("Can the goo spread. Set the false to disable all goo spreading.")
                .define("canSpreadAll", true);
        PLAYER_SPREAD_RANGE = COMMON_BUILDER.comment("The range from players in which Goo can spread. Goo outside of this range won't spread (Like vanilla mob spawners)")
                .defineInRange("playerSpreadRange", 100, 0, Integer.MAX_VALUE);
        SPREAD_TICK_DELAY = COMMON_BUILDER.comment("The max delay (in ticks) for goo to spread - the higher the number the slower the spread. -1 disables this")
                .defineInRange("spreadTickDelay", -1, -1, Integer.MAX_VALUE);
        ANIMATE_SPREAD = COMMON_BUILDER.comment("Does the goo use entities to animate spread, or do simple setblocks?")
                .define("animateSpread", true);

        COMMON_BUILDER.comment("Normal Goo Specific Settings").push(SUBCATEGORY_GOO);
        CAN_SPREAD_GOO = COMMON_BUILDER.comment("Can the normal goo spread. Set the false to disable only normal goo spreading.")
                .define("canSpreadGoo", true);
        SPREADCHANCEGOO = COMMON_BUILDER.comment("The chance that goo will spread when it randomly ticks. The lower this is, the slower goo spreads.")
                .defineInRange("spreadChanceGoo", 100, 1, 100);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Terrain Goo Specific Settings").push(SUBCATEGORY_GOO_TERRAIN);
        CAN_SPREAD_TERRAIN = COMMON_BUILDER.comment("Can the terrain goo spread. Set the false to disable only terrain goo spreading.")
                .define("canSpreadTerrain", true);
        SPREADCHANCETERRAIN = COMMON_BUILDER.comment("The chance that terrain goo will spread when it randomly ticks. The lower this is, the slower goo spreads.")
                .defineInRange("spreadChanceTerrain", 100, 1, 100);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Burst Goo Specific Settings").push(SUBCATEGORY_GOO_BURST);
        CAN_SPREAD_BURST = COMMON_BUILDER.comment("Can the burst goo spread. Set the false to disable only burst goo spreading.")
                .define("canSpreadBurst", true);
        SPREADCHANCEBURST = COMMON_BUILDER.comment("The chance that burst goo will spread when it randomly ticks. The lower this is, the slower goo spreads.")
                .defineInRange("spreadChanceBurst", 25, 1, 100);
        MINBURST = COMMON_BUILDER.comment("The minimum number of blocks BurstGoo can do at once - it will randomly pick a number between Min and Max")
                .defineInRange("minBurst", 5, 1, 25);
        MAXBURST = COMMON_BUILDER.comment("The maximum number of blocks BurstGoo can do at once - it will randomly pick a number between Min and Max")
                .defineInRange("maxBurst", 15, 1, 25);
        COMMON_BUILDER.pop();
    }
}

