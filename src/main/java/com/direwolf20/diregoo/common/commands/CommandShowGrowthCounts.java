package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.common.events.ServerEvents;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CommandShowGrowthCounts {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("showGrowthCounts")
                .requires(cs -> cs.hasPermissionLevel(0))
                .executes(ctx -> outputEntityCount(ctx));
    }

    public static int outputEntityCount(CommandContext<CommandSource> context) throws CommandSyntaxException {
        World world = context.getSource().getWorld();
        BlockSave blockSave = BlockSave.get(world);
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.blockChangeSize", ServerEvents.blockChangeSize()), false);
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.chunkChangeQueueSize", ServerEvents.chunkQueueSize()), false);
        return 0;
    }

}
