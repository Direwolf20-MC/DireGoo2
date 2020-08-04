package com.direwolf20.diregoo.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GooBase extends Block {
    public GooBase() {
        super(
                Properties.create(Material.IRON).hardnessAndResistance(2.0f)
        );
    }

}
