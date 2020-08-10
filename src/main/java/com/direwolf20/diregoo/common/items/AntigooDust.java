package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.worldsave.BlockSave;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AntigooDust extends Item {

    public AntigooDust() {
        super(new Item.Properties().maxStackSize(64).group(DireGoo.itemGroup));
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote) return ActionResultType.SUCCESS; //Skip client side

        ItemStack itemStack = context.getItem();
        BlockPos targetPos = context.getPos();

        BlockSave blockSave = BlockSave.get(world);
        if (blockSave.addAnti(targetPos))
            itemStack.shrink(1);
        return ActionResultType.SUCCESS;
    }

    /*@Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);



        //Server Side Only
        BlockSave blockSave = BlockSave.get(world);

        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }*/
}
