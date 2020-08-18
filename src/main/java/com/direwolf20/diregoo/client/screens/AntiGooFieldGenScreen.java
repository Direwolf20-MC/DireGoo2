package com.direwolf20.diregoo.client.screens;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.AntiGooFieldGenContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class AntiGooFieldGenScreen extends FEScreenBase<AntiGooFieldGenContainer> {
    private static final ResourceLocation background = new ResourceLocation(DireGoo.MOD_ID, "textures/gui/antigoofieldgen.png");

    public AntiGooFieldGenScreen(AntiGooFieldGenContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        super.func_230450_a_(stack, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.antigoofieldgen"), 55, 8, Color.DARK_GRAY.getRGB());
    }
}
