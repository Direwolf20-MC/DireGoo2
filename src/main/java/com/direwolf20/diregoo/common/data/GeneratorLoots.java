package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.items.ModItems;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GeneratorLoots extends LootTableProvider {
    public GeneratorLoots(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(Pair.of(Blocks::new, LootParameterSets.BLOCK));
    }

    private static class Blocks extends BlockLootTables {
        @Override
        protected void addTables() {
            LootPool.Builder builder = LootPool.builder()
                    .name(ModBlocks.GOO_BLOCK.get().getRegistryName().toString())
                    .rolls(ConstantRange.of(1))
                    .acceptCondition(SurvivesExplosion.builder())
                    .addEntry(ItemLootEntry.builder(ModItems.GOO_RESIDUE.get())
                    );

            this.registerLootTable(ModBlocks.GOO_BLOCK.get(), LootTable.builder().addLootPool(builder));

            LootPool.Builder builder2 = LootPool.builder()
                    .name(ModBlocks.GOO_BLOCK_TERRAIN.get().getRegistryName().toString())
                    .rolls(ConstantRange.of(1))
                    .acceptCondition(SurvivesExplosion.builder())
                    .addEntry(ItemLootEntry.builder(ModItems.GOO_RESIDUE.get())
                    );

            //this.registerLootTable(ModBlocks.GOO_BLOCK_BURST.get(), LootTable.builder().addLootPool(builder));

            /*LootPool.Builder builder3 = LootPool.builder()
                    .name(ModBlocks.GOO_BLOCK_BURST.get().getRegistryName().toString())
                    .rolls(ConstantRange.of(1))
                    .acceptCondition(SurvivesExplosion.builder())
                    .addEntry(ItemLootEntry.builder(ModItems.GOO_RESIDUE.get())
                    );*/

            this.registerLootTable(ModBlocks.GOO_BLOCK.get(), LootTable.builder().addLootPool(builder));
            this.registerLootTable(ModBlocks.GOO_BLOCK_TERRAIN.get(), LootTable.builder().addLootPool(builder2));
            //this.registerLootTable(ModBlocks.GOO_BLOCK_BURST.get(), LootTable.builder().addLootPool(builder3));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ImmutableList.of(ModBlocks.GOO_BLOCK.get(), /*ModBlocks.GOO_BLOCK_BURST.get(),*/ ModBlocks.GOO_BLOCK_TERRAIN.get());
        }
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((name, table) -> LootTableManager.func_227508_a_(validationtracker, name, table));
    }
}
