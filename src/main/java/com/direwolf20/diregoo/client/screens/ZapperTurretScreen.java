package com.direwolf20.diregoo.client.screens;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.ZapperTurretContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class ZapperTurretScreen extends FEScreenBase<ZapperTurretContainer> {
    private static final ResourceLocation background = new ResourceLocation(DireGoo.MOD_ID, "textures/gui/zapperturret.png");

    public ZapperTurretScreen(ZapperTurretContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public ResourceLocation getBackground() {
        return background;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.zapperturretblock"), 55, 8, Color.DARK_GRAY.getRGB());
        /*Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.focusSlot"), 47, 30, Color.DARK_GRAY.getRGB());
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.powerSlot"), 85, 30, Color.DARK_GRAY.getRGB());
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.beamSlot"), 122, 30, Color.DARK_GRAY.getRGB());*/

    }
}
