package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.common.worldsave.BlockSave;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

public class CommandGooDeathEvent {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("gooDeathEvent")
                .requires(cs -> cs.hasPermissionLevel(0))
                .executes(ctx -> getDeathEvent(ctx))
                .then(Commands.argument("gooDeathEvent", BoolArgumentType.bool())
                        .executes(ctx -> setDeathEvent(ctx, BoolArgumentType.getBool(ctx, "gooDeathEvent"))));
    }

    public static int getDeathEvent(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        BlockSave blockSave = BlockSave.get(world);
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.gooDeathEvent " + blockSave.getGooDeathEvent()), false);
        return 0;
    }

    public static int setDeathEvent(CommandContext<CommandSource> context, boolean gooDeath) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        BlockSave blockSave = BlockSave.get(world);
        blockSave.setGooDeathEvent(gooDeath);
        getDeathEvent(context);
        return 0;
    }
}
