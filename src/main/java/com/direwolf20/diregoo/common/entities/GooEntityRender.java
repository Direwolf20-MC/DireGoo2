package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.client.renderer.MyRenderMethods;
import com.direwolf20.diregoo.client.renderer.OurRenderTypes;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.List;
import java.util.Random;

public class GooEntityRender extends EntityRenderer<GooEntity> {

    public GooEntityRender(EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0F;
    }

    @Override
    public void render(GooEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder;
        builder = buffer.getBuffer(OurRenderTypes.RenderBlock);
        BlockState gooBlockState = entityIn.getGooBlockState();
        BlockState renderBlockState = gooBlockState != null ? gooBlockState : Blocks.AIR.getDefaultState();
        int teCounter = entityIn.ticksExisted;
        int maxLife = entityIn.getMaxLife();
        teCounter = Math.min(teCounter, maxLife);
        float scale = (float) (maxLife - teCounter) / maxLife;

        matrixStackIn.push();
        matrixStackIn.translate(-0.0005f, -0.0005f, -0.0005f);
        matrixStackIn.scale(1.001f, 1.001f, 1.001f);//Slightly Larger block to avoid z-fighting.
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        int color = blockColors.getColor(renderBlockState, entityIn.getEntityWorld(), entityIn.func_233580_cy_(), 0);
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        IBakedModel ibakedmodel = blockrendererdispatcher.getModelForState(renderBlockState);
        for (Direction direction : Direction.values()) {
            //The below logic checks to see if there is a different render goo entity in the blockspace next to this one.
            //It then checks to see if this goo is 'older' than that one
            //Don't render the specified side if thats the case (This looks better visually)
            boolean shouldRenderSide = true;
            BlockPos tempPos = new BlockPos(entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ()).offset(direction);
            List<GooEntity> list = entityIn.world.getEntitiesWithinAABB(GooEntity.class, new AxisAlignedBB(tempPos.getX(), tempPos.getY(), tempPos.getZ(), tempPos.getX() + 0.25d, tempPos.getY() + 0.25d, tempPos.getZ() + 0.25d));
            for (GooEntity entity : list) {
                if ((entity.getEntityId() != entityIn.getEntityId()) && (entityIn.ticksExisted >= entity.ticksExisted))
                    shouldRenderSide = false;
            }
            if (shouldRenderSide)
                MyRenderMethods.renderModelBrightnessColorQuads(matrixStackIn.getLast(), builder, f, f1, f2, scale, ibakedmodel.getQuads(renderBlockState, direction, new Random(MathHelper.getPositionRandom(entityIn.func_233580_cy_())), EmptyModelData.INSTANCE), 15728640, 655360);
        }
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(GooEntity entity) {
        return null;
    }
}