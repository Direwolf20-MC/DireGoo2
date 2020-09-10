package com.direwolf20.diregoo.common.container;

import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.items.CoreFreeze;
import com.direwolf20.diregoo.common.items.CoreMelt;
import com.direwolf20.diregoo.common.items.zapperupgrades.BaseFocusCrystal;
import com.direwolf20.diregoo.common.items.zapperupgrades.BasePowerAmp;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ZapperItemContainer extends Container {
    public static final int SLOTS = 3;
    public ItemStackHandler handler;

    public ZapperItemContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buf) {
        this(windowId, playerInventory, new ItemStackHandler(3));
    }

    public ZapperItemContainer(int windowId, PlayerInventory playerInventory, ItemStackHandler handler) {
        super(ModBlocks.ZAPPER_ITEM_CONTAINER.get(), windowId);
        this.handler = handler;
        this.setup(playerInventory);
    }


    public void setup(PlayerInventory inventory) {
        addSlot(new RestrictedSlot(handler, 0, 47, 41));
        addSlot(new RestrictedSlot(handler, 1, 85, 41));
        addSlot(new RestrictedSlot(handler, 2, 122, 41));
        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 56 + 86;
            addSlot(new Slot(inventory, row, x, y));
        }
        // Slots for the main inventory
        for (int row = 1; row < 4; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + (56 + 10);
                addSlot(new Slot(inventory, col + row * 9, x, y));
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
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

    static class RestrictedSlot extends SlotItemHandler {
        public RestrictedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            if (getSlotIndex() == 0)
                return (stack.getItem() instanceof CoreFreeze) || (stack.getItem() instanceof CoreMelt);

            if (getSlotIndex() == 1)
                return stack.getItem() instanceof BaseFocusCrystal;

            if (getSlotIndex() == 2)
                return stack.getItem() instanceof BasePowerAmp;

            return super.isItemValid(stack);
        }
    }

}
