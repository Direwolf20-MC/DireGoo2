package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.blocks.goospreadblocks.GooRender;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

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
        //buildCubeGoo(ModBlocks.GOO_BLOCK_BURST.get());

        buildCubeAll(ModBlocks.GOO_BLOCK_POISON.get());
//        buildCubeAll(ModBlocks.ANTI_GOO_FIELD_GEN.get()); // Rid: Removed due to custom model in Main package
//        buildCubeAll(ModBlocks.GOOLIMINATIONFIELDGEN.get()); // Rid: Removed due to custom model in Main package
//        buildCubeAll(ModBlocks.ANTI_GOO_BEACON.get()); // Rid: Turned to a simpleBlock below
//        buildCubeAll(ModBlocks.TURRET_BLOCK.get()); // Rid: Turned to a simpleBlock below

        simpleBlock(ModBlocks.ANTI_GOO_BEACON.get(), models().cubeBottomTop(
                Objects.requireNonNull(ModBlocks.ANTI_GOO_BEACON.get().getRegistryName()).getPath(),
                modLoc("block/antigoobeacon_side"),
                modLoc("block/base_bottom"),
                modLoc("block/antigoobeacon_top_on")
        ));

        simpleBlock(ModBlocks.TURRET_BLOCK.get(), models().cubeBottomTop(
                Objects.requireNonNull(ModBlocks.TURRET_BLOCK.get().getRegistryName()).getPath(),
                modLoc("block/turret_side"),
                modLoc("block/turret_bottom"),
                modLoc("block/turret_top")
        ));

        directionalBlock(ModBlocks.ZAPPER_TURRET_BLOCK.get(), models().orientableWithBottom(
                Objects.requireNonNull(ModBlocks.ZAPPER_TURRET_BLOCK.get().getRegistryName()).getPath(),
                modLoc("block/zapperturret_side"),
                modLoc("block/zapperturret_side"),
                modLoc("block/zapperturret_bottom"),
                modLoc("block/zapperturret_top")
        ));

        simpleBlock(ModBlocks.GNT_BLOCK_T1.get(), models().cubeBottomTop(
                Objects.requireNonNull(ModBlocks.GNT_BLOCK_T1.get().getRegistryName()).getPath(),
                modLoc("block/gnt_basic_side"),
                modLoc("block/gnt_bottom"),
                modLoc("block/gnt_top")
        ));
        simpleBlock(ModBlocks.GNT_BLOCK_T2.get(), models().cubeBottomTop(
                Objects.requireNonNull(ModBlocks.GNT_BLOCK_T2.get().getRegistryName()).getPath(),
                modLoc("block/gnt_improved_side"),
                modLoc("block/gnt_bottom"),
                modLoc("block/gnt_top")
        ));
        simpleBlock(ModBlocks.GNT_BLOCK_T3.get(), models().cubeBottomTop(
                Objects.requireNonNull(ModBlocks.GNT_BLOCK_T3.get().getRegistryName()).getPath(),
                modLoc("block/gnt_powerful_side"),
                modLoc("block/gnt_bottom"),
                modLoc("block/gnt_top")
        ));
        simpleBlock(ModBlocks.GNT_BLOCK_T4.get(), models().cubeBottomTop(
                Objects.requireNonNull(ModBlocks.GNT_BLOCK_T4.get().getRegistryName()).getPath(),
                modLoc("block/gnt_extreme_side"),
                modLoc("block/gnt_bottom"),
                modLoc("block/gnt_top")
        ));

        Block gooRender = ModBlocks.GOO_RENDER.get();
        String gooRenderPath = Objects.requireNonNull(gooRender.getRegistryName()).getPath();
        VariantBlockStateBuilder gooRenderBuilder = getVariantBuilder(ModBlocks.GOO_RENDER.get());

        for (int i = 1; i <= 9; i++) {
            gooRenderBuilder.partialState().with(GooRender.GROWTH, i).setModels(new ConfiguredModel(models().cubeAll(gooRenderPath + i, modLoc("block/goorender" + i))));
        }

        Block gooTerrainRender = ModBlocks.GOO_RENDER_TERRAIN.get();
        String gooTerrainRenderPath = Objects.requireNonNull(gooTerrainRender.getRegistryName()).getPath();
        VariantBlockStateBuilder gooTerrainRenderBuilder = getVariantBuilder(ModBlocks.GOO_RENDER_TERRAIN.get());

        for (int i = 1; i <= 9; i++) {
            gooTerrainRenderBuilder.partialState().with(GooRender.GROWTH, i).setModels(new ConfiguredModel(models().cubeAll(gooTerrainRenderPath + i, modLoc("block/goorenderterrain" + i))));
        }

        /*Block gooBurstRender = ModBlocks.GOO_RENDER_BURST.get();
        String gooBurstRenderPath = Objects.requireNonNull(gooBurstRender.getRegistryName()).getPath();
        VariantBlockStateBuilder gooBurstRenderBuilder = getVariantBuilder(ModBlocks.GOO_RENDER_BURST.get());

        for (int i = 1; i <= 9; i++) {
            gooBurstRenderBuilder.partialState().with(GooRender.GROWTH, i).setModels(new ConfiguredModel(models().cubeAll(gooBurstRenderPath + i, modLoc("block/goorenderburst" + i))));
        }*/
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
