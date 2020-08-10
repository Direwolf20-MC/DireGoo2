package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.common.blocks.GooBase;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.stream.Collectors;

public class CommandClearGoo implements Command<CommandSource> {

    private static final CommandClearGoo CMD = new CommandClearGoo();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("ClearGoo")
                .requires(cs -> cs.hasPermissionLevel(0))
                .then(Commands.argument("range", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int range = IntegerArgumentType.getInteger(context, "range");
        PlayerEntity player = context.getSource().asPlayer();
        BlockPos hitPos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
        List<BlockPos> area = BlockPos.getAllInBox(hitPos.add(-range, -range, -range), hitPos.add(range, range, range))
                .filter(blockPos -> player.getEntityWorld().getBlockState(blockPos).getBlock() instanceof GooBase)
                .map(BlockPos::toImmutable)
                .collect(Collectors.toList());
        for (BlockPos pos : area)
            GooBase.resetBlock((ServerWorld) player.getEntityWorld(), pos, false);
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.cleargoo " + area.size()), false);
        return 0;
    }
}
