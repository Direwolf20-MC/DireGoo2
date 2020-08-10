package com.direwolf20.diregoo.common.events;

import com.direwolf20.diregoo.common.blocks.GooBase;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OnBlockBreak {

    @SubscribeEvent
    public static void UpdateWorldSave(BlockEvent.BreakEvent event) {
        World world = event.getWorld().getWorld();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        BlockSave blockSave = BlockSave.get(world);
        if (blockSave.checkAnti(pos))
            blockSave.removeAnti(pos);
        if (state.getBlock() instanceof GooBase) {
            blockSave.pop(pos);
            blockSave.popTE(pos);
        }
    }
}
