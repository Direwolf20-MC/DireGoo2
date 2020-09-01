package com.direwolf20.diregoo.common.entities;

import com.direwolf20.diregoo.client.renderer.MyRenderMethods;
import com.direwolf20.diregoo.client.renderer.OurRenderTypes;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.blocks.goospreadblocks.GooRender;
import com.direwolf20.diregoo.common.blocks.goospreadblocks.GooRenderBase;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class GooSpreadEntityRender extends EntityRenderer<GooSpreadEntity> {

    public GooSpreadEntityRender(EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0F;
    }

    @Override
    public void render(GooSpreadEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder;
        builder = buffer.getBuffer(OurRenderTypes.RenderBlockSpread);
        BlockState gooBlockState = entityIn.getGooBlockState();
        int teCounter = entityIn.ticksExisted;
        int maxLife = entityIn.getMaxLife();
        teCounter = Math.min(teCounter, maxLife);
        float ticksPerPhase = maxLife / 10;
        float phaseIncrement = 100 / ticksPerPhase;
        float scale = ((float) (teCounter) / maxLife) * 10;
        int stateRender = (int) Math.ceil(scale);
        if (stateRender < 1) stateRender = 1;
        float currentAlphaIncrement = ((teCounter - ((stateRender - 1) * ticksPerPhase)) * phaseIncrement) / 100;
        float prevAlphaIncrement = currentAlphaIncrement == 0 ? 0 : (((teCounter - 1) - ((stateRender - 1) * ticksPerPhase)) * phaseIncrement) / 100;

        float renderAlphaScale = MathHelper.lerp(partialTicks, prevAlphaIncrement, currentAlphaIncrement);

        BlockState renderBlockState = Blocks.AIR.getDefaultState();
        BlockState renderBlockState2 = Blocks.AIR.getDefaultState();
        if (stateRender == 10) {
            renderBlockState = gooBlockState;
        } else if (stateRender == 9) {
            if (gooBlockState.getBlock().equals(ModBlocks.GOO_BLOCK.get()))
                renderBlockState = ModBlocks.GOO_RENDER.get().getDefaultState().with(GooRenderBase.GROWTH, stateRender);
            else if (gooBlockState.getBlock().equals(ModBlocks.GOO_BLOCK_TERRAIN.get()))
                renderBlockState = ModBlocks.GOO_RENDER_TERRAIN.get().getDefaultState().with(GooRenderBase.GROWTH, stateRender);
            else if (gooBlockState.getBlock().equals(ModBlocks.GOO_BLOCK_BURST.get()))
                renderBlockState = ModBlocks.GOO_RENDER_BURST.get().getDefaultState().with(GooRenderBase.GROWTH, stateRender);
            renderBlockState2 = gooBlockState;
        } else {
            if (gooBlockState.getBlock().equals(ModBlocks.GOO_BLOCK.get())) {
                renderBlockState = ModBlocks.GOO_RENDER.get().getDefaultState().with(GooRenderBase.GROWTH, stateRender);
                renderBlockState2 = ModBlocks.GOO_RENDER.get().getDefaultState().with(GooRender.GROWTH, stateRender + 1);
            } else if (gooBlockState.getBlock().equals(ModBlocks.GOO_BLOCK_TERRAIN.get())) {
                renderBlockState = ModBlocks.GOO_RENDER_TERRAIN.get().getDefaultState().with(GooRenderBase.GROWTH, stateRender);
                renderBlockState2 = ModBlocks.GOO_RENDER_TERRAIN.get().getDefaultState().with(GooRender.GROWTH, stateRender + 1);
            } else if (gooBlockState.getBlock().equals(ModBlocks.GOO_BLOCK_BURST.get())) {
                renderBlockState = ModBlocks.GOO_RENDER_BURST.get().getDefaultState().with(GooRenderBase.GROWTH, stateRender);
                renderBlockState2 = ModBlocks.GOO_RENDER_BURST.get().getDefaultState().with(GooRender.GROWTH, stateRender + 1);
            }
        }

        Direction growDirection = entityIn.getDirection().getOpposite();

        matrixStackIn.push();

        matrixStackIn.translate(-0.0005f, -0.0005f, -0.0005f);
        matrixStackIn.scale(1.001f, 1.001f, 1.001f);//Slightly Larger block to avoid z-fighting.
        switch (growDirection.getIndex()) {
            case 0:
                //Down
                matrixStackIn.translate(0, 1, 1);
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
                break;
            case 1:
                //Up - No changes
                break;
            case 2:
                //North
                matrixStackIn.translate(0, 0, 1);
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(270.0F));
                break;
            case 3:
                //South
                matrixStackIn.translate(0, 1, 0);
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
                break;
            case 4:
                //West
                matrixStackIn.translate(1, 0, 0);
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
                break;
            case 5:
                //East
                matrixStackIn.translate(0, 1, 0);
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(270.0F));
                break;

        }

        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        int color = blockColors.getColor(renderBlockState, entityIn.getEntityWorld(), entityIn.func_233580_cy_(), 0);
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        int lightValue = 15728640;

        IBakedModel ibakedmodel = blockrendererdispatcher.getModelForState(renderBlockState);
        for (Direction direction : Direction.values()) {
            if (!direction.equals(Direction.UP) && !direction.equals(Direction.DOWN))
                MyRenderMethods.renderModelBrightnessColorQuads(matrixStackIn.getLast(), builder, f, f1, f2, 1f, ibakedmodel.getQuads(renderBlockState, direction, new Random(MathHelper.getPositionRandom(entityIn.func_233580_cy_())), EmptyModelData.INSTANCE), lightValue, 0, growDirection);
            if (direction.equals(Direction.UP) && stateRender == 10)
                MyRenderMethods.renderModelBrightnessColorQuads(matrixStackIn.getLast(), builder, f, f1, f2, 1f, ibakedmodel.getQuads(renderBlockState, direction, new Random(MathHelper.getPositionRandom(entityIn.func_233580_cy_())), EmptyModelData.INSTANCE), lightValue, 0, growDirection);
        }

        ibakedmodel = blockrendererdispatcher.getModelForState(renderBlockState2);
        for (Direction direction : Direction.values()) {
            if (!direction.equals(Direction.UP) && !direction.equals(Direction.DOWN))
                MyRenderMethods.renderModelBrightnessColorQuads(matrixStackIn.getLast(), builder, f, f1, f2, (renderAlphaScale), ibakedmodel.getQuads(renderBlockState2, direction, new Random(MathHelper.getPositionRandom(entityIn.func_233580_cy_())), EmptyModelData.INSTANCE), lightValue, 655360, growDirection);
            if (direction.equals(Direction.UP) && stateRender >= 9)
                MyRenderMethods.renderModelBrightnessColorQuads(matrixStackIn.getLast(), builder, f, f1, f2, (renderAlphaScale), ibakedmodel.getQuads(renderBlockState2, direction, new Random(MathHelper.getPositionRandom(entityIn.func_233580_cy_())), EmptyModelData.INSTANCE), lightValue, 655360, growDirection);
        }
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(GooSpreadEntity entity) {
        return null;
    }
}