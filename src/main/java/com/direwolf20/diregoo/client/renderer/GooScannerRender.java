package com.direwolf20.diregoo.client.renderer;

import com.direwolf20.diregoo.common.blocks.GooBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.*;
import java.util.stream.Collectors;

public class GooScannerRender {

    public static long gooVisibleStartTime;
    public static List<BlockPos> gooBlocksList = new ArrayList<>();

    public static void renderGoo(RenderWorldLastEvent evt) {
        if (((System.currentTimeMillis() - gooVisibleStartTime) / 1000) >= 10) return;
        if (gooBlocksList.equals(new HashSet<>()))
            discoverGoo(Minecraft.getInstance().player);

        final Minecraft mc = Minecraft.getInstance();
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        PlayerEntity player = mc.player;
        World world = player.world;
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

        Vector3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();

        MatrixStack matrix = evt.getMatrixStack();
        matrix.push();
        matrix.translate(-view.getX(), -view.getY(), -view.getZ());

        IVertexBuilder builder;
        builder = buffer.getBuffer(OurRenderTypes.RenderScanner);
        gooBlocksList.sort(Comparator.comparingDouble(blockPos -> player.getEyePosition(evt.getPartialTicks()).distanceTo(new Vector3d(blockPos.getX() + .5, blockPos.getY(), blockPos.getZ() + .5))));
        Collections.reverse(gooBlocksList);

        gooBlocksList.forEach(e -> {
            matrix.push();
            matrix.translate(e.getX(), e.getY(), e.getZ());
            matrix.translate(-0.005f, -0.005f, -0.005f);
            matrix.scale(1.01f, 1.01f, 1.01f);

            BlockState gooBlockState = world.getBlockState(e);
            BlockColors blockColors = Minecraft.getInstance().getBlockColors();
            int color = blockColors.getColor(gooBlockState, world, e, 0);
            float f = (float) (color >> 16 & 255) / 255.0F;
            float f1 = (float) (color >> 8 & 255) / 255.0F;
            float f2 = (float) (color & 255) / 255.0F;

            IBakedModel ibakedmodel = blockrendererdispatcher.getModelForState(gooBlockState);
            for (Direction direction : Direction.values()) {
                MyRenderMethods.renderModelBrightnessColorQuads(matrix.getLast(), builder, f, f1, f2, 1f, ibakedmodel.getQuads(gooBlockState, direction, new Random(MathHelper.getPositionRandom(e)), EmptyModelData.INSTANCE), 15728640, 655360);
            }
            matrix.pop();
        });
        matrix.pop();
        RenderSystem.disableDepthTest();
        buffer.finish(OurRenderTypes.RenderScanner);
        RenderSystem.enableDepthTest();
    }

    public static void discoverGoo(PlayerEntity player) {
        BlockPos playerPos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
        gooBlocksList = BlockPos.getAllInBox(playerPos.add(-50, -50, -50), playerPos.add(50, 50, 50))
                .filter(blockPos -> player.world.getBlockState(blockPos).getBlock() instanceof GooBase)
                .map(BlockPos::toImmutable)
                .sorted(Comparator.comparingDouble(blockPos -> playerPos.distanceSq(blockPos)))
                .collect(Collectors.toList());
        Collections.reverse(gooBlocksList);
        gooVisibleStartTime = System.currentTimeMillis();
    }
}
