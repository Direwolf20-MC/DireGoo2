package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class GeneratorItemModels extends ItemModelProvider {
    public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, DireGoo.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Our block items
        registerBlockModel(ModBlocks.GOO_BLOCK.get());
        registerBlockModel(ModBlocks.GOO_BLOCK_TERRAIN.get());
        registerBlockModel(ModBlocks.GOO_BLOCK_BURST.get());
        registerBlockModel(ModBlocks.GOO_BLOCK_POISON.get());
        registerBlockModel(ModBlocks.TURRET_BLOCK.get());
        registerBlockModel(ModBlocks.ZAPPER_TURRET_BLOCK.get());
        registerBlockModel(ModBlocks.ANTI_GOO_FIELD_GEN.get());
        //registerBlockModel(ModBlocks.GOO_DETECTOR.get());

        String path = ModBlocks.GOO_DETECTOR.get().getRegistryName().getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path))).transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                .rotation(45, 0, 90)
                .translation(0, 0, 0)
                .scale(.5f)
                .end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
                .rotation(45, 0, 90)
                .translation(0, 0, 0)
                .scale(.5f)
                .end()
                .transform(ModelBuilder.Perspective.GUI)
                .rotation(90, 0, 0)
                .translation(0, 0, 0)
                .scale(1f)
                .end()
                .end();
        ;

        //Our Item Models
        String gooRemover = ModItems.GOO_REMOVER.get().getRegistryName().getPath();
        singleTexture(gooRemover, mcLoc("item/handheld"), "layer0", modLoc("item/" + gooRemover)).transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                .rotation(0, 80, 0)
                .translation(0, 0, 0)
                .scale(.5f)
                .end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
                .rotation(0, 80, 0)
                .translation(6, 0, -7)
                .scale(1f)
                .end()
                .end();

        String gooZapper = ModItems.GOO_ZAPPER.get().getRegistryName().getPath();
        singleTexture(gooZapper, mcLoc("item/handheld"), "layer0", modLoc("item/" + gooZapper)).transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                .rotation(0, 80, 0)
                .translation(0, 0, 0)
                .scale(.5f)
                .end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
                .rotation(0, 80, 0)
                .translation(6, 0, -7)
                .scale(1f)
                .end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT)
                .rotation(0, -80, 0)
                .translation(6, 0, -7)
                .scale(1f)
                .end()
                .end();

        String gooFreezer = ModItems.GOO_FREEZER.get().getRegistryName().getPath();
        singleTexture(gooFreezer, mcLoc("item/handheld"), "layer0", modLoc("item/" + gooFreezer)).transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                .rotation(0, 80, 0)
                .translation(0, 0, 0)
                .scale(.5f)
                .end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
                .rotation(0, 80, 0)
                .translation(6, 0, -7)
                .scale(1f)
                .end()
                .transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT)
                .rotation(0, -80, 0)
                .translation(6, 0, -7)
                .scale(1f)
                .end()
                .end();

        for (RegistryObject<Item> item : ModItems.BASICITEMS.getEntries()) {
            registerBasicItem(item.get());
        }
        /*
        String goonadefreeze = ModItems.GOONADE_FREEZE.get().getRegistryName().getPath();
        singleTexture(goonadefreeze, mcLoc("item/handheld"), "layer0", modLoc("item/" + goonadefreeze));

        String antigoopaste = ModItems.ANTI_GOO_PASTE.get().getRegistryName().getPath();
        singleTexture(antigoopaste, mcLoc("item/handheld"), "layer0", modLoc("item/" + antigoopaste));

        String gooscanner = ModItems.GOO_SCANNER.get().getRegistryName().getPath();
        singleTexture(gooscanner, mcLoc("item/handheld"), "layer0", modLoc("item/" + gooscanner));

        String antigooDust = ModItems.ANTI_GOO_DUST.get().getRegistryName().getPath();
        singleTexture(antigooDust, mcLoc("item/handheld"), "layer0", modLoc("item/" + antigooDust));

        String gooresidue = ModItems.GOO_RESIDUE.get().getRegistryName().getPath();
        singleTexture(gooresidue, mcLoc("item/handheld"), "layer0", modLoc("item/" + gooresidue));*/
    }

    private void registerBlockModel(Block block) {
        String path = block.getRegistryName().getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
    }

    private void registerBasicItem(Item item) {
        String path = item.getRegistryName().getPath();
        singleTexture(path, mcLoc("item/handheld"), "layer0", modLoc("item/" + path));
    }

    @Override
    public String getName() {
        return "Item Models";
    }
}