package com.direwolf20.diregoo.common.container;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.items.AntiGooDust;
import com.direwolf20.diregoo.common.items.CoreFreeze;
import com.direwolf20.diregoo.common.items.CoreMelt;
import com.direwolf20.diregoo.common.items.zapperupgrades.BasePowerAmp;
import com.direwolf20.diregoo.common.tiles.TurretBlockTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TurretContainer extends FEContainerBase {
    public static final int SLOTS = 3;

    public TurretContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        this((TurretBlockTileEntity) playerInventory.player.world.getTileEntity(extraData.readBlockPos()), new IntArray(3), windowId, playerInventory, new ItemStackHandler(SLOTS));
    }

    public TurretContainer(@Nullable TurretBlockTileEntity tile, IIntArray turretData, int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModBlocks.TURRET_CONTAINER.get(), tile, turretData, windowId, playerInventory, handler);
        this.tile = tile;
        this.data = turretData;
        this.setup(playerInventory);
        trackIntArray(turretData);
    }

    @Override
    public void setup(PlayerInventory inventory) {
        addSlot(new RestrictedSlot(handler, 0, 47, 41, this));
        addSlot(new RestrictedSlot(handler, 1, 85, 41, this));
        addSlot(new RestrictedSlot(handler, 2, 122, 41, this));
        //addSlot(new RestrictedSlot(handler, 1, 119, 43));
        super.setup(inventory);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        BlockPos pos = this.tile.getPos();
        return this.tile != null && !this.tile.isRemoved() && playerIn.getDistanceSq(new Vector3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack currentStack = slot.getStack();
            itemstack = currentStack.copy();

            if (index < SLOTS) {
                if (!this.mergeItemStack(currentStack, SLOTS, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(currentStack, 0, SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (currentStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public int getFuelShots() {
        return this.data.get(2);
    }


    static class RestrictedSlot extends SlotItemHandler {
        TurretContainer container;

        public RestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, TurretContainer fromContainer) {
            super(itemHandler, index, xPosition, yPosition);
            this.container = fromContainer;
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            if (getSlotIndex() == 0)
                return (stack.getItem() instanceof CoreFreeze) || (stack.getItem() instanceof CoreMelt);

            if (getSlotIndex() == 1)
                return stack.getItem() instanceof AntiGooDust;

            if (getSlotIndex() == 2)
                return stack.getItem() instanceof BasePowerAmp;

            return super.isItemValid(stack);
        }

        @Override
        public void onSlotChanged() {
            if (getSlotIndex() == 0 || getSlotIndex() == 2) {
                TileEntity te = container.tile;
                if (!te.getWorld().isRemote) {
                    if (te instanceof TurretBlockTileEntity) {
                        ((TurretBlockTileEntity) te).generateTurretQueue();
                    }
                }
            }
            super.onSlotChanged();
        }
    }
}
