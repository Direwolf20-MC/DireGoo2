package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.container.AntiGooFieldGenContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AntiGooFieldGenTileEntity extends FETileBase implements ITickableTileEntity, INamedContainerProvider {

    /*public ChargerEnergyStorage energyStorage;
    private LazyOptional<ChargerEnergyStorage> energy;*/


    public AntiGooFieldGenTileEntity() {
        super(ModBlocks.ANTI_GOO_FIELD_GEN_TILE.get());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        assert world != null;
        return new AntiGooFieldGenContainer(this, this.FETileData, i, playerInventory);
    }

    @Override
    public void tick() {
        //Client only
        if (world.isRemote) {
            //System.out.println("I'm here!");
        }

        //Server Only
        if (!world.isRemote) {
            //System.out.println("I'm here!");
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        return super.write(tag);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.up(10).north(10).east(10), pos.down(10).south(10).west(10));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Anti Goo Field Gen Tile");
    }
}
