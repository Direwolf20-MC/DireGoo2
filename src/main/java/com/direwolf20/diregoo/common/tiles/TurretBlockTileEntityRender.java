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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;

public class TurretBlockTileEntityRender extends TileEntityRenderer<TurretBlockTileEntity> {

    public TurretBlockTileEntityRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TurretBlockTileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        drawAllMiningLasers(tile, matrixStackIn, partialTicks);
    }

    public static void drawAllMiningLasers(TurretBlockTileEntity tile, MatrixStack matrixStackIn, float f) {
        BlockPos targetBlock = new BlockPos(264, 67, -145);

        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder;

        matrixStackIn.push();
        Matrix4f positionMatrix2 = matrixStackIn.getLast().getMatrix();

        long gameTime = tile.getWorld().getGameTime();
        double v = gameTime * 0.04;

        float diffX = targetBlock.getX() + .5f - tile.getPos().getX();
        float diffY = targetBlock.getY() + .5f - tile.getPos().getY();
        float diffZ = targetBlock.getZ() + .5f - tile.getPos().getZ();
        Vec3f startLaser = new Vec3f(0.5f, 1.25f, 0.5f);
        Vec3f endLaser = new Vec3f(diffX, diffY, diffZ);

        builder = buffer.getBuffer(OurRenderTypes.LASER_MAIN_BEAM);
        drawMiningLaser(builder, positionMatrix2, endLaser, startLaser, 1, 0, 0, 1f, 0.1f, v, v + diffY * 1.5, tile);

        builder = buffer.getBuffer(OurRenderTypes.LASER_MAIN_CORE);
        drawMiningLaser(builder, positionMatrix2, endLaser, startLaser, 1, 1, 1, 1f, 0.05f, v, v + diffY - 2.5 * 1.5, tile);

        buffer.finish();
        matrixStackIn.pop();
    }

    public static Vec3f adjustBeamToEyes(Vec3f from, Vec3f to, TurretBlockTileEntity tile) {
        //This method takes the player's position into account, and adjusts the beam so that its rendered properly whereever you stand
        PlayerEntity player = Minecraft.getInstance().player;
        Vec3f P = new Vec3f((float) player.getPosX() - tile.getPos().getX(), (float) player.getPosYEye() - tile.getPos().getY(), (float) player.getPosZ() - tile.getPos().getZ());

        Vec3f PS = new Vec3f(from);
        PS.sub(P);
        Vec3f SE = new Vec3f(to);
        SE.sub(from);

        Vec3f adjustedVec = new Vec3f();
        adjustedVec.cross(PS, SE);
        adjustedVec.normalize();
        return adjustedVec;
    }

    public static void drawMiningLaser(IVertexBuilder builder, Matrix4f positionMatrix, Vec3f from, Vec3f to, float r, float g, float b, float alpha, float thickness, double v1, double v2, TurretBlockTileEntity tile) {
        Vec3f adjustedVec = adjustBeamToEyes(from, to, tile);
        adjustedVec.mul(thickness); //Determines how thick the beam is

        Vec3f p1 = new Vec3f(from);
        p1.add(adjustedVec);
        Vec3f p2 = new Vec3f(from);
        p2.sub(adjustedVec);
        Vec3f p3 = new Vec3f(to);
        p3.add(adjustedVec);
        Vec3f p4 = new Vec3f(to);
        p4.sub(adjustedVec);

        builder.pos(positionMatrix, p1.x, p1.y, p1.z)
                .color(r, g, b, alpha)
                .tex(1, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p3.x, p3.y, p3.z)
                .color(r, g, b, alpha)
                .tex(1, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p4.x, p4.y, p4.z)
                .color(r, g, b, alpha)
                .tex(0, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p2.x, p2.y, p2.z)
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
