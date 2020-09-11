package com.direwolf20.diregoo.client.screens;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.TurretContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class TurretScreen extends FEScreenBase<TurretContainer> {
    private static final ResourceLocation background = new ResourceLocation(DireGoo.MOD_ID, "textures/gui/turret.png");

    public TurretScreen(TurretContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public ResourceLocation getBackground() {
        return background;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.turret"), 55, 8, Color.DARK_GRAY.getRGB());
        if (container.getFuelShots() > 0) {
            Minecraft.getInstance().fontRenderer.drawString(stack, new TranslationTextComponent("screen.diregoo.turret.shots_remaining", this.container.getFuelShots() + (this.container.handler.getStackInSlot(0).getStack().getCount() * 5)).getString(), 30, 30, Color.DARK_GRAY.getRGB());
        }
    }
}
