package com.direwolf20.diregoo.client;

import com.direwolf20.diregoo.client.screens.AntiGooBeaconScreen;
import com.direwolf20.diregoo.client.screens.AntiGooFieldGenScreen;
import com.direwolf20.diregoo.client.screens.TurretScreen;
import com.direwolf20.diregoo.client.screens.ZapperTurretScreen;
import com.direwolf20.diregoo.client.tilerenders.AntiGooFieldGenTileEntityRender;
import com.direwolf20.diregoo.client.tilerenders.TurretBlockTileEntityRender;
import com.direwolf20.diregoo.client.tilerenders.ZapperTurretTileEntityRender;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Only put client code here plz.
 */
public final class ClientSetup {
    public static void setup() {
        registerRenderers();
        registerContainerScreens();
    }

    /**
     * Called from some Client Dist runner in the main class
     */
    private static void registerContainerScreens() {
        ScreenManager.registerFactory(ModBlocks.ANTI_GOO_FIELD_GEN_CONTAINER.get(), AntiGooFieldGenScreen::new);
        ScreenManager.registerFactory(ModBlocks.TURRET_CONTAINER.get(), TurretScreen::new);
        ScreenManager.registerFactory(ModBlocks.ZAPPER_TURRET_CONTAINER.get(), ZapperTurretScreen::new);
        ScreenManager.registerFactory(ModBlocks.ANTI_GOO_BEACON_CONTAINER.get(), AntiGooBeaconScreen::new);
    }

    /**
     * Client Registry for renders
     */
    private static void registerRenderers() {
        ClientRegistry.bindTileEntityRenderer(ModBlocks.TURRETBLOCK_TILE.get(), TurretBlockTileEntityRender::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.ANTI_GOO_FIELD_GEN_TILE.get(), AntiGooFieldGenTileEntityRender::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.ZAPPERTURRET_TILE.get(), ZapperTurretTileEntityRender::new);
    }
}
