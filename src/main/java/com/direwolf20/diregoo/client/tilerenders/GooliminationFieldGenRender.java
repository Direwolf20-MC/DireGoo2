package com.direwolf20.diregoo.client.tilerenders;

import com.direwolf20.diregoo.common.tiles.GooliminationFieldGenTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class GooliminationFieldGenRender extends TileEntityRenderer<GooliminationFieldGenTile> {

    public GooliminationFieldGenRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(GooliminationFieldGenTile tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightsIn, int combinedOverlayIn) {

    }

    @Override
    public boolean isGlobalRenderer(GooliminationFieldGenTile te) {
        return true;
    }
}
