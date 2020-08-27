package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.blocks.GooRender;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import java.util.Objects;

public class GeneratorBlockStates extends BlockStateProvider {
    public GeneratorBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, DireGoo.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Goo block
        buildCubeGoo(ModBlocks.GOO_BLOCK.get());
        buildCubeGoo(ModBlocks.GOO_BLOCK_TERRAIN.get());
        buildCubeGoo(ModBlocks.GOO_BLOCK_BURST.get());

        buildCubeAll(ModBlocks.GOO_BLOCK_POISON.get());
        buildCubeAll(ModBlocks.TURRET_BLOCK.get());
        buildCubeAll(ModBlocks.ANTI_GOO_FIELD_GEN.get());

        Block gooRender = ModBlocks.GOO_RENDER.get();
        String gooRenderPath = Objects.requireNonNull(gooRender.getRegistryName()).getPath();
        getVariantBuilder(ModBlocks.GOO_RENDER.get())
                .partialState().with(GooRender.GROWTH, 1).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "1", modLoc("block/goorender1"))))
                .partialState().with(GooRender.GROWTH, 2).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "2", modLoc("block/goorender2"))))
                .partialState().with(GooRender.GROWTH, 3).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "3", modLoc("block/goorender3"))))
                .partialState().with(GooRender.GROWTH, 4).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "4", modLoc("block/goorender4"))))
                .partialState().with(GooRender.GROWTH, 5).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "5", modLoc("block/goorender5"))))
                .partialState().with(GooRender.GROWTH, 6).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "6", modLoc("block/goorender6"))))
                .partialState().with(GooRender.GROWTH, 7).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "7", modLoc("block/goorender7"))))
                .partialState().with(GooRender.GROWTH, 8).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "8", modLoc("block/goorender8"))))
                .partialState().with(GooRender.GROWTH, 9).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + "9", modLoc("block/goorender9")))
        );
    }

    private void buildCubeAll(Block block) {
        getVariantBuilder(block).forAllStates(state ->
                ConfiguredModel.builder().modelFile(cubeAll(block)).build()
        );
    }

    private void buildCubeGoo(Block block) {
        String blockName = Objects.requireNonNull(block.getRegistryName()).getPath();
        getVariantBuilder(block)
                //No State
                .partialState().with(GooBase.FROZEN, 0).setModels(new ConfiguredModel(models().cubeAll(blockName, blockTexture(block))))
                .partialState().with(GooBase.FROZEN, 1).setModels(new ConfiguredModel(models().cubeAll("gooblockfrozen", modLoc("block/gooblockfrozen"))))
                .partialState().with(GooBase.FROZEN, 2).setModels(new ConfiguredModel(models().cubeAll("gooblockfrozen", modLoc("block/gooblockfrozen"))))
                .partialState().with(GooBase.FROZEN, 3).setModels(new ConfiguredModel(models().cubeAll("gooblockfrozen", modLoc("block/gooblockfrozen")))
        );
    }
}
