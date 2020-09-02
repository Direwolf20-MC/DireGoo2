package com.direwolf20.diregoo.client.screens;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.TurretContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class TurretScreen extends FEScreenBase<TurretContainer> {
    private static final ResourceLocation background = new ResourceLocation(DireGoo.MOD_ID, "textures/gui/antigoofieldgen.png");

    public TurretScreen(TurretContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.turret"), 55, 8, Color.DARK_GRAY.getRGB());
    }
}
