package com.direwolf20.diregoo.client.renderer;

import com.direwolf20.diregoo.client.renderer.util.MyRenderMethods;
import com.direwolf20.diregoo.client.renderer.util.OurRenderTypes;
import com.direwolf20.diregoo.common.blocks.GooBase;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.io.Closeable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GooScannerRender {

    public static long gooVisibleStartTime;
    private static int sortCounter;
    private static MultiVBORenderer renderBuffer;
    public static List<BlockPos> gooBlocksList = new ArrayList<>();

    public static void renderGoo(RenderWorldLastEvent evt) {
        if (((System.currentTimeMillis() - gooVisibleStartTime) / 1000) >= 10) return;
        if (gooBlocksList.equals(new HashSet<>()) || renderBuffer == null)
            discoverGoo(Minecraft.getInstance().player);

        drawVBO(evt.getMatrixStack());
    }

    public static void drawVBO(MatrixStack matrix) {
        //This method draws the cached RenderBuffer on the screen
        Vector3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        try {
            //This sorts the vertices based on players current position, if you're doing transparency its important to look good
            //But it definitely costs CPU time, so if you are drawing a LOT of blocks this will kill FPS
            //In this instance, i have it only resorting every so often, and only if the block list isn't HUGE.
            if (gooBlocksList.size() <= 50000) {
                if (sortCounter == 0) {
                    renderBuffer.sort((float) projectedView.getX(), (float) projectedView.getY(), (float) projectedView.getZ());
                    sortCounter = 100;
                } else
                    sortCounter--;
            }
        } catch (Exception ignored) {
        }
        RenderSystem.disableDepthTest();
        matrix.translate(-projectedView.getX(), -projectedView.getY(), -projectedView.getZ());
        renderBuffer.render(matrix.getLast().getMatrix()); //Actually draw whats in the buffer
        RenderSystem.enableDepthTest();
    }

    public static void discoverGoo(PlayerEntity player) {
        //Find all the goo in an area around the player
        BlockPos playerPos = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
        int radius = 100;
        if (!player.isSneaking())
            gooBlocksList = BlockPos.getAllInBox(playerPos.add(-radius, -radius, -radius), playerPos.add(radius, radius, radius))
                    .filter(blockPos -> player.world.getBlockState(blockPos).getBlock() instanceof GooBase)
                    .map(BlockPos::toImmutable)
                    .sorted(Comparator.comparingDouble(blockPos -> playerPos.distanceSq(blockPos)))
                    .collect(Collectors.toList());
        else
            gooBlocksList = BlockPos.getAllInBox(playerPos.add(-radius, -radius, -radius), playerPos.add(radius, radius, radius))
                    .filter(blockPos -> player.world.getBlockState(blockPos).getBlock() instanceof GooBase && player.world.getBlockState(blockPos).get(GooBase.ACTIVE))
                    .map(BlockPos::toImmutable)
                    .sorted(Comparator.comparingDouble(blockPos -> playerPos.distanceSq(blockPos)))
                    .collect(Collectors.toList());
        if (gooBlocksList.size() > 100000) {
            player.sendStatusMessage(new TranslationTextComponent("message.diregoo.toomuchgoo", gooBlocksList.size()), true);
        } else {
            player.sendStatusMessage(new TranslationTextComponent("message.diregoo.goocount", gooBlocksList.size()), true);
            Collections.reverse(gooBlocksList);
            gooVisibleStartTime = System.currentTimeMillis();
            //System.out.println(gooBlocksList.size());
            renderVBO();
        }
    }

    public static void renderVBO() {
        PlayerEntity player = Minecraft.getInstance().player;
        World world = player.world;
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        //Populate the renderBuffer variable
        renderBuffer = MultiVBORenderer.of((buffer) -> {
            IVertexBuilder builder;
            builder = buffer.getBuffer(OurRenderTypes.RenderScanner);

            MatrixStack matrix = new MatrixStack(); //Create a new matrix stack for use in the buffer building process
            matrix.push(); //Save position

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
        });
    }


    /**
     * Vertex Buffer Object for caching the render. Pretty similar to how the chunk caching works
     */
    public static class MultiVBORenderer implements Closeable {
        private static final int BUFFER_SIZE = 2 * 1024 * 1024 * 3;

        public static MultiVBORenderer of(Consumer<IRenderTypeBuffer> vertexProducer) {
            final Map<RenderType, BufferBuilder> builders = Maps.newHashMap();

            vertexProducer.accept(rt -> builders.computeIfAbsent(rt, (_rt) -> {
                BufferBuilder builder = new BufferBuilder(BUFFER_SIZE);
                builder.begin(_rt.getDrawMode(), _rt.getVertexFormat());

                return builder;
            }));

            Map<RenderType, BufferBuilder.State> sortCaches = Maps.newHashMap();
            Map<RenderType, VertexBuffer> buffers = Maps.transformEntries(builders, (rt, builder) -> {
                Objects.requireNonNull(rt);
                Objects.requireNonNull(builder);
                sortCaches.put(rt, builder.getVertexState());

                builder.finishDrawing();
                VertexFormat fmt = rt.getVertexFormat();
                VertexBuffer vbo = new VertexBuffer(fmt);

                vbo.upload(builder);
                return vbo;
            });

            return new MultiVBORenderer(buffers, sortCaches);
        }

        private final ImmutableMap<RenderType, VertexBuffer> buffers;
        private final ImmutableMap<RenderType, BufferBuilder.State> sortCaches;

        protected MultiVBORenderer(Map<RenderType, VertexBuffer> buffers, Map<RenderType, BufferBuilder.State> sortCaches) {
            this.buffers = ImmutableMap.copyOf(buffers);
            this.sortCaches = ImmutableMap.copyOf(sortCaches);
        }

        public void sort(float x, float y, float z) {
            for (Map.Entry<RenderType, BufferBuilder.State> kv : sortCaches.entrySet()) {
                RenderType rt = kv.getKey();
                BufferBuilder.State state = kv.getValue();
                BufferBuilder builder = new BufferBuilder(BUFFER_SIZE);
                builder.begin(rt.getDrawMode(), rt.getVertexFormat());
                builder.setVertexState(state);
                builder.sortVertexData(x, y, z);
                builder.finishDrawing();

                VertexBuffer vbo = buffers.get(rt);
                vbo.upload(builder);
            }
        }

        public void render(Matrix4f matrix) {
            buffers.forEach((rt, vbo) -> {
                VertexFormat fmt = rt.getVertexFormat();

                rt.setupRenderState();
                vbo.bindBuffer();
                fmt.setupBufferState(0L);
                vbo.draw(matrix, rt.getDrawMode());
                VertexBuffer.unbindBuffer();
                fmt.clearBufferState();
                rt.clearRenderState();
            });
        }

        public void close() {
            buffers.values().forEach(VertexBuffer::close);
        }
    }
}
