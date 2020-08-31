package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.Config;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandCanSpread {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("canSpread")
                .requires(cs -> cs.hasPermissionLevel(0))
                .executes(ctx -> getCanSpread(ctx))
                .then(Commands.argument("canspread", BoolArgumentType.bool())
                        .executes(ctx -> setCanSpread(ctx, BoolArgumentType.getBool(ctx, "canspread"))));
    }

    public static int getCanSpread(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.canspread " + Config.CAN_SPREAD_ALL.get()), false);
        return 0;
    }

    public static int setCanSpread(CommandContext<CommandSource> context, boolean canSpread) throws CommandSyntaxException {
        Config.CAN_SPREAD_ALL.set(canSpread);
        getCanSpread(context);
        return 0;
    }
}
