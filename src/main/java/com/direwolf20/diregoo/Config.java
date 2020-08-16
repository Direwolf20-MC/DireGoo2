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

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue PLAYER_SPREAD_RANGE;
    public static ForgeConfigSpec.IntValue TURRET_RANGE;
    public static ForgeConfigSpec.IntValue SPREAD_TICK_DELAY;
    public static ForgeConfigSpec.IntValue MINBURST;
    public static ForgeConfigSpec.IntValue MAXBURST;
    public static ForgeConfigSpec.BooleanValue CAN_SPREAD;


    static {

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

        setupGooConfig();

        COMMON_BUILDER.pop();


        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupGooConfig() {
        PLAYER_SPREAD_RANGE = COMMON_BUILDER.comment("The range from players in which Goo can spread.")
                .defineInRange("playerSpreadRange", 100, 0, Integer.MAX_VALUE);
        SPREAD_TICK_DELAY = COMMON_BUILDER.comment("The max delay (in ticks) for good to spread - the higher the number the slower the spread")
                .defineInRange("spreadTickDelay", 150, -1, Integer.MAX_VALUE);
        TURRET_RANGE = COMMON_BUILDER.comment("The range of the turret block (AKA Radius)")
                .defineInRange("turretRange", 5, 0, 25);
        MINBURST = COMMON_BUILDER.comment("The minimum number of blocks BurstGoo can do at once - it will randomly pick a number between Min and Max")
                .defineInRange("minBurst", 5, 1, 25);
        MAXBURST = COMMON_BUILDER.comment("The maximum number of blocks BurstGoo can do at once - it will randomly pick a number between Min and Max")
                .defineInRange("maxBurst", 15, 1, 25);

        CAN_SPREAD = COMMON_BUILDER.comment("Can the goo spread. Set the false to disable all good spreading.")
                .define("canSpread", true);
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

