package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.Config;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandAnimateSpread {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("animateSpread")
                .requires(cs -> cs.hasPermissionLevel(2))
                .executes(ctx -> getCanSpread(ctx))
                .then(Commands.argument("animateSpread", BoolArgumentType.bool())
                        .executes(ctx -> setCanSpread(ctx, BoolArgumentType.getBool(ctx, "animateSpread"))));
    }

    public static int getCanSpread(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.animatespread", Config.ANIMATE_SPREAD.get()), false);
        return 0;
    }

    public static int setCanSpread(CommandContext<CommandSource> context, boolean canSpread) throws CommandSyntaxException {
        Config.ANIMATE_SPREAD.set(canSpread);
        getCanSpread(context);
        return 0;
    }
}
