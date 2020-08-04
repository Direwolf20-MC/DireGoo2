package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class GeneratorBlockStates extends BlockStateProvider {
    public GeneratorBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, DireGoo.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Goo block
        buildCubeAll(ModBlocks.GOO_BLOCK.get());
    }

    private void buildCubeAll(Block block) {
        getVariantBuilder(block).forAllStates(state ->
                ConfiguredModel.builder().modelFile(cubeAll(block)).build()
        );
    }
}
