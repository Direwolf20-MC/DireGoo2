package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    /**
     * Deferred Registers for the our Main class to load.
     */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DireGoo.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, DireGoo.MOD_ID);

    /**
     * Register our blocks to the above registers to be loaded when the mod is initialized
     */
    public static final RegistryObject<Block> GOO_BLOCK = BLOCKS.register("gooblock", GooBlock::new);

    /**
     * TileEntity Registers to the above deferred registers to be loaded in from the mods main class.
     */
}
