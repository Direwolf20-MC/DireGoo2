package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.DireGoo;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> cmdTut = dispatcher.register(
                Commands.literal(DireGoo.MOD_ID)
                        .then(CommandCanSpread.register(dispatcher))
                //.then(CommandTpDim.register(dispatcher))
                //.then(CommandSpawner.register(dispatcher))
        );

        dispatcher.register(Commands.literal("diregoo").redirect(cmdTut));
    }
}
