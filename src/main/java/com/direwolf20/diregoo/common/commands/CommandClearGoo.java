package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.stream.Collectors;

public class CommandClearGoo {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("ClearGoo")
                .requires(cs -> cs.hasPermissionLevel(2))
                .then(Commands.argument("range", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                        .executes(ctx -> clearGoo(ctx)));
    }

    public static int clearGoo(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int range = IntegerArgumentType.getInteger(context, "range");
        PlayerEntity player = context.getSource().asPlayer();
        BlockSave blockSave = BlockSave.get(player.world);
        BlockPos hitPos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
        BlockPos minPos = hitPos.add(-range, -range, -range);
        BlockPos maxPos = hitPos.add(range, range, range);
        List<BlockPos> area = BlockPos.getAllInBox(minPos, maxPos)
                .filter(blockPos -> player.getEntityWorld().getBlockState(blockPos).getBlock() instanceof GooBase)
                .map(BlockPos::toImmutable)
                .collect(Collectors.toList());
        for (BlockPos pos : area)
            GooBase.resetBlock((ServerWorld) player.getEntityWorld(), pos, false, 80, false, blockSave);
        List<GooSpreadEntity> gooSpreadEntities = player.getEntityWorld().getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(minPos, maxPos));
        for (GooSpreadEntity gooSpreadEntity : gooSpreadEntities)
            gooSpreadEntity.remove();
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.cleargoo", area.size()), false);
        return 0;
    }
}
