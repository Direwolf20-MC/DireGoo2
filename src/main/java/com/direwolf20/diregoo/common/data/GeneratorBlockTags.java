package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;

import static net.minecraft.tags.BlockTags.makeWrapperTag;


public class GeneratorBlockTags extends BlockTagsProvider {

    public static final ITag.INamedTag<Block> GOORESISTANT = makeWrapperTag("diregoo:gooresistant");

    public GeneratorBlockTags(DataGenerator generatorIn, ExistingFileHelper exFileHelper) {
        super(generatorIn, DireGoo.MOD_ID, exFileHelper);
    }

    @Override
    public void registerTags() {
        this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.DOORS);
        this.getOrCreateBuilder(GOORESISTANT).addTag(BlockTags.BEDS);
        this.getOrCreateBuilder(GOORESISTANT).add(Blocks.PISTON, Blocks.PISTON_HEAD, Blocks.STICKY_PISTON, Blocks.MOVING_PISTON);

    }

    /**
     * Resolves a Path for the location to save the given tag.
     */
    @Override
    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    @Override
    public String getName() {
        return "Block Tags";
    }
}
