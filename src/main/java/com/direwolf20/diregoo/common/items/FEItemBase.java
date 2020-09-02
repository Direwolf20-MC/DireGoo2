package com.direwolf20.diregoo.common.items;

import com.direwolf20.diregoo.common.capabilities.ItemEnergyProvider;
import com.direwolf20.diregoo.common.util.MagicHelpers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class FEItemBase extends Item {
    private int energyCapacity;

    public FEItemBase(Item.Properties properties) {
        super(properties);
        this.energyCapacity = 1000000;
    }

    public FEItemBase(Item.Properties properties, int MaxRF) {
        super(properties);
        this.energyCapacity = MaxRF;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.energyCapacity;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        stack.getCapability(CapabilityEnergy.ENERGY, null)
                .ifPresent(energy -> tooltip.add(
                        new TranslationTextComponent("diregoo.tooltip.itemenergy",
                                MagicHelpers.tidyValue(energy.getEnergyStored()),
                                MagicHelpers.tidyValue(energy.getMaxEnergyStored())).mergeStyle(TextFormatting.GREEN)));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        return (energy.getEnergyStored() < energy.getMaxEnergyStored());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ItemEnergyProvider(stack, 1000000);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        if (energy == null)
            return 0;

        return 1D - (energy.getEnergyStored() / (double) energy.getMaxEnergyStored());
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        if (energy == null)
            return super.getRGBDurabilityForDisplay(stack);

        return MathHelper.hsvToRGB(Math.max(0.0F, (float) energy.getEnergyStored() / (float) energy.getMaxEnergyStored()) / 3.0F, 1.0F, 1.0F);
    }

    public boolean canUseItem(ItemStack itemStack, int RFCost) {
        int energy = itemStack.getOrCreateTag().getInt("energy");
        return RFCost <= energy;
    }
}
