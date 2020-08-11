package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.Config;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandPlayerSpreadRange implements Command<CommandSource> {

    private static final CommandPlayerSpreadRange CMD = new CommandPlayerSpreadRange();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("spreadRange")
                .requires(cs -> cs.hasPermissionLevel(0))
                .then(Commands.argument("spreadrange", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int spreadDelay = IntegerArgumentType.getInteger(context, "spreadrange");
        Config.PLAYER_SPREAD_RANGE.set(spreadDelay);
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.spreadrange " + Config.PLAYER_SPREAD_RANGE.get()), false);
        return 0;
    }
}
