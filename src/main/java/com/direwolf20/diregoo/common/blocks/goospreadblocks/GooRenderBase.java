package com.direwolf20.diregoo.common.blocks.goospreadblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;

public class GooRenderBase extends Block {
    public static final IntegerProperty GROWTH = IntegerProperty.create("growth", 1, 9);

    public GooRenderBase() {
        super(Properties.create(Material.IRON).hardnessAndResistance(2.0f));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(GROWTH);
    }
}
