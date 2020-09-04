package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.common.worldsave.BlockSave;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CommandShowGrowthCounts {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("showGrowthCounts")
                .requires(cs -> cs.hasPermissionLevel(0))
                .executes(ctx -> outputEntityCount(ctx));
    }

    public static int outputEntityCount(CommandContext<CommandSource> context) throws CommandSyntaxException {
        World world = context.getSource().getWorld();
        BlockSave blockSave = BlockSave.get(world);
        LinkedHashMap<Long, Integer> blockChangeCounter = blockSave.getBlockChangeCounter();
        for (Map.Entry<Long, Integer> entry : blockChangeCounter.entrySet()) {
            context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.blockChangeCounter " + (world.getGameTime() - entry.getKey()) + ": " + entry.getValue()), false);
        }
        LinkedHashMap<Long, Set<ChunkPos>> chunkChangeCounter = blockSave.getChunkChangeCounter();
        for (Map.Entry<Long, Set<ChunkPos>> entry : chunkChangeCounter.entrySet()) {
            context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.chunkChangeCounter " + (world.getGameTime() - entry.getKey()) + ": " + blockSave.getChunkChangesThisTick(entry.getKey())), false);
        }
        //context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.blockChangeCounter " + blockSave.getBlockChangeThisTick(world.getGameTime()-1)), false);
        //context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.chunkChangeCounter " + blockSave.getChunkChangesThisTick(world.getGameTime()-1) ), false);
        return 0;
    }

}
