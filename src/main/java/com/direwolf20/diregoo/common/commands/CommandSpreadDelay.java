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

public class CommandSpreadDelay implements Command<CommandSource> {

    private static final CommandSpreadDelay CMD = new CommandSpreadDelay();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("spreadDelay")
                .requires(cs -> cs.hasPermissionLevel(0))
                .then(Commands.argument("spreaddelay", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int spreadDelay = IntegerArgumentType.getInteger(context, "spreaddelay");
        Config.SPREAD_TICK_DELAY.set(spreadDelay);
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.spreaddelay " + Config.SPREAD_TICK_DELAY.get()), false);
        return 0;
    }
}
