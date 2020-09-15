package com.direwolf20.diregoo.client.screens;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.AntiGooBeaconContainer;
import com.direwolf20.diregoo.common.util.MagicHelpers;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class AntiGooBeaconScreen extends ContainerScreen<AntiGooBeaconContainer> {
    private static final ResourceLocation background = new ResourceLocation(DireGoo.MOD_ID, "textures/gui/antigoobeacon.png");

    protected final AntiGooBeaconContainer container;

    public AntiGooBeaconScreen(AntiGooBeaconContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.container = container;
    }

    public ResourceLocation getBackground() {
        return background;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);

        /*this.func_230459_a_(stack, mouseX, mouseY); // @mcp: func_230459_a_ = renderHoveredToolTip
        if (mouseX > (guiLeft + 7) && mouseX < (guiLeft + 7) + 18 && mouseY > (guiTop + 7) && mouseY < (guiTop + 7) + 73)
            this.renderTooltip(stack, LanguageMap.getInstance().func_244260_a(Arrays.asList(
                    new TranslationTextComponent("screen.diregoo.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(this.container.getMaxPower())))
            ), mouseX, mouseY);*/
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1, 1, 1, 1);
        getMinecraft().getTextureManager().bindTexture(getBackground());
        this.blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);

        /*int maxEnergy = this.container.getMaxPower(), height = 70;
        if (maxEnergy > 0) {
            int remaining = (this.container.getEnergy() * height) / maxEnergy;
            this.blit(stack, guiLeft + 8, guiTop + 78 - remaining, 176, 84 - remaining, 16, remaining + 1);
        }*/
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.antigoobeaconscreen"), 55, 8, Color.DARK_GRAY.getRGB());
        if (container.getFuelRemaining() > 0) {
            Minecraft.getInstance().fontRenderer.drawString(stack, new TranslationTextComponent("screen.diregoo.antigoobeacon.fuel_remaining", MagicHelpers.ticksInSeconds(this.container.getFuelRemaining())).getString(), 45, 30, Color.DARK_GRAY.getRGB());
        }
        /*Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.focusSlot"), 47, 30, Color.DARK_GRAY.getRGB());
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.powerSlot"), 85, 30, Color.DARK_GRAY.getRGB());
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.beamSlot"), 122, 30, Color.DARK_GRAY.getRGB());*/

    }

    protected static TranslationTextComponent getTrans(String key, Object... args) {
        return new TranslationTextComponent(DireGoo.MOD_ID + "." + key, args);
    }
}
