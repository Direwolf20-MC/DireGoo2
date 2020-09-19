package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.items.zapperupgrades.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    // The item group is the creative tab it will go into.
    public static final Item.Properties ITEM_GROUP = new Item.Properties().group(DireGoo.itemGroup);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DireGoo.MOD_ID);
    public static final DeferredRegister<Item> BASICITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DireGoo.MOD_ID);


    // Block items
    public static final RegistryObject<Item> GOO_BLOCK_ITEM = ITEMS.register("gooblock", () -> new BlockItem(ModBlocks.GOO_BLOCK.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GOO_BLOCK_TERRAIN_ITEM = ITEMS.register("gooblockterrain", () -> new BlockItem(ModBlocks.GOO_BLOCK_TERRAIN.get(), ITEM_GROUP));
    //public static final RegistryObject<Item> GOO_BLOCK_BURST_ITEM = ITEMS.register("gooblockburst", () -> new BlockItem(ModBlocks.GOO_BLOCK_BURST.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GNT_BLOCK_T1_ITEM = ITEMS.register("gntblockt1", () -> new BlockItem(ModBlocks.GNT_BLOCK_T1.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GNT_BLOCK_T2_ITEM = ITEMS.register("gntblockt2", () -> new BlockItem(ModBlocks.GNT_BLOCK_T2.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GNT_BLOCK_T3_ITEM = ITEMS.register("gntblockt3", () -> new BlockItem(ModBlocks.GNT_BLOCK_T3.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GNT_BLOCK_T4_ITEM = ITEMS.register("gntblockt4", () -> new BlockItem(ModBlocks.GNT_BLOCK_T4.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GOO_BLOCK_POISON_ITEM = ITEMS.register("gooblockpoison", () -> new BlockItem(ModBlocks.GOO_BLOCK_POISON.get(), ITEM_GROUP));
    public static final RegistryObject<Item> TURRET_BLOCK_ITEM = ITEMS.register("turretblock", () -> new BlockItem(ModBlocks.TURRET_BLOCK.get(), ITEM_GROUP));
    public static final RegistryObject<Item> ZAPPER_TURRET_ITEM = ITEMS.register("zapperturretblock", () -> new BlockItem(ModBlocks.ZAPPER_TURRET_BLOCK.get(), ITEM_GROUP));
    public static final RegistryObject<Item> ANTI_GOO_FIELD_GEN_ITEM = ITEMS.register("antigoofieldgen", () -> new BlockItem(ModBlocks.ANTI_GOO_FIELD_GEN.get(), ITEM_GROUP));
    public static final RegistryObject<Item> ANTI_GOO_BEACON_ITEM = ITEMS.register("antigoobeacon", () -> new BlockItem(ModBlocks.ANTI_GOO_BEACON.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GOOLIMINATION_FIELD_GEN_ITEM = ITEMS.register("gooliminationfieldgen", () -> new BlockItem(ModBlocks.GOOLIMINATIONFIELDGEN.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GOO_DETECTOR_ITEM = ITEMS.register("goodetector", () -> new BlockItem(ModBlocks.GOO_DETECTOR.get(), ITEM_GROUP));


    // Items
    public static final RegistryObject<Item> GOO_RESIDUE = BASICITEMS.register("gooresidue", GooResidue::new);
    public static final RegistryObject<Item> ANTI_GOO_DUST = BASICITEMS.register("antigoodust", AntiGooDust::new);
    public static final RegistryObject<Item> GOO_REMOVER = ITEMS.register("gooremover", GooRemover::new);
    public static final RegistryObject<Item> GOO_ZAPPER = ITEMS.register("goozapper", GooZapper::new);
    public static final RegistryObject<Item> GOONADE = BASICITEMS.register("goonade", Goonade::new);
    public static final RegistryObject<Item> GOONADE_FREEZE = BASICITEMS.register("goonadefreeze", GoonadeFreeze::new);
    public static final RegistryObject<Item> ANTI_GOO_PASTE = BASICITEMS.register("antigoopaste", AntigooPaste::new);
    public static final RegistryObject<Item> GOO_SCANNER = BASICITEMS.register("gooscanner", GooScanner::new);
    public static final RegistryObject<Item> CORE_FREEZE = BASICITEMS.register("corefreeze", CoreFreeze::new);
    public static final RegistryObject<Item> CORE_MELT = BASICITEMS.register("coremelt", CoreMelt::new);
    public static final RegistryObject<Item> FOCUS_T1 = BASICITEMS.register("focust1", FocusT1::new);
    public static final RegistryObject<Item> FOCUS_T2 = BASICITEMS.register("focust2", FocusT2::new);
    public static final RegistryObject<Item> FOCUS_T3 = BASICITEMS.register("focust3", FocusT3::new);
    public static final RegistryObject<Item> FOCUS_T4 = BASICITEMS.register("focust4", FocusT4::new);
    public static final RegistryObject<Item> POWERAMP_T1 = BASICITEMS.register("powerampt1", PowerAmpT1::new);
    public static final RegistryObject<Item> POWERAMP_T2 = BASICITEMS.register("powerampt2", PowerAmpT2::new);
    public static final RegistryObject<Item> POWERAMP_T3 = BASICITEMS.register("powerampt3", PowerAmpT3::new);
    public static final RegistryObject<Item> POWERAMP_T4 = BASICITEMS.register("powerampt4", PowerAmpT4::new);

}
