package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.Config;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandSpreadDelay {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("spreadDelay")
                .requires(cs -> cs.hasPermissionLevel(2))
                .executes(ctx -> getRange(ctx))
                .then(Commands.argument("spreaddelay", IntegerArgumentType.integer(-1, Integer.MAX_VALUE))
                        .executes(ctx -> setRange(ctx, IntegerArgumentType.getInteger(ctx, "spreaddelay"))));
    }

    public static int getRange(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.spreaddelay", Config.SPREAD_TICK_DELAY.get()), false);
        return 0;
    }

    public static int setRange(CommandContext<CommandSource> context, int range) throws CommandSyntaxException {
        Config.SPREAD_TICK_DELAY.set(range);
        getRange(context);
        return 0;
    }
}
