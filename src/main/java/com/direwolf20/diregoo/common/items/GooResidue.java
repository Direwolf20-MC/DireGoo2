package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.item.Item;

public class GooResidue extends Item {
    public GooResidue() {
        super(new Item.Properties().maxStackSize(64).group(DireGoo.itemGroup));
    }
}
