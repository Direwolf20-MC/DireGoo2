package com.direwolf20.diregoo.client.screens;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.GooliminationFieldGenContainer;
import com.direwolf20.diregoo.common.network.PacketHandler;
import com.direwolf20.diregoo.common.network.packets.PacketChangeGooliminationFieldActive;
import com.direwolf20.diregoo.common.tiles.GooliminationFieldGenTile;
import com.direwolf20.diregoo.common.util.MagicHelpers;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GooliminationFieldGenScreen extends ContainerScreen<GooliminationFieldGenContainer> {
    private static final ResourceLocation background = new ResourceLocation(DireGoo.MOD_ID, "textures/gui/gooliminiationfieldgen.png");

    protected final GooliminationFieldGenContainer container;
    private boolean isActive;

    public GooliminationFieldGenScreen(GooliminationFieldGenContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.container = container;
        GooliminationFieldGenTile te = (GooliminationFieldGenTile) container.tile;
        isActive = te.isActive(te.getWorld());
    }

    public ResourceLocation getBackground() {
        return background;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);

        this.renderHoveredTooltip(stack, mouseX, mouseY); // @mcp: func_230459_a_ = renderHoveredToolTip
        if (mouseX > (guiLeft + 7) && mouseX < (guiLeft + 7) + 18 && mouseY > (guiTop + 7) && mouseY < (guiTop + 7) + 73)
            this.renderTooltip(stack, LanguageMap.getInstance().func_244260_a(Arrays.asList(
                    new TranslationTextComponent("screen.diregoo.energy", MagicHelpers.withSuffix(this.container.getEnergy()), MagicHelpers.withSuffix(this.container.getMaxPower())))
            ), mouseX, mouseY);
    }

    @Override
    public void init() {
        List<Widget> Rwidgets = new ArrayList<>();
        int baseX = width / 3 + width / 10, baseY = (height / 2) - (height / 40);
        int top = baseY - (80);

        Rwidgets.add(new Button(baseX + 15, 0, 50, 20, new TranslationTextComponent("screen.diregoo.antigoofieldgen.active", isActive), (button) -> {
            isActive = !isActive;
            button.setMessage(new TranslationTextComponent("screen.diregoo.goolimination.active", isActive));
            PacketHandler.sendToServer(new PacketChangeGooliminationFieldActive(isActive, this.container.tile.getPos()));
        }));

        for (int i = 0; i < Rwidgets.size(); i++) {
            Rwidgets.get(i).y = (top + 20) + (i * 15);
            addButton(Rwidgets.get(i));
        }

        super.init();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1, 1, 1, 1);
        getMinecraft().getTextureManager().bindTexture(getBackground());
        this.blit(stack, guiLeft, guiTop, 0, 0, xSize, ySize);

        int maxEnergy = this.container.getMaxPower(), height = 70;
        if (maxEnergy > 0) {
            int remaining = (int) Math.floor(((double) this.container.getEnergy() * height) / maxEnergy);
            this.blit(stack, guiLeft + 8, guiTop + 78 - remaining, 176, 84 - remaining, 16, remaining + 1);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.gooliminationscreen"), 28, 8, Color.DARK_GRAY.getRGB());
    }

    protected static TranslationTextComponent getTrans(String key, Object... args) {
        return new TranslationTextComponent(DireGoo.MOD_ID + "." + key, args);
    }
}
