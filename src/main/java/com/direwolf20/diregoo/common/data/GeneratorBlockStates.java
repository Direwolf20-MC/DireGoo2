package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
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

        /*Block gooDetectorBlock = ModBlocks.GOO_DETECTOR.get();
        String gooDetector = Objects.requireNonNull(gooDetectorBlock.getRegistryName()).getPath();
        getVariantBuilder(ModBlocks.GOO_DETECTOR.get())
                .partialState().with(POWERED, false).setModels(new ConfiguredModel(models().carpet(gooDetector, blockTexture(gooDetectorBlock))))
                .partialState().with(POWERED, true).setModels(new ConfiguredModel(models().carpet("goodetector_on", modLoc("block/goodetector_on")))
        );*/
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
