package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.item.Item;

public class GooRemover extends Item {
    public GooRemover() {
        super(new Item.Properties().maxStackSize(1).group(DireGoo.itemGroup));
    }
}
