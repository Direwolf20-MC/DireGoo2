package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    // The item group is the creative tab it will go into.
    public static final Item.Properties ITEM_GROUP = new Item.Properties().group(DireGoo.itemGroup);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DireGoo.MOD_ID);


    // Block items
    public static final RegistryObject<Item> GOO_BLOCK_ITEM = ITEMS.register("gooblock", () -> new BlockItem(ModBlocks.GOO_BLOCK.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GOO_BLOCK_TERRAIN_ITEM = ITEMS.register("gooblockterrain", () -> new BlockItem(ModBlocks.GOO_BLOCK_TERRAIN.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GOO_BLOCK_BURST_ITEM = ITEMS.register("gooblockburst", () -> new BlockItem(ModBlocks.GOO_BLOCK_BURST.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GOO_BLOCK_POISON_ITEM = ITEMS.register("gooblockpoison", () -> new BlockItem(ModBlocks.GOO_BLOCK_POISON.get(), ITEM_GROUP));
    public static final RegistryObject<Item> TURRET_BLOCK_ITEM = ITEMS.register("turretblock", () -> new BlockItem(ModBlocks.TURRET_BLOCK.get(), ITEM_GROUP));
    public static final RegistryObject<Item> ANTI_GOO_FIELD_GEN_ITEM = ITEMS.register("antigoofieldgen", () -> new BlockItem(ModBlocks.ANTI_GOO_FIELD_GEN.get(), ITEM_GROUP));


    // Items
    public static final RegistryObject<Item> GOO_REMOVER = ITEMS.register("gooremover", GooRemover::new);
    public static final RegistryObject<Item> GOONADE = ITEMS.register("goonade", Goonade::new);
    public static final RegistryObject<Item> ANTI_GOO_DUST = ITEMS.register("antigoodust", AntigooDust::new);
    public static final RegistryObject<Item> GOO_SCANNER = ITEMS.register("gooscanner", GooScanner::new);

}
