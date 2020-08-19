package com.direwolf20.diregoo.client.screens;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.AntiGooFieldGenContainer;
import com.direwolf20.diregoo.common.tiles.AntiGooFieldGenTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AntiGooFieldGenScreen extends FEScreenBase<AntiGooFieldGenContainer> implements Slider.ISlider {
    private static final ResourceLocation background = new ResourceLocation(DireGoo.MOD_ID, "textures/gui/antigoofieldgen.png");
    private int xRange;
    private Slider xRangeSlider;
    private int yRange;
    private Slider yRangeSlider;
    private int zRange;
    private Slider zRangeSlider;

    public AntiGooFieldGenScreen(AntiGooFieldGenContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        AntiGooFieldGenTileEntity te = (AntiGooFieldGenTileEntity) container.tile;
        xRange = te.getRange();
    }

    @Override
    public void init() {
        List<Widget> widgets = new ArrayList<>();
        int baseX = width / 2 + width / 7, baseY = height / 2;
        int top = baseY - (80);

        widgets.add(xRangeSlider = new Slider(baseX - 135, 0, 100, 10, getTrans("tooltip.screen.range").appendString(": "), StringTextComponent.EMPTY, 1, 25, this.xRange, false, true, s -> {
        }, this));


        for (int i = 0; i < widgets.size(); i++) {
            widgets.get(i).y = (top + 20) + (i * 25);
            addButton(widgets.get(i));
        }
        super.init();
    }

    @Override
    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.antigoofieldgen"), 55, 8, Color.DARK_GRAY.getRGB());
    }

    @Override
    public void onChangeSliderValue(Slider slider) {
        if (slider.equals(xRangeSlider))
            this.xRange = slider.getValueInt();
        if (slider.equals(yRangeSlider))
            this.yRange = slider.getValueInt();
        if (slider.equals(zRangeSlider))
            this.zRange = slider.getValueInt();
    }

    @Override
    public void onClose() {
        //PacketHandler.sendToServer(new PacketChangeRange(this.beamRange));
        super.onClose();
    }
}
