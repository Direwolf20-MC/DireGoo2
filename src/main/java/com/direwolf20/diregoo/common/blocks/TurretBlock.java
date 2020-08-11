package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.common.tiles.TurretBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class TurretBlock extends Block {
    public TurretBlock() {
        super(
                Properties.create(Material.IRON)
                        .hardnessAndResistance(2.0f)
        );
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TurretBlockTileEntity();
    }

}
