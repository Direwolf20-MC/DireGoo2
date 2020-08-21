package com.direwolf20.diregoo.common.tiles;

import com.direwolf20.diregoo.client.renderer.AntiGooRender;
import com.direwolf20.diregoo.client.renderer.OurRenderTypes;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;

public class AntiGooFieldGenTileEntityRender extends TileEntityRenderer<AntiGooFieldGenTileEntity> {

    public AntiGooFieldGenTileEntityRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(AntiGooFieldGenTileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightsIn, int combinedOverlayIn) {
        if (tile.getProtectedBlocksList().isEmpty()) return;
        IVertexBuilder builder;
        builder = bufferIn.getBuffer(OurRenderTypes.AntiGooLines);
        matrixStackIn.push();
        Matrix4f positionMatrix2 = matrixStackIn.getLast().getMatrix();
        int[] ranges = tile.getRanges();
        BlockPos startPos = new BlockPos(-ranges[2], -ranges[5], -ranges[0]);
        BlockPos endPos = new BlockPos(ranges[3], ranges[4], ranges[1]);
        AntiGooRender.renderBox(positionMatrix2, builder, startPos, endPos);

        matrixStackIn.pop();
    }

    @Override
    public boolean isGlobalRenderer(AntiGooFieldGenTileEntity te) {
        return true;
    }
}
