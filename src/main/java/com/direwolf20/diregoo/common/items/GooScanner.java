package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.client.renderer.GooScannerRender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class GooScanner extends Item {
    public GooScanner() {
        super(new Properties().maxStackSize(1).group(DireGoo.itemGroup));
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (world.isRemote) { //Client Side
            GooScannerRender.discoverGoo(player);
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
        if (!world.isRemote) { //Server Side
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
}
