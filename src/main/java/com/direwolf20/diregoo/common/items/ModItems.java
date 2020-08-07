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


    // Items
    public static final RegistryObject<Item> GOO_REMOVER = ITEMS.register("gooremover", GooRemover::new);
    public static final RegistryObject<Item> GOONADE = ITEMS.register("goonade", Goonade::new);

}
