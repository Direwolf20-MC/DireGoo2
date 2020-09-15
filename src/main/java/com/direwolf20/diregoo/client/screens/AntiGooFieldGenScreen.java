package com.direwolf20.diregoo.client.screens;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.container.AntiGooFieldGenContainer;
import com.direwolf20.diregoo.common.network.PacketHandler;
import com.direwolf20.diregoo.common.network.packets.PacketChangeAntiGooFieldActive;
import com.direwolf20.diregoo.common.network.packets.PacketChangeAntiGooFieldRange;
import com.direwolf20.diregoo.common.network.packets.PacketChangeAntiGooFieldRender;
import com.direwolf20.diregoo.common.tiles.AntiGooFieldGenTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AntiGooFieldGenScreen extends FEScreenBase<AntiGooFieldGenContainer> implements Slider.ISlider {
    private static final ResourceLocation background = new ResourceLocation(DireGoo.MOD_ID, "textures/gui/antigoofieldgen.png");
    private int[] ranges;
    private Slider ESlider;
    private Slider WSlider;
    private Slider NSlider;
    private Slider SSlider;
    private Slider USlider;
    private Slider DSlider;
    private boolean isRenderArea;
    private boolean isActive;

    public AntiGooFieldGenScreen(AntiGooFieldGenContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        AntiGooFieldGenTileEntity te = (AntiGooFieldGenTileEntity) container.tile;
        ranges = te.getRanges();
        isRenderArea = te.isRenderArea();
        isActive = te.isActive();
    }

    @Override
    public void init() {
        List<Widget> Lwidgets = new ArrayList<>();
        List<Widget> Rwidgets = new ArrayList<>();
        int baseX = width / 3 + width / 20, baseY = (height / 2) - (height / 40);
        int top = baseY - (80);

        Lwidgets.add(NSlider = new Slider(baseX, 0, 50, 10, new TranslationTextComponent("N:"), StringTextComponent.EMPTY, 0, 25, this.ranges[0], false, true, s -> {
        }, this));
        Lwidgets.add(ESlider = new Slider(baseX, 0, 50, 10, new TranslationTextComponent("E:"), StringTextComponent.EMPTY, 0, 25, this.ranges[3], false, true, s -> {
        }, this));
        Lwidgets.add(USlider = new Slider(baseX, 0, 50, 10, new TranslationTextComponent("U:"), StringTextComponent.EMPTY, 0, 25, this.ranges[4], false, true, s -> {
        }, this));

        Lwidgets.add(new Button(baseX, 0, 50, 20, new TranslationTextComponent("screen.diregoo.antigoofieldgen.renderarea", isRenderArea), (button) -> {
            isRenderArea = !isRenderArea;
            button.setMessage(new TranslationTextComponent("screen.diregoo.antigoofieldgen.renderarea", isRenderArea));
            PacketHandler.sendToServer(new PacketChangeAntiGooFieldRender(isRenderArea, this.container.tile.getPos()));
        }));

        Rwidgets.add(SSlider = new Slider(baseX + 64, 0, 50, 10, new TranslationTextComponent("S:"), StringTextComponent.EMPTY, 0, 25, this.ranges[1], false, true, s -> {
        }, this));
        Rwidgets.add(WSlider = new Slider(baseX + 64, 0, 50, 10, new TranslationTextComponent("W:"), StringTextComponent.EMPTY, 0, 25, this.ranges[2], false, true, s -> {
        }, this));
        Rwidgets.add(DSlider = new Slider(baseX + 64, 0, 50, 10, new TranslationTextComponent("D:"), StringTextComponent.EMPTY, 0, 25, this.ranges[5], false, true, s -> {
        }, this));
        Rwidgets.add(new Button(baseX + 64, 0, 50, 20, new TranslationTextComponent("screen.diregoo.antigoofieldgen.active", isActive), (button) -> {
            isActive = !isActive;
            button.setMessage(new TranslationTextComponent("screen.diregoo.antigoofieldgen.active", isActive));
            PacketHandler.sendToServer(new PacketChangeAntiGooFieldActive(isActive, this.container.tile.getPos()));
        }));


        for (int i = 0; i < Lwidgets.size(); i++) {
            Lwidgets.get(i).y = (top + 20) + (i * 15);
            addButton(Lwidgets.get(i));
        }
        for (int i = 0; i < Rwidgets.size(); i++) {
            Rwidgets.get(i).y = (top + 20) + (i * 15);
            addButton(Rwidgets.get(i));
        }
        super.init();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(stack, I18n.format("block.diregoo.antigoofieldgen"), 40, 5, Color.DARK_GRAY.getRGB());
    }

    @Override
    public void onChangeSliderValue(Slider slider) {
        if (slider.equals(NSlider))
            this.ranges[0] = slider.getValueInt();
        if (slider.equals(SSlider))
            this.ranges[1] = slider.getValueInt();
        if (slider.equals(WSlider))
            this.ranges[2] = slider.getValueInt();
        if (slider.equals(ESlider))
            this.ranges[3] = slider.getValueInt();
        if (slider.equals(USlider))
            this.ranges[4] = slider.getValueInt();
        if (slider.equals(DSlider))
            this.ranges[5] = slider.getValueInt();
    }

    @Override
    public void onClose() {
        PacketHandler.sendToServer(new PacketChangeAntiGooFieldRange(this.ranges, this.container.tile.getPos()));
        super.onClose();
    }

    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        NSlider.dragging = false;
        SSlider.dragging = false;
        WSlider.dragging = false;
        ESlider.dragging = false;
        USlider.dragging = false;
        DSlider.dragging = false;
        return false;
    }

    /*@Override
    public void removed() {
        PacketHandler.sendToServer(new PacketChangeAntiGooFieldRange(this.ranges, this.container.tile.getPos()));
        super.removed();
    }*/
}
