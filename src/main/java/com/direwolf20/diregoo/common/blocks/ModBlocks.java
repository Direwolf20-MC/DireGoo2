package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.AntiGooFieldGenContainer;
import com.direwolf20.diregoo.common.container.TurretContainer;
import com.direwolf20.diregoo.common.tiles.AntiGooFieldGenTileEntity;
import com.direwolf20.diregoo.common.tiles.TurretBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    /**
     * Deferred Registers for the our Main class to load.
     */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DireGoo.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, DireGoo.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DireGoo.MOD_ID);

    /**
     * Register our blocks to the above registers to be loaded when the mod is initialized
     */
    public static final RegistryObject<Block> GOO_BLOCK = BLOCKS.register("gooblock", GooBlock::new);
    public static final RegistryObject<Block> GOO_BLOCK_TERRAIN = BLOCKS.register("gooblockterrain", GooBlockTerrain::new);
    public static final RegistryObject<Block> GOO_BLOCK_BURST = BLOCKS.register("gooblockburst", GooBlockBurst::new);
    public static final RegistryObject<Block> GOO_BLOCK_POISON = BLOCKS.register("gooblockpoison", GooBlockPoison::new);
    public static final RegistryObject<Block> TURRET_BLOCK = BLOCKS.register("turretblock", TurretBlock::new);
    public static final RegistryObject<Block> ANTI_GOO_FIELD_GEN = BLOCKS.register("antigoofieldgen", AntiGooFieldGen::new);
    public static final RegistryObject<Block> GOO_DETECTOR = BLOCKS.register("goodetector", GooDetector::new);

    /**
     * TileEntity Registers to the above deferred registers to be loaded in from the mods main class.
     */
    public static final RegistryObject<TileEntityType<TurretBlockTileEntity>> TURRETBLOCK_TILE =
            TILES_ENTITIES.register("turretblock", () -> TileEntityType.Builder.create(TurretBlockTileEntity::new, ModBlocks.TURRET_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<AntiGooFieldGenTileEntity>> ANTI_GOO_FIELD_GEN_TILE =
            TILES_ENTITIES.register("antigoofieldgen", () -> TileEntityType.Builder.create(AntiGooFieldGenTileEntity::new, ModBlocks.ANTI_GOO_FIELD_GEN.get()).build(null));

    /**
     * Containers
     */
    public static final RegistryObject<ContainerType<AntiGooFieldGenContainer>> ANTI_GOO_FIELD_GEN_CONTAINER = CONTAINERS.register("anti_goo_field_gen_container", () -> IForgeContainerType.create(AntiGooFieldGenContainer::new));
    public static final RegistryObject<ContainerType<TurretContainer>> TURRET_CONTAINER = CONTAINERS.register("turret_container", () -> IForgeContainerType.create(TurretContainer::new));


}
