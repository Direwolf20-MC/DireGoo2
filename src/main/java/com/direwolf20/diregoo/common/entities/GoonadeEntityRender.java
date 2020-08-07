package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.client.renderer.MyRenderMethods;
import com.direwolf20.diregoo.client.renderer.OurRenderTypes;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class GoonadeEntityRender extends EntityRenderer<GoonadeEntity> {

    public GoonadeEntityRender(EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0F;
    }

    @Override
    public void render(GoonadeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder;
        Minecraft mc = Minecraft.getInstance();
        builder = buffer.getBuffer(OurRenderTypes.RenderBlock);
        BlockState renderBlockState = ModBlocks.GOO_BLOCK.get().getDefaultState();


        matrixStackIn.push();
        matrixStackIn.translate(-0.0005f, -0.0005f, -0.0005f);
        matrixStackIn.scale(0.25f, 0.25f, 0.25f);//Slightly Larger block to avoid z-fighting.
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        int color = blockColors.getColor(renderBlockState, entityIn.getEntityWorld(), entityIn.func_233580_cy_(), 0);
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        IBakedModel ibakedmodel = blockrendererdispatcher.getModelForState(renderBlockState);
        for (Direction direction : Direction.values()) {
            MyRenderMethods.renderModelBrightnessColorQuads(matrixStackIn.getLast(), builder, f, f1, f2, 1f, ibakedmodel.getQuads(renderBlockState, direction, new Random(MathHelper.getPositionRandom(entityIn.func_233580_cy_())), EmptyModelData.INSTANCE), 15728640, 655360);
        }
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(GoonadeEntity entity) {
        return null;
    }
}