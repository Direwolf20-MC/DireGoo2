package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.Config;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandCanSpread implements Command<CommandSource> {

    private static final CommandCanSpread CMD = new CommandCanSpread();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("canSpread")
                .requires(cs -> cs.hasPermissionLevel(0))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Config.CAN_SPREAD_ALL.set(!Config.CAN_SPREAD_ALL.get());
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.canspread " + Config.CAN_SPREAD_ALL.get()), false);
        return 0;
    }
}
