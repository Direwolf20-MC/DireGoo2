package com.direwolf20.diregoo.common.data;

import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static com.direwolf20.diregoo.common.items.ModItems.*;

public class GeneratorRecipes extends RecipeProvider {
    public GeneratorRecipes(DataGenerator generator) {
        super(generator);
    }

    /**
     * This is basically just a code version of the json file meaning you type less and generate more. To use
     * Tags use Tags and to specific normal Items use their Items class. A Criterion is what the game will
     * use to see if you can make that recipe. I've been pretty lazy and just done the higher tier ones
     * for now. Hopefully this should mean we write less json in the long run :D
     */
    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapelessRecipe(ANTI_GOO_DUST.get())
                .addIngredient(Tags.Items.DUSTS_REDSTONE)
                .addIngredient(GOO_RESIDUE.get())
                .addCriterion("has_residue", hasItem(GOO_RESIDUE.get()))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ANTI_GOO_PASTE.get())
                .addIngredient(ANTI_GOO_DUST.get())
                .addIngredient(Items.CLAY_BALL) //Balanced
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CORE_MELT.get())
                .key('a', ANTI_GOO_DUST.get())
                .key('g', Items.GLASS)
                .key('i', Tags.Items.INGOTS_IRON)
                .patternLine("igi")
                .patternLine("gag")
                .patternLine("igi")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(CORE_FREEZE.get())
                .key('a', ANTI_GOO_DUST.get())
                .key('g', Items.ICE)
                .key('i', Tags.Items.INGOTS_IRON)
                .patternLine("igi")
                .patternLine("gag")
                .patternLine("igi")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GOO_REMOVER.get())
                .key('r', Tags.Items.DUSTS_REDSTONE)
                .key('c', CORE_MELT.get())
                .key('g', Items.GLASS_PANE)
                .key('i', Tags.Items.INGOTS_IRON)
                .patternLine("iii")
                .patternLine("icg")
                .patternLine("r  ")
                .addCriterion("has_core", hasItem(CORE_MELT.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GOO_ZAPPER.get())
                .key('r', Tags.Items.DUSTS_REDSTONE)
                .key('s', GOO_SCANNER.get())
                .key('g', Items.GLASS_PANE)
                .key('n', Tags.Items.INGOTS_NETHERITE)
                .patternLine("nnn")
                .patternLine("nsg")
                .patternLine("r  ")
                .addCriterion("has_scanner", hasItem(GOO_SCANNER.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(FOCUS_T1.get())
                .key('p', Items.GLASS_PANE)
                .key('a', Items.GLASS)
                .key('g', ANTI_GOO_DUST.get())
                .patternLine("pgp")
                .patternLine("gag")
                .patternLine("pgp")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(FOCUS_T2.get())
                .key('p', Items.GLASS_PANE)
                .key('a', Tags.Items.GEMS_DIAMOND)
                .key('g', ANTI_GOO_DUST.get())
                .patternLine("pgp")
                .patternLine("gag")
                .patternLine("pgp")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(FOCUS_T3.get())
                .key('p', Items.GLASS_PANE)
                .key('a', Tags.Items.GEMS_EMERALD)
                .key('g', ANTI_GOO_DUST.get())
                .patternLine("pgp")
                .patternLine("gag")
                .patternLine("pgp")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(FOCUS_T4.get())
                .key('p', Items.GLASS_PANE)
                .key('a', Tags.Items.NETHER_STARS)
                .key('g', ANTI_GOO_DUST.get())
                .patternLine("pgp")
                .patternLine("gag")
                .patternLine("pgp")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(POWERAMP_T1.get())
                .key('p', Tags.Items.INGOTS_IRON)
                .key('r', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .key('a', Items.GLASS)
                .key('g', ANTI_GOO_DUST.get())
                .patternLine("pgp")
                .patternLine("rar")
                .patternLine("pgp")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(POWERAMP_T2.get())
                .key('p', Tags.Items.INGOTS_IRON)
                .key('r', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .key('a', Tags.Items.GEMS_DIAMOND)
                .key('g', ANTI_GOO_DUST.get())
                .patternLine("pgp")
                .patternLine("rar")
                .patternLine("pgp")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(POWERAMP_T3.get())
                .key('p', Tags.Items.INGOTS_IRON)
                .key('r', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .key('a', Tags.Items.GEMS_EMERALD)
                .key('g', ANTI_GOO_DUST.get())
                .patternLine("pgp")
                .patternLine("rar")
                .patternLine("pgp")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(POWERAMP_T4.get())
                .key('p', Tags.Items.INGOTS_IRON)
                .key('r', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .key('a', Tags.Items.NETHER_STARS)
                .key('g', ANTI_GOO_DUST.get())
                .patternLine("pgp")
                .patternLine("rar")
                .patternLine("pgp")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GOONADE.get(), 4)
                .key('c', CORE_MELT.get())
                .key('g', Items.GUNPOWDER)
                .key('i', Tags.Items.INGOTS_IRON)
                .patternLine("igi")
                .patternLine("gcg")
                .patternLine("igi")
                .addCriterion("has_core", hasItem(CORE_MELT.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GOONADE_FREEZE.get(), 4)
                .key('c', CORE_FREEZE.get())
                .key('g', Items.GUNPOWDER)
                .key('i', Tags.Items.INGOTS_IRON)
                .patternLine("igi")
                .patternLine("gcg")
                .patternLine("igi")
                .addCriterion("has_core_freeze", hasItem(CORE_FREEZE.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GOO_SCANNER.get(), 1)
                .key('a', ANTI_GOO_DUST.get())
                .key('e', Items.ENDER_EYE)
                .key('g', Items.GLASS)
                .patternLine("aga")
                .patternLine("geg")
                .patternLine("aga")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(GOO_BLOCK_POISON_ITEM.get())
                .addIngredient(Items.CLAY)
                .addIngredient(ANTI_GOO_DUST.get())
                .addIngredient(Items.FERMENTED_SPIDER_EYE)
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(TURRET_BLOCK_ITEM.get())
                .key('c', CORE_MELT.get())
                .key('r', Tags.Items.DUSTS_REDSTONE)
                .key('i', Tags.Items.INGOTS_IRON)
                .key('d', GOO_DETECTOR_ITEM.get())
                .patternLine("idi")
                .patternLine("ici")
                .patternLine("iri")
                .addCriterion("has_core_melt", hasItem(CORE_MELT.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ZAPPER_TURRET_ITEM.get())
                .key('d', Tags.Items.GEMS_DIAMOND)
                .key('r', Tags.Items.DUSTS_REDSTONE)
                .key('n', Tags.Items.INGOTS_NETHERITE)
                .key('a', ANTI_GOO_DUST.get())
                .key('s', GOO_SCANNER.get())
                .patternLine("nsn")
                .patternLine("ada")
                .patternLine("nrn")
                .addCriterion("has_scanner", hasItem(GOO_SCANNER.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ANTI_GOO_BEACON_ITEM.get())
                .key('d', Tags.Items.GEMS_DIAMOND)
                .key('i', Tags.Items.INGOTS_IRON)
                .key('a', ANTI_GOO_DUST.get())
                .patternLine("iai")
                .patternLine("ada")
                .patternLine("iai")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ANTI_GOO_FIELD_GEN_ITEM.get())
                .key('d', Items.DIAMOND_BLOCK)
                .key('r', Tags.Items.DUSTS_REDSTONE)
                .key('g', Tags.Items.INGOTS_GOLD)
                .key('a', ANTI_GOO_DUST.get())
                .patternLine("gag")
                .patternLine("ada")
                .patternLine("grg")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(GOO_DETECTOR_ITEM.get())
                .key('s', Tags.Items.STONE)
                .key('r', Items.REDSTONE_TORCH)
                .key('a', ANTI_GOO_DUST.get())
                .patternLine(" r ")
                .patternLine("rar")
                .patternLine("sss")
                .addCriterion("has_agd", hasItem(ANTI_GOO_DUST.get()))
                .build(consumer);
    }
}
