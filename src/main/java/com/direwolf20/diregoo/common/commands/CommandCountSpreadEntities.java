package com.direwolf20.diregoo.common.commands;

import com.direwolf20.diregoo.common.entities.GooSpreadEntity;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class CommandCountSpreadEntities {

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("countSpreadEntities")
                .requires(cs -> cs.hasPermissionLevel(0))
                .then(Commands.argument("range", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                        .executes(ctx -> outputEntityCount(ctx)));
    }

    public static int outputEntityCount(CommandContext<CommandSource> context) throws CommandSyntaxException {
        World world = context.getSource().getWorld();
        int range = IntegerArgumentType.getInteger(context, "range");
        Vector3d playerPos = context.getSource().asPlayer().getPositionVec();
        List<GooSpreadEntity> list = world.getEntitiesWithinAABB(GooSpreadEntity.class, new AxisAlignedBB(playerPos.getX() - range, playerPos.getY() - range, playerPos.getZ() - range, playerPos.getX() + range, playerPos.getY() + range, playerPos.getZ() + range));
        context.getSource().sendFeedback(new TranslationTextComponent("message.diregoo.command.entityCount", list.size()), false);
        return 0;
    }

}
