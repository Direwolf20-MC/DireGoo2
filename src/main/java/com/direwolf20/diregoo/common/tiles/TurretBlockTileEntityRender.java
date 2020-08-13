package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.client.renderer.OurRenderTypes;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sun.javafx.geom.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;

public class TurretBlockTileEntityRender extends TileEntityRenderer<TurretBlockTileEntity> {

    private static float xOffset;
    private static float yOffset = -1f;
    private static float yDelta = 0.01f;
    private static float zOffset;

    public TurretBlockTileEntityRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TurretBlockTileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        drawAllMiningLasers(tile, matrixStackIn, partialTicks);
    }

    public static void drawAllMiningLasers(TurretBlockTileEntity tile, MatrixStack matrixStackIn, float f) {
        BlockPos targetBlock = new BlockPos(263, 65, -146);

        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder;

        matrixStackIn.push();
        Matrix4f positionMatrix2 = matrixStackIn.getLast().getMatrix();

        long gameTime = tile.getWorld().getGameTime();
        double v = gameTime * 0.04;

        float r = 1f;
        float g = 0f;
        float b = 0f;
        float ir = 1f;
        float ig = 1f;
        float ib = 1f;
        float diffX = targetBlock.getX() + .5f - tile.getPos().getX();
        float diffY = targetBlock.getY() + .5f - tile.getPos().getY();
        float diffZ = targetBlock.getZ() + .5f - tile.getPos().getZ();
        Vec3f startLaser = new Vec3f(0.5f, 1.25f, 0.5f);
        Vec3f endLaser = new Vec3f(diffX, diffY, diffZ);

        builder = buffer.getBuffer(OurRenderTypes.LASER_MAIN_BEAM);
        drawMiningLaser(builder, positionMatrix2, endLaser, startLaser, r, g, b, 1f, 0.1f, v, v + diffY * 1.5);

        builder = buffer.getBuffer(OurRenderTypes.LASER_MAIN_CORE);
        drawMiningLaser(builder, positionMatrix2, endLaser, startLaser, ir, ig, ib, 1f, 0.05f, v, v + diffY - 2.5 * 1.5);


        buffer.finish();
        matrixStackIn.pop();
    }

    public static void drawMiningLaser(IVertexBuilder builder, Matrix4f positionMatrix, Vec3f from, Vec3f to, float r, float g, float b, float alpha, float thickness, double v1, double v2) {
        builder.pos(positionMatrix, from.x - thickness, from.y, from.z)
                .color(r, g, b, alpha)
                .tex(1, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, (float) to.x - thickness, (float) to.y, (float) to.z)
                .color(r, g, b, alpha)
                .tex(1, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, (float) to.x + thickness, (float) to.y, (float) to.z)
                .color(r, g, b, alpha)
                .tex(0, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, from.x + thickness, from.y, from.z)
                .color(r, g, b, alpha)
                .tex(0, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();

        builder.pos(positionMatrix, from.x, from.y, from.z - thickness)
                .color(r, g, b, alpha)
                .tex(1, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, (float) to.x, (float) to.y, (float) to.z - thickness)
                .color(r, g, b, alpha)
                .tex(1, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, (float) to.x, (float) to.y, (float) to.z + thickness)
                .color(r, g, b, alpha)
                .tex(0, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, from.x, from.y, from.z + thickness)
                .color(r, g, b, alpha)
                .tex(0, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();

        builder.pos(positionMatrix, from.x, from.y - thickness, from.z)
                .color(r, g, b, alpha)
                .tex(1, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, (float) to.x, (float) to.y - thickness, (float) to.z)
                .color(r, g, b, alpha)
                .tex(1, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, (float) to.x, (float) to.y + thickness, (float) to.z)
                .color(r, g, b, alpha)
                .tex(0, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, from.x, from.y + thickness, from.z)
                .color(r, g, b, alpha)
                .tex(0, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
    }

    @Override
    public boolean isGlobalRenderer(TurretBlockTileEntity te) {
        return true;
    }
}
