package com.direwolf20.diregoo.client;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.tiles.TurretBlockTileEntityRender;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Only put client code here plz.
 */
public final class ClientSetup {
    public static void setup() {
        registerRenderers();
        //registerContainerScreens();
    }

    /**
     * Called from some Client Dist runner in the main class
     */
    private static void registerContainerScreens() {
        //ScreenManager.registerFactory(ModContainers.MODIFICATIONTABLE_CONTAINER.get(), ModificationTableScreen::new);
        //ScreenManager.registerFactory(ModContainers.FILTER_CONTAINER.get(), FilterScreen::new);
    }

    /**
     * Client Registry for renders
     */
    private static void registerRenderers() {
        ClientRegistry.bindTileEntityRenderer(ModBlocks.TURRETBLOCK_TILE.get(), TurretBlockTileEntityRender::new);
    }
}
