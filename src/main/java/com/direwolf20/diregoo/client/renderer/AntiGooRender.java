package com.direwolf20.diregoo.client.renderer;

import com.direwolf20.diregoo.client.renderer.util.BlockOverlayRender;
import com.direwolf20.diregoo.client.renderer.util.OurRenderTypes;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class AntiGooRender {
    private static ArrayList<BlockPos> antigooList = new ArrayList<>();
    private static int sortCounter;

    public static void updateGooList(ArrayList<BlockPos> list) {
        antigooList = list;
        sortCounter = 0;
    }

    public static void renderAntiGoo(RenderWorldLastEvent event) {
        final Minecraft mc = Minecraft.getInstance();

        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

        Vector3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();

        MatrixStack matrix = event.getMatrixStack();
        matrix.push();
        matrix.translate(-view.getX(), -view.getY(), -view.getZ());

        IVertexBuilder builder;
        OurRenderTypes.updateRenders();
        builder = buffer.getBuffer(OurRenderTypes.BlockOverlay);

        if (sortCounter <= 0) { //Sort the block positions relative to player every 40 ticks, this looks better than not sorting especially on stuff like glass
            ClientPlayerEntity player = mc.player;
            antigooList.sort(Comparator.comparingDouble(blockPos -> blockPos.distanceSq(player.getPosX(), player.getPosYEye(), player.getPosZ(), true)));
            sortCounter = 40;
        } else {
            sortCounter--;
        }

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
        buffer.finish(OurRenderTypes.BlockOverlay);
    }


}
