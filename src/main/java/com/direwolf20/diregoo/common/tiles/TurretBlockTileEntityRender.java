package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.client.renderer.MyRenderHelper;
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
        /*BlockPos destination = new BlockPos(263, 65, -146);
        if (destination != null) {

                TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(OurRenderTypes.laserBeam);
                IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                IVertexBuilder builder = buffer.getBuffer(OurRenderTypes.LASER_MAIN_BEAM);
                matrixStackIn.push();

                MyRenderHelper.rotateToPlayer(matrixStackIn);
                Matrix4f matrix = matrixStackIn.getLast().getMatrix();
                PlayerEntity player = Minecraft.getInstance().player;
                MyRenderHelper.Vector start = new MyRenderHelper.Vector(.5f, 1.25f, .5f);
                MyRenderHelper.Vector end = new MyRenderHelper.Vector(destination.getX() + .5f- tile.getPos().getX(), destination.getY() + .5f- tile.getPos().getY(), destination.getZ() + .5f- tile.getPos().getZ());
                MyRenderHelper.Vector playerVec = new MyRenderHelper.Vector((float)player.getPosX()- tile.getPos().getX(),(float)player.getPosYEye()- tile.getPos().getY(),(float)player.getPosZ()- tile.getPos().getZ());
                MyRenderHelper.drawBeam(matrix, builder, sprite, start, end, playerVec, .1f);

            matrixStackIn.pop();
            buffer.finish();
        }*/

    }

    public static void drawAllMiningLasers(TurretBlockTileEntity tile, MatrixStack matrixStackIn, float f) {
        BlockPos targetBlock = new BlockPos(264, 67, -145);

        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder;

        matrixStackIn.push();
        //MyRenderHelper.rotateToPlayer(matrixStackIn);
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
        //Vec3f startLaser = new Vec3f(tile.getPos().getX() +0.5f, tile.getPos().getY() +1.25f, tile.getPos().getZ() +0.5f);
        Vec3f endLaser = new Vec3f(diffX, diffY, diffZ);

        builder = buffer.getBuffer(OurRenderTypes.LASER_MAIN_BEAM);
        drawMiningLaser(builder, positionMatrix2, endLaser, startLaser, r, g, b, 1f, 0.1f, v, v + diffY * 1.5, tile);
        /*drawMiningLaser3(builder, positionMatrix2, endLaser, startLaser, new Vec3f(0,0,0.1f), r, g, b, 1f, 0f, 0.1f, 0f, v, v + diffY * 1.5, tile);
        startLaser = new Vec3f(0.5f, 1.25f, 0.5f);
        endLaser = new Vec3f(diffX, diffY, diffZ);
        drawMiningLaser3(builder, positionMatrix2, endLaser, startLaser, new Vec3f(0,0,-0.1f), r, g, b, 1f, 0f, 0.1f, 0f, v, v + diffY * 1.5, tile);
        startLaser = new Vec3f(0.5f, 1.25f, 0.5f);
        endLaser = new Vec3f(diffX, diffY, diffZ);
        drawMiningLaser3(builder, positionMatrix2, endLaser, startLaser, new Vec3f(0,0.1f,0f), r, g, b, 1f, 0f, 0f, 0.1f, v, v + diffY * 1.5, tile);
        startLaser = new Vec3f(0.5f, 1.25f, 0.5f);
        endLaser = new Vec3f(diffX, diffY, diffZ);
        drawMiningLaser3(builder, positionMatrix2, endLaser, startLaser, new Vec3f(0,-0.1f,0f), r, g, b, 1f, 0f, 0f, 0.1f, v, v + diffY * 1.5, tile);
        startLaser = new Vec3f(0.5f, 1.25f, 0.5f);
        endLaser = new Vec3f(diffX, diffY, diffZ);*/
        builder = buffer.getBuffer(OurRenderTypes.LASER_MAIN_CORE);
        drawMiningLaser(builder, positionMatrix2, endLaser, startLaser, ir, ig, ib, 1f, 0.05f, v, v + diffY - 2.5 * 1.5, tile);


        buffer.finish();
        matrixStackIn.pop();
    }

    public static void drawMiningLaser3(IVertexBuilder builder, Matrix4f positionMatrix, Vec3f from, Vec3f to, Vec3f offset, float r, float g, float b, float alpha, float thicknessX, float thicknessY, float thicknessZ, double v1, double v2, TurretBlockTileEntity tile) {
        from.add(offset);
        to.add(offset);
        builder.pos(positionMatrix, from.x - thicknessX, from.y - thicknessY, from.z - thicknessZ)
                .color(r, g, b, alpha)
                .tex(1, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, (float) to.x - thicknessX, (float) to.y - thicknessY, (float) to.z - thicknessZ)
                .color(r, g, b, alpha)
                .tex(1, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, (float) to.x + thicknessX, (float) to.y + thicknessY, (float) to.z + thicknessZ)
                .color(r, g, b, alpha)
                .tex(0, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, from.x + thicknessX, from.y + thicknessY, from.z + thicknessZ)
                .color(r, g, b, alpha)
                .tex(0, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
    }

    public static void drawMiningLaser(IVertexBuilder builder, Matrix4f positionMatrix, Vec3f from, Vec3f to, float r, float g, float b, float alpha, float thickness, double v1, double v2, TurretBlockTileEntity tile) {
        MyRenderHelper.Vector S = new MyRenderHelper.Vector(from.x, from.y, from.z);
        MyRenderHelper.Vector E = new MyRenderHelper.Vector(to.x, to.y, to.z);
        PlayerEntity player = Minecraft.getInstance().player;
        MyRenderHelper.Vector P = new MyRenderHelper.Vector((float) player.getPosX() - tile.getPos().getX(), (float) player.getPosYEye() - tile.getPos().getY(), (float) player.getPosZ() - tile.getPos().getZ());
        MyRenderHelper.Vector PS = MyRenderHelper.Sub(S, P);
        MyRenderHelper.Vector SE = MyRenderHelper.Sub(E, S);

        MyRenderHelper.Vector normal = MyRenderHelper.Cross(PS, SE);
        MyRenderHelper.Vector normal2 = new MyRenderHelper.Vector(normal.y, normal.z, normal.x);
        normal = normal.normalize();
        normal2 = normal2.normalize();
        //normal2 = MyRenderHelper.Mul(normal, -1);

        MyRenderHelper.Vector half = MyRenderHelper.Mul(normal, thickness);
        MyRenderHelper.Vector half2 = MyRenderHelper.Mul(normal2, thickness);

        MyRenderHelper.Vector p1 = MyRenderHelper.Add(S, half);
        MyRenderHelper.Vector p2 = MyRenderHelper.Sub(S, half);
        MyRenderHelper.Vector p3 = MyRenderHelper.Add(E, half);
        MyRenderHelper.Vector p4 = MyRenderHelper.Sub(E, half);

        /*MyRenderHelper.Vector p5 = MyRenderHelper.Add(MyRenderHelper.Add(S, half2), half);
        MyRenderHelper.Vector p6 = MyRenderHelper.Add(MyRenderHelper.Sub(S, half2), half);
        MyRenderHelper.Vector p7 = MyRenderHelper.Add(MyRenderHelper.Add(E, half2), half);
        MyRenderHelper.Vector p8 = MyRenderHelper.Add(MyRenderHelper.Sub(E, half2), half);*/

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
        /*builder.pos(positionMatrix, p6.x, p6.y, p6.z)
                .color(r, g, b, alpha)
                .tex(1, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p8.x, p8.y, p8.z)
                .color(r, g, b, alpha)
                .tex(1, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p7.x, p7.y, p7.z)
                .color(r, g, b, alpha)
                .tex(0, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p5.x, p5.y, p5.z)
                .color(r, g, b, alpha)
                .tex(0, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();*/

/*
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
                .endVertex();*/
    }


    @Override
    public boolean isGlobalRenderer(TurretBlockTileEntity te) {
        return true;
    }
}
