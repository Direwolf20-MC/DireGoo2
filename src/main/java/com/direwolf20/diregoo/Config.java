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

    public static final String SUBCATEGORY_GOO = "normal_goo";
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_GOO;
    public static ForgeConfigSpec.IntValue SPREADCHANCEGOO;

    public static final String SUBCATEGORY_GOO_TERRAIN = "terrain_goo";
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_TERRAIN;
    public static ForgeConfigSpec.IntValue SPREADCHANCETERRAIN;

    public static final String SUBCATEGORY_GOO_BURST = "burst_goo";
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD_BURST;
    public static ForgeConfigSpec.IntValue SPREADCHANCEBURST;
    public static ForgeConfigSpec.IntValue MINBURST;
    public static ForgeConfigSpec.IntValue MAXBURST;


    public static final String CATEGORY_RFCOSTS = "rfcosts";
    public static ForgeConfigSpec.IntValue TILEMAXENERGY;

    public static final String SUBCATEGORY_TILE_TURRET = "tile_turret";
    public static ForgeConfigSpec.IntValue TURRET_RANGE;
    public static ForgeConfigSpec.IntValue TURRET_RFCOST;

    public static final String SUBCATEGORY_TILE_ANTIGOOFIELD = "tile_antigoofield";
    public static ForgeConfigSpec.DoubleValue ANTIGOOFIELDGENRF;

    public static final String SUBCATEGORY_ITEM_REMOVER = "item_remover";
    public static ForgeConfigSpec.IntValue ITEM_REMOVER_RFCOST;
    public static ForgeConfigSpec.IntValue ITEM_REMOVER_RFMAX;

    public static final String SUBCATEGORY_ITEM_ZAPPER = "item_zapper";
    public static ForgeConfigSpec.IntValue ITEM_ZAPPER_RFCOST;
    public static ForgeConfigSpec.IntValue ITEM_ZAPPER_RFMAX;
    public static ForgeConfigSpec.IntValue ITEM_ZAPPER_RANGE;

    public static final String SUBCATEGORY_ITEM_FREEZER = "item_freezer";
    public static ForgeConfigSpec.IntValue ITEM_FREEZER_RFCOST;
    public static ForgeConfigSpec.IntValue ITEM_FREEZER_RFMAX;
    public static ForgeConfigSpec.IntValue ITEM_FREEZER_RANGE;

    /*public static final String SUBCATEGORY_ITEM_SCANNER = "item_scanner";
    public static ForgeConfigSpec.IntValue ITEM_SCANNER_RFCOST;
    public static ForgeConfigSpec.IntValue ITEM_SCANNER_RFMAX;*/

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

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupGeneralConfig() {


    }

    private static void setupRFCostConfig() {
        TILEMAXENERGY = COMMON_BUILDER.comment("The max amount of RF in the Tile Entities")
                .defineInRange("tileMaxEnergy", 1000000, 0, Integer.MAX_VALUE);

        COMMON_BUILDER.comment("Goo Removal Turret Settings").push(SUBCATEGORY_TILE_TURRET);
        TURRET_RANGE = COMMON_BUILDER.comment("The range of the turret block (AKA Radius)")
                .defineInRange("turretRange", 5, 0, 25);
        TURRET_RFCOST = COMMON_BUILDER.comment("The RF cost per shot (Per block removed)")
                .defineInRange("turretRFCost", 10000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("AntiGoo field Generator Settings").push(SUBCATEGORY_TILE_ANTIGOOFIELD);
        ANTIGOOFIELDGENRF = COMMON_BUILDER.comment("The RF cost per block protected by the Anti Goo Field Generator")
                .defineInRange("antiGooFieldCost", 1, 0, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goo Remover (Item) Settings").push(SUBCATEGORY_ITEM_REMOVER);
        ITEM_REMOVER_RFCOST = COMMON_BUILDER.comment("The RF cost per shot (Per block removed)")
                .defineInRange("removerRFCost", 10000, 0, Integer.MAX_VALUE);
        ITEM_REMOVER_RFMAX = COMMON_BUILDER.comment("The max RF the Goo Remover can hold")
                .defineInRange("removerRFMax", 1000000, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goo Zapper (Item) Settings").push(SUBCATEGORY_ITEM_ZAPPER);
        ITEM_ZAPPER_RFCOST = COMMON_BUILDER.comment("The RF cost per tick")
                .defineInRange("zapperRFCost", 1000, 0, Integer.MAX_VALUE);
        ITEM_ZAPPER_RFMAX = COMMON_BUILDER.comment("The max RF the Goo Zapper can hold")
                .defineInRange("zapperRFMax", 1000000, 0, Integer.MAX_VALUE);
        ITEM_ZAPPER_RANGE = COMMON_BUILDER.comment("The range of the Goo Zapper")
                .defineInRange("zapperRange", 15, 0, 30);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Goo Freezer (Item) Settings").push(SUBCATEGORY_ITEM_FREEZER);
        ITEM_FREEZER_RFCOST = COMMON_BUILDER.comment("The RF cost per tick")
                .defineInRange("freezerRFCost", 1000, 0, Integer.MAX_VALUE);
        ITEM_FREEZER_RFMAX = COMMON_BUILDER.comment("The max RF the Goo Freezer can hold")
                .defineInRange("freezerRFMax", 1000000, 0, Integer.MAX_VALUE);
        ITEM_FREEZER_RANGE = COMMON_BUILDER.comment("The range of the Goo Freezer")
                .defineInRange("freezerRange", 15, 0, 30);
        COMMON_BUILDER.pop();

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

