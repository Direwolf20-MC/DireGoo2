package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.Config;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandPlayerSpreadRange {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("spreadRange")
                .requires(cs -> cs.hasPermissionLevel(0))
                .executes(ctx -> getRange(ctx))
                .then(Commands.argument("spreadrange", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                        .executes(ctx -> setRange(ctx, IntegerArgumentType.getInteger(ctx, "spreadrange"))));
    }

    public static int getRange(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.spreadrange " + Config.PLAYER_SPREAD_RANGE.get()), false);
        return 0;
    }

    public static int setRange(CommandContext<CommandSource> context, int range) throws CommandSyntaxException {
        Config.PLAYER_SPREAD_RANGE.set(range);
        getRange(context);
        return 0;
    }
}
