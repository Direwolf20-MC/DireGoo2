package com.direwolf20.diregoo;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static final String CATEGORY_GOO_SPREAD = "goo_spread";
    public static ForgeConfigSpec.IntValue PLAYER_SPREAD_RANGE;
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_ALL;
    public static ForgeConfigSpec.IntValue SPREAD_TICK_DELAY;
    public static ForgeConfigSpec.BooleanValue ANIMATE_SPREAD;
    public static ForgeConfigSpec.IntValue FREEZE_MELT_CHANCE;
    public static ForgeConfigSpec.IntValue MAX_BLOCK_CHANGES;
    public static ForgeConfigSpec.IntValue MAX_CHUNK_CHANGES;
    public static ForgeConfigSpec.IntValue MAX_CHUNK_QUEUE;
    public static ForgeConfigSpec.IntValue GOO_SPREAD_FREQUENCY;
    public static ForgeConfigSpec.BooleanValue BATCH_GOO_SPREAD;

    public static final String SUBCATEGORY_GOO = "normal_goo";
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_GOO;
    public static ForgeConfigSpec.IntValue SPREADCHANCEGOO;

    public static final String SUBCATEGORY_GOO_TERRAIN = "terrain_goo";
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_TERRAIN;
    public static ForgeConfigSpec.IntValue SPREADCHANCETERRAIN;

    /*public static final String SUBCATEGORY_GOO_BURST = "burst_goo";
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_BURST;
    public static ForgeConfigSpec.IntValue SPREADCHANCEBURST;
    public static ForgeConfigSpec.IntValue MINBURST;
    public static ForgeConfigSpec.IntValue MAXBURST;*/

    public static final String SUBCATEGORY_GNT = "gnt";
    public static ForgeConfigSpec.IntValue GNT_TIER1_RADIUS;
    public static ForgeConfigSpec.IntValue GNT_TIER2_RADIUS;
    public static ForgeConfigSpec.IntValue GNT_TIER3_RADIUS;
    public static ForgeConfigSpec.IntValue GNT_TIER4_RADIUS;

    public static final String CATEGORY_RFCOSTS = "rfcosts";
    //public static ForgeConfigSpec.IntValue TILEMAXENERGY;

    public static final String SUBCATEGORY_TILE_TURRET = "tile_turret";
    //public static ForgeConfigSpec.IntValue TURRET_RANGE;
    public static ForgeConfigSpec.IntValue TURRET_RFCOST;
    public static ForgeConfigSpec.IntValue TURRET_BOOST_COUNT;
    public static ForgeConfigSpec.IntValue TURRET_MAX_RF;

    public static final String SUBCATEGORY_TILE_ZAPPER = "tile_zapper_turret";
    public static ForgeConfigSpec.IntValue ZAPPER_TILE_RFCOST;
    public static ForgeConfigSpec.IntValue ZAPPER_TILE_MAXRF;

    public static final String SUBCATEGORY_TILE_ANTIGOOFIELD = "tile_antigoofield";
    public static ForgeConfigSpec.DoubleValue ANTIGOOFIELDGENRF;
    public static ForgeConfigSpec.IntValue ANTIGOOFIELDGENRF_MAXRF;

    public static final String SUBCATEGORY_TILE_GOOLIMINATION = "tile_goolimination";
    public static ForgeConfigSpec.IntValue GOOLIMINATION_RF_PER_TICK;
    public static ForgeConfigSpec.IntValue GOOLIMINATION_MAXRF;

    public static final String SUBCATEGORY_ITEM_REMOVER = "item_remover";
    public static ForgeConfigSpec.IntValue ITEM_REMOVER_RFCOST;
    public static ForgeConfigSpec.IntValue ITEM_REMOVER_RFMAX;

    public static final String SUBCATEGORY_ITEM_ZAPPER = "item_zapper";
    public static ForgeConfigSpec.IntValue ITEM_ZAPPER_RFCOST_MELT;
    public static ForgeConfigSpec.IntValue ITEM_ZAPPER_RFCOST_FREEZE;
    public static ForgeConfigSpec.IntValue ITEM_ZAPPER_RFCOST_TIER_MULTIPLIER;
    public static ForgeConfigSpec.IntValue ITEM_ZAPPER_RFMAX;


    public static final String CATEGORY_WORLDGEN = "worldgen";

    public static final String SUBCATEGORY_WORLDGEN_NORMAL_GOO = "worldgen_normalgoo";
    public static ForgeConfigSpec.BooleanValue CAN_GEN_NORMAL_UNDERGROUND;
    public static ForgeConfigSpec.IntValue GEN_NORMAL_UNDERGROUND_CHANCE;
    public static ForgeConfigSpec.IntValue GEN_NORMAL_UNDERGROUND_YMIN;
    public static ForgeConfigSpec.IntValue GEN_NORMAL_UNDERGROUND_YMAX;
    public static ForgeConfigSpec.BooleanValue CAN_GEN_NORMAL_ABOVEGROUND;
    public static ForgeConfigSpec.IntValue GEN_NORMAL_ABOVEGROUND_CHANCE;
    public static ForgeConfigSpec.IntValue GEN_NORMAL_ABOVEGROUND_YMIN;
    public static ForgeConfigSpec.IntValue GEN_NORMAL_ABOVEGROUND_YMAX;

    public static final String SUBCATEGORY_WORLDGEN_TERRAIN_GOO = "worldgen_terraingoo";
    public static ForgeConfigSpec.BooleanValue CAN_GEN_TERRAIN_UNDERGROUND;
    public static ForgeConfigSpec.IntValue GEN_TERRAIN_UNDERGROUND_CHANCE;
    public static ForgeConfigSpec.IntValue GEN_TERRAIN_UNDERGROUND_YMIN;
    public static ForgeConfigSpec.IntValue GEN_TERRAIN_UNDERGROUND_YMAX;

    static {

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        setupGeneralConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goo Spread settings").push(CATEGORY_GOO_SPREAD);
        setupGooSpreadConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("RF SETTINGS").push(CATEGORY_RFCOSTS);
        setupRFCostConfig();
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("WORLDGEN").push(CATEGORY_WORLDGEN);
        setupWorldGenConfig();
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupGeneralConfig() {


    }

    private static void setupWorldGenConfig() {
        COMMON_BUILDER.comment("Normal Goo Worldgen Settings").push(SUBCATEGORY_WORLDGEN_NORMAL_GOO);
        CAN_GEN_NORMAL_UNDERGROUND = COMMON_BUILDER.comment("Can normal goo generate underground like ores")
                .define("can_gen_normal_underground", false);
        GEN_NORMAL_UNDERGROUND_CHANCE = COMMON_BUILDER.comment("The chance for goo to generate underground, 1 in X chunks. Bigger number means more rare")
                .defineInRange("gen_normal_underground_chance", 25, 1, 10000);
        GEN_NORMAL_UNDERGROUND_YMIN = COMMON_BUILDER.comment("The minimum Y level that normal goo can spawn underground")
                .defineInRange("gen_normal_underground_ymin", 0, 0, 255);
        GEN_NORMAL_UNDERGROUND_YMAX = COMMON_BUILDER.comment("The maximum Y level that normal goo can spawn underground")
                .defineInRange("gen_normal_underground_ymax", 16, 0, 255);
        CAN_GEN_NORMAL_ABOVEGROUND = COMMON_BUILDER.comment("Can normal goo generate above ground (in the air)")
                .define("can_gen_normal_aboveground", false);
        GEN_NORMAL_ABOVEGROUND_CHANCE = COMMON_BUILDER.comment("The chance for goo to generate aboveground, 1 in X chunks. Bigger number means more rare")
                .defineInRange("gen_normal_aboveground_chance", 25, 1, 10000);
        GEN_NORMAL_ABOVEGROUND_YMIN = COMMON_BUILDER.comment("The minimum Y level that normal goo can spawn aboveground")
                .defineInRange("gen_normal_aboveground_ymin", 70, 0, 255);
        GEN_NORMAL_ABOVEGROUND_YMAX = COMMON_BUILDER.comment("The maximum Y level that normal goo can spawn aboveground")
                .defineInRange("gen_normal_aboveground_ymax", 125, 0, 255);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Terrain Goo Worldgen Settings").push(SUBCATEGORY_WORLDGEN_TERRAIN_GOO);
        CAN_GEN_TERRAIN_UNDERGROUND = COMMON_BUILDER.comment("Can terrain goo generate underground like ores")
                .define("can_gen_terrain_underground", false);
        GEN_TERRAIN_UNDERGROUND_CHANCE = COMMON_BUILDER.comment("The chance for terrain goo to generate underground, 1 in X chunks. Bigger number means more rare")
                .defineInRange("gen_terrain_underground_chance", 25, 1, 10000);
        GEN_TERRAIN_UNDERGROUND_YMIN = COMMON_BUILDER.comment("The minimum Y level that terrain goo can spawn underground")
                .defineInRange("gen_terrain_underground_ymin", 0, 0, 255);
        GEN_TERRAIN_UNDERGROUND_YMAX = COMMON_BUILDER.comment("The maximum Y level that terrain goo can spawn underground")
                .defineInRange("gen_terrain_underground_ymax", 16, 0, 255);
        COMMON_BUILDER.pop();

    }

    private static void setupRFCostConfig() {
        COMMON_BUILDER.comment("Goo Removal Turret Settings").push(SUBCATEGORY_TILE_TURRET);
        /*TURRET_RANGE = COMMON_BUILDER.comment("The range of the turret block (AKA Radius)")
                .defineInRange("turretRange", 5, 0, 25);*/
        TURRET_RFCOST = COMMON_BUILDER.comment("The RF cost per shot (Per block removed)")
                .defineInRange("turretRFCost", 1000, 0, Integer.MAX_VALUE);
        TURRET_BOOST_COUNT = COMMON_BUILDER.comment("The number of boosted (Faster) shots per AntiGooDust")
                .defineInRange("turretBoostCount", 10, 0, Integer.MAX_VALUE);
        TURRET_MAX_RF = COMMON_BUILDER.comment("The max amount of RF in the Goo Removal Turret")
                .defineInRange("turretMaxEnergy", 1000000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goo Zapper Turret Settings").push(SUBCATEGORY_TILE_ZAPPER);
        ZAPPER_TILE_RFCOST = COMMON_BUILDER.comment("The base RF cost per shot (Per block activation). This increases based on installed upgrades")
                .defineInRange("zapperTileRFCost", 10000, 0, Integer.MAX_VALUE);
        ZAPPER_TILE_MAXRF = COMMON_BUILDER.comment("The max amount of RF in the Zapper Turret")
                .defineInRange("zapperMaxEnergy", 1000000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("AntiGoo field Generator Settings").push(SUBCATEGORY_TILE_ANTIGOOFIELD);
        ANTIGOOFIELDGENRF = COMMON_BUILDER.comment("The RF cost per block protected by the Anti Goo Field Generator")
                .defineInRange("antiGooFieldCost", 1, 0, Double.MAX_VALUE);
        ANTIGOOFIELDGENRF_MAXRF = COMMON_BUILDER.comment("The max amount of RF in the Antigoo Field Generator")
                .defineInRange("antigooMaxEnergy", 10000000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goolimination Field Gen Settings").push(SUBCATEGORY_TILE_GOOLIMINATION);
        GOOLIMINATION_RF_PER_TICK = COMMON_BUILDER.comment("The RF cost per tick to run the Goolimination Field Generator")
                .defineInRange("gooliminationCost", 1000000, 0, Integer.MAX_VALUE);
        GOOLIMINATION_MAXRF = COMMON_BUILDER.comment("The max amount of RF in the Goolimination Field Generator")
                .defineInRange("gooliminationMaxEnergy", 1000000000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goo Remover (Item) Settings").push(SUBCATEGORY_ITEM_REMOVER);
        ITEM_REMOVER_RFCOST = COMMON_BUILDER.comment("The RF cost per shot (Per block removed)")
                .defineInRange("removerRFCost", 1000, 0, Integer.MAX_VALUE);
        ITEM_REMOVER_RFMAX = COMMON_BUILDER.comment("The max RF the Goo Remover can hold")
                .defineInRange("removerRFMax", 1000000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goo Zapper (Item) Settings").push(SUBCATEGORY_ITEM_ZAPPER);
        ITEM_ZAPPER_RFCOST_MELT = COMMON_BUILDER.comment("The base RF cost per tick while melting")
                .defineInRange("zapperRFCostMelt", 500, 0, Integer.MAX_VALUE);
        ITEM_ZAPPER_RFCOST_FREEZE = COMMON_BUILDER.comment("The base RF cost per tick while freezing")
                .defineInRange("zapperRFCostFreeze", 250, 0, Integer.MAX_VALUE);
        ITEM_ZAPPER_RFCOST_TIER_MULTIPLIER = COMMON_BUILDER.comment("The RF Cost multiplier per tier of Radius Upgrade. T1 = x. T2 = 2x. T3 = 3x. Etc")
                .defineInRange("zapperRFCostTierMultiplier", 3, 0, Integer.MAX_VALUE);
        ITEM_ZAPPER_RFMAX = COMMON_BUILDER.comment("The max RF the Goo Zapper can hold")
                .defineInRange("zapperRFMax", 10000000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        /*COMMON_BUILDER.comment("Goo Freezer (Item) Settings").push(SUBCATEGORY_ITEM_FREEZER);
        ITEM_FREEZER_RFCOST = COMMON_BUILDER.comment("The RF cost per tick")
                .defineInRange("freezerRFCost", 1000, 0, Integer.MAX_VALUE);
        ITEM_FREEZER_RFMAX = COMMON_BUILDER.comment("The max RF the Goo Freezer can hold")
                .defineInRange("freezerRFMax", 1000000, 0, Integer.MAX_VALUE);
        ITEM_FREEZER_RANGE = COMMON_BUILDER.comment("The range of the Goo Freezer")
                .defineInRange("freezerRange", 15, 0, 30);
        COMMON_BUILDER.pop();*/

        /*COMMON_BUILDER.comment("Goo Scanner Settings").push(SUBCATEGORY_ITEM_SCANNER);
        ITEM_SCANNER_RFCOST = COMMON_BUILDER.comment("The RF cost activation")
                .defineInRange("scannerRFCost", 50000, 0, Integer.MAX_VALUE);
        ITEM_SCANNER_RFMAX = COMMON_BUILDER.comment("The max RF the Goo Scanner can hold")
                .defineInRange("scannerRFMax", 1000000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();*/
    }

    private static void setupGooSpreadConfig() {
        CAN_SPREAD_ALL = COMMON_BUILDER.comment("Can the goo spread. Set the false to disable all goo spreading.")
                .define("canSpreadAll", true);
        PLAYER_SPREAD_RANGE = COMMON_BUILDER.comment("The range from players in which Goo can spread. Goo outside of this range won't spread (Like vanilla mob spawners)")
                .defineInRange("playerSpreadRange", 100, 0, Integer.MAX_VALUE);
        SPREAD_TICK_DELAY = COMMON_BUILDER.comment("Force extra goo ticks. This is the delay (in ticks) before goo is forced to spread - the higher the number the slower the spread. -1 disables this. Only use this if you want goo to spread even faster than normal")
                .defineInRange("spreadTickDelay", -1, -1, Integer.MAX_VALUE);
        ANIMATE_SPREAD = COMMON_BUILDER.comment("Does the goo use entities to animate spread, or do simple setblocks?")
                .define("animateSpread", true);
        FREEZE_MELT_CHANCE = COMMON_BUILDER.comment("The chance for Frozen Goo to melt on block tick. The lower this number, the longer it stays frozen.")
                .defineInRange("freezeMeltChance", 50, 0, 100);
        MAX_BLOCK_CHANGES = COMMON_BUILDER.comment("The maximum number of goo blocks that can cause a blockchange per chunk in a single tick. Lowering this number will slow the spread of goo and may improve performance.")
                .defineInRange("maxBlockChanges", 200, 1, 10000);
        MAX_CHUNK_CHANGES = COMMON_BUILDER.comment("The maximum number of chunks that can change per spreadEvent as a result of goospread. Lowering this number will slow the spread of goo and may improve performance.")
                .defineInRange("maxChunkChanges", 10, 1, 250);
        MAX_CHUNK_QUEUE = COMMON_BUILDER.comment("The maximum number of chunks that can be queue'd in the server processing queue. Probably don't need to mess with this unless you know what you're doing and why.")
                .defineInRange("maxChunkQueue", 100, 1, 500);
        GOO_SPREAD_FREQUENCY = COMMON_BUILDER.comment("How frequently the server processes a batch of goo. The amount of chunks processed is defined in maxChunkChanges. That many chunks will change every X ticks per this value")
                .defineInRange("gooSpreadFrequency", 20, 1, 2000);
        BATCH_GOO_SPREAD = COMMON_BUILDER.comment("Do we batch the goo spread? Should increase performance.")
                .define("batchSpread", true);

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

        /*COMMON_BUILDER.comment("Burst Goo Specific Settings").push(SUBCATEGORY_GOO_BURST);
        CAN_SPREAD_BURST = COMMON_BUILDER.comment("Can the burst goo spread. Set the false to disable only burst goo spreading.")
                .define("canSpreadBurst", true);
        SPREADCHANCEBURST = COMMON_BUILDER.comment("The chance that burst goo will spread when it randomly ticks. The lower this is, the slower goo spreads.")
                .defineInRange("spreadChanceBurst", 25, 1, 100);
        MINBURST = COMMON_BUILDER.comment("The minimum number of blocks BurstGoo can do at once - it will randomly pick a number between Min and Max")
                .defineInRange("minBurst", 5, 1, 25);
        MAXBURST = COMMON_BUILDER.comment("The maximum number of blocks BurstGoo can do at once - it will randomly pick a number between Min and Max")
                .defineInRange("maxBurst", 15, 1, 25);
        COMMON_BUILDER.pop();*/

        COMMON_BUILDER.comment("GNT Settings").push(SUBCATEGORY_GNT);
        GNT_TIER1_RADIUS = COMMON_BUILDER.comment("The radius of Tier 1 GNT")
                .defineInRange("gntTier1Radius", 5, 1, 100);
        GNT_TIER2_RADIUS = COMMON_BUILDER.comment("The radius of Tier 2 GNT")
                .defineInRange("gntTier2Radius", 10, 1, 100);
        GNT_TIER3_RADIUS = COMMON_BUILDER.comment("The radius of Tier 3 GNT")
                .defineInRange("gntTier3Radius", 15, 1, 100);
        GNT_TIER4_RADIUS = COMMON_BUILDER.comment("The radius of Tier 4 GNT")
                .defineInRange("gntTier4Radius", 20, 1, 100);
        COMMON_BUILDER.pop();
    }
}

