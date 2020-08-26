package com.direwolf20.diregoo;

import com.direwolf20.diregoo.client.ClientSetup;
import com.direwolf20.diregoo.client.events.ClientEvents;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.commands.ModCommands;
import com.direwolf20.diregoo.common.entities.GoonadeEntity;
import com.direwolf20.diregoo.common.entities.GoonadeFreezeEntity;
import com.direwolf20.diregoo.common.events.ServerEvents;
import com.direwolf20.diregoo.common.items.ModItems;
import com.direwolf20.diregoo.common.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DireGoo.MOD_ID)
public class DireGoo
{
    //ModID
    public static final String MOD_ID = "diregoo";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static ItemGroup itemGroup = new ItemGroup(DireGoo.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.GOO_BLOCK.get());
        }
    };

    public DireGoo() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();

        // Register all of our items, blocks, item blocks, etc
        ModBlocks.BLOCKS.register(event);
        ModItems.ITEMS.register(event);
        ModBlocks.TILES_ENTITIES.register(event);
        ModBlocks.CONTAINERS.register(event);

        //This does stuff with the Config somehow?
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        //Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-client.toml"));
        //Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-common.toml"));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);

        //ExampleMod Stuff Below

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);



    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        PacketHandler.register();
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
        //Dispenser thingy
        DefaultDispenseItemBehavior goonadeFreezeBehavior = new ProjectileDispenseBehavior() {
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return Util.make(new GoonadeFreezeEntity(worldIn, position.getX(), position.getY(), position.getZ()), (p_218409_1_) -> {
                    p_218409_1_.setItem(stackIn);
                });
            }
        };
        DispenserBlock.registerDispenseBehavior(ModItems.GOONADE_FREEZE.get(), goonadeFreezeBehavior);

        DefaultDispenseItemBehavior goonadeBehavior = new ProjectileDispenseBehavior() {
            protected ProjectileEntity getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return Util.make(new GoonadeEntity(worldIn, position.getX(), position.getY(), position.getZ()), (p_218409_1_) -> {
                    p_218409_1_.setItem(stackIn);
                });
            }
        };
        DispenserBlock.registerDispenseBehavior(ModItems.GOONADE.get(), goonadeBehavior);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientSetup.setup();
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        // do something that can only be done on the client
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        ModCommands.register(event.getServer().getCommandManager().getDispatcher());
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here

        }
    }
}
