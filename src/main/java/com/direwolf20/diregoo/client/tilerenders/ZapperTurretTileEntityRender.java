package com.direwolf20.diregoo.client.tilerenders;

import com.direwolf20.diregoo.client.renderer.util.OurRenderTypes;
import com.direwolf20.diregoo.common.blocks.ZapperTurretBlock;
import com.direwolf20.diregoo.common.tiles.ZapperTurretTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class ZapperTurretTileEntityRender extends TileEntityRenderer<ZapperTurretTileEntity> {

    public ZapperTurretTileEntityRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ZapperTurretTileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        drawAllMiningLasers(tile, matrixStackIn, partialTicks, bufferIn);
    }

    public static void drawAllMiningLasers(ZapperTurretTileEntity tile, MatrixStack matrixStackIn, float f, IRenderTypeBuffer bufferIn) {
        //if (tile.getFiringCooldown() == 0) return;
        //OurRenderTypes.updateRenders();
        //BlockPos targetBlock = tile.getCurrentTarget();
        Direction facing = tile.getBlockState().get(ZapperTurretBlock.FACING);
        BlockPos targetBlock = tile.getPos().offset(facing, 5);

        //IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder;

        matrixStackIn.push();
        Matrix4f positionMatrix2 = matrixStackIn.getLast().getMatrix();

        long gameTime = tile.getWorld().getGameTime();
        double v = gameTime * 0.04;

        float diffX = targetBlock.getX() + .5f - tile.getPos().getX();
        float diffY = targetBlock.getY() + .5f - tile.getPos().getY();
        float diffZ = targetBlock.getZ() + .5f - tile.getPos().getZ();
        Vector3f startLaser = new Vector3f(0.5f + facing.getXOffset() / 2, .5f + facing.getYOffset() / 2, 0.5f + facing.getZOffset() / 2);
        Vector3f endLaser = new Vector3f(diffX, diffY, diffZ);

        builder = bufferIn.getBuffer(OurRenderTypes.LASER_MAIN_BEAM);
        drawMiningLaser(builder, positionMatrix2, endLaser, startLaser, 1, 0, 0, 1f, 0.5f, v, v + diffY * 1.5, tile);
        builder = bufferIn.getBuffer(OurRenderTypes.LASER_MAIN_CORE);
        drawMiningLaser(builder, positionMatrix2, endLaser, startLaser, 1, 1, 1, 1f, 0.25f, v, v + diffY - 2.5 * 1.5, tile);

        matrixStackIn.pop();
    }

    public static Vector3f adjustBeamToEyes(Vector3f from, Vector3f to, ZapperTurretTileEntity tile) {
        //This method takes the player's position into account, and adjusts the beam so that its rendered properly whereever you stand
        PlayerEntity player = Minecraft.getInstance().player;
        Vector3f P = new Vector3f((float) player.getPosX() - tile.getPos().getX(), (float) player.getPosYEye() - tile.getPos().getY(), (float) player.getPosZ() - tile.getPos().getZ());

        Vector3f PS = from.copy();
        PS.sub(P);
        Vector3f SE = to.copy();
        SE.sub(from);

        Vector3f adjustedVec = PS.copy();
        adjustedVec.cross(SE);
        adjustedVec.normalize();
        return adjustedVec;
    }

    public static void drawMiningLaser(IVertexBuilder builder, Matrix4f positionMatrix, Vector3f from, Vector3f to, float r, float g, float b, float alpha, float thickness, double v1, double v2, ZapperTurretTileEntity tile) {
        Vector3f adjustedVec = adjustBeamToEyes(from, to, tile);
        adjustedVec.mul(thickness); //Determines how thick the beam is

        Vector3f p1 = from.copy();
        p1.add(adjustedVec);
        Vector3f p2 = from.copy();
        p2.sub(adjustedVec);
        Vector3f p3 = to.copy();
        p3.add(adjustedVec);
        Vector3f p4 = to.copy();
        p4.sub(adjustedVec);

        builder.pos(positionMatrix, p1.getX(), p1.getY(), p1.getZ())
                .color(r, g, b, alpha)
                .tex(1, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p3.getX(), p3.getY(), p3.getZ())
                .color(r, g, b, alpha)
                .tex(1, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p4.getX(), p4.getY(), p4.getZ())
                .color(r, g, b, alpha)
                .tex(0, (float) v2)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
        builder.pos(positionMatrix, p2.getX(), p2.getY(), p2.getZ())
                .color(r, g, b, alpha)
                .tex(0, (float) v1)
                .overlay(OverlayTexture.NO_OVERLAY)
                .lightmap(15728880)
                .endVertex();
    }

    @Override
    public boolean isGlobalRenderer(ZapperTurretTileEntity te) {
        return true;
    }
}
