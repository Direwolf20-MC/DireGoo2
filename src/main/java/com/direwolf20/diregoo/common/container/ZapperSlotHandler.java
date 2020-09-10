package com.direwolf20.diregoo.common.container;

import com.direwolf20.diregoo.common.items.GooZapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ZapperSlotHandler extends ItemStackHandler {
    ItemStack stack;

    public ZapperSlotHandler(int size, ItemStack itemStack) {
        super(size);
        this.stack = itemStack;
    }

    @Override
    protected void onContentsChanged(int slot) {
        GooZapper.setInventory(stack, this);
    }
}