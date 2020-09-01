package com.direwolf20.diregoo.common.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

public class EnergisedItem extends EnergyStorage {
    private ItemStack stack;

    public EnergisedItem(ItemStack stack, int capacity) {
        super(capacity, 10000, 0, 0);
        this.stack = stack;
        this.energy = stack.hasTag() && stack.getTag().contains("energy") ? stack.getTag().getInt("energy") : 0;
    }

    private static int getMaxCapacity(ItemStack stack, int capacity) {
        if (!stack.hasTag() || !stack.getTag().contains("max_energy"))
            return capacity;

        return stack.getTag().getInt("max_energy");
    }

    @Override
    public int extractEnergy(int maxReceive, boolean simulate) {
        int amount = super.extractEnergy(maxReceive, simulate);

        if (!simulate)
            stack.getOrCreateTag().putInt("energy", this.energy);

        return amount;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int amount = super.receiveEnergy(maxReceive, simulate);

        if (!simulate)
            stack.getOrCreateTag().putInt("energy", this.energy);

        return amount;
    }
}
