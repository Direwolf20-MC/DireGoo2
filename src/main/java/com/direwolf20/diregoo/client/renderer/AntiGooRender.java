package com.direwolf20.diregoo.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class AntiGooRender {
    private static Set<BlockPos> antigooList = new HashSet<>();

    public static void updateGooList(Set<BlockPos> list) {
        antigooList = list;
    }

    public static void renderAntiGoo(RenderWorldLastEvent event) {
        final Minecraft mc = Minecraft.getInstance();

        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

        Vector3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();

        MatrixStack matrix = event.getMatrixStack();
        matrix.push();
        matrix.translate(-view.getX(), -view.getY(), -view.getZ());

        IVertexBuilder builder;
        builder = buffer.getBuffer(OurRenderTypes.BlockOverlay);
        antigooList.forEach(e -> {
            matrix.push();
            matrix.translate(e.getX(), e.getY(), e.getZ());
            matrix.translate(-0.005f, -0.005f, -0.005f);
            matrix.scale(1.01f, 1.01f, 1.01f);
            matrix.rotate(Vector3f.YP.rotationDegrees(-90.0F));

            Matrix4f positionMatrix = matrix.getLast().getMatrix();
            BlockOverlayRender.render(positionMatrix, builder, e, Color.GREEN);
            matrix.pop();
        });
        matrix.pop();
        RenderSystem.disableDepthTest();
        buffer.finish(OurRenderTypes.BlockOverlay);
    }
}
