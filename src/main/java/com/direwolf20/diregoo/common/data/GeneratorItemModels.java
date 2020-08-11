package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class GeneratorItemModels extends ItemModelProvider {
    public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, DireGoo.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Our block items
        registerBlockModel(ModBlocks.GOO_BLOCK.get());
        registerBlockModel(ModBlocks.TURRET_BLOCK.get());

        //Our Item Models
        String path = ModItems.GOO_REMOVER.get().getRegistryName().getPath();
        singleTexture(path, mcLoc("item/handheld"), "layer0", modLoc("item/" + path)).transforms()
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

        String goopath = ModItems.GOONADE.get().getRegistryName().getPath();
        singleTexture(goopath, mcLoc("item/handheld"), "layer0", modLoc("item/" + goopath));

        String antigoopath = ModItems.ANTI_GOO_DUST.get().getRegistryName().getPath();
        singleTexture(antigoopath, mcLoc("item/handheld"), "layer0", modLoc("item/" + antigoopath));
    }

    private void registerBlockModel(Block block) {
        String path = block.getRegistryName().getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
    }

    @Override
    public String getName() {
        return "Item Models";
    }
}