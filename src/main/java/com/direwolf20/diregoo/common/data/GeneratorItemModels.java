package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class GeneratorItemModels extends ItemModelProvider {
    public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, DireGoo.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Our block items
        registerBlockModel(ModBlocks.GOO_BLOCK.get());
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