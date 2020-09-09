package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;

import static net.minecraft.tags.BlockTags.makeWrapperTag;


public class GeneratorBlockTags extends TagsProvider<Block> {

    public static final ITag.INamedTag<Block> GOORESISTANT = makeWrapperTag("diregoo:gooresistant");

    public GeneratorBlockTags(DataGenerator generatorIn) {
        super(generatorIn, Registry.BLOCK);
    }

    @Override
    public void registerTags() {
        //Todo: Add in forge 1.16.2
        //this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.BEDS);
        //this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.DOORS);
        this.getOrCreateBuilder(GOORESISTANT).add(Blocks.PISTON, Blocks.PISTON_HEAD, Blocks.STICKY_PISTON, Blocks.MOVING_PISTON);

    }

    /**
     * Resolves a Path for the location to save the given tag.
     */
    @Override
    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + DireGoo.MOD_ID + "/tags/blocks/" + id.getPath() + ".json");
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    @Override
    public String getName() {
        return "Block Tags";
    }
}
