package com.direwolf20.diregoo.client.renderer;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

public class OurRenderTypes extends RenderType {
    public final static ResourceLocation laserBeam = new ResourceLocation(DireGoo.MOD_ID + ":textures/misc/laser.png");
    public final static ResourceLocation laserBeam2 = new ResourceLocation(DireGoo.MOD_ID + ":textures/misc/laser2.png");
    public final static ResourceLocation laserBeamGlow = new ResourceLocation(DireGoo.MOD_ID + ":textures/misc/laser_glow.png");

    // Dummy
    public OurRenderTypes(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable runnablePre, Runnable runnablePost) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, runnablePre, runnablePost);
    }

    public static void updateRenders() {
        //Used for debugging, so we can change the values without restarting instance. Not needed in normal use.
        RenderBlock = makeType("GooEntity",
                DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256,
                RenderType.State.getBuilder()
                        .shadeModel(RenderState.SHADE_ENABLED)
                        .lightmap(RenderState.LIGHTMAP_ENABLED)
                        .texture(RenderState.BLOCK_SHEET_MIPPED)
                        .layer(RenderState.field_239235_M_)
                        .transparency(TRANSLUCENT_TRANSPARENCY)
                        .depthTest(RenderState.DEPTH_LEQUAL)
                        .cull(RenderState.CULL_ENABLED)
                        .writeMask(RenderState.COLOR_WRITE)
                        .build(false));
    }

    public static RenderType RenderBlock = makeType("GooEntity",
            DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder()
                    .shadeModel(RenderState.SHADE_ENABLED)
                    .lightmap(RenderState.LIGHTMAP_ENABLED)
                    .texture(RenderState.BLOCK_SHEET_MIPPED)
                    .layer(RenderState.field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .depthTest(RenderState.DEPTH_LEQUAL)
                    .cull(RenderState.CULL_ENABLED)
                    .writeMask(RenderState.COLOR_WRITE)
                    .build(false));

    public static RenderType RenderScanner = makeType("RenderScanner",
            DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder()
                    .layer(field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .texture(BLOCK_SHEET_MIPPED)
                    .depthTest(DEPTH_ALWAYS)
                    .cull(CULL_ENABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_DEPTH_WRITE)
                    .build(false));

    public static RenderType BlockOverlay = makeType("MiningLaserBlockOverlay",
            DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder()
                    .layer(field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .texture(NO_TEXTURE)
                    .depthTest(DEPTH_LEQUAL)
                    .cull(CULL_ENABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_DEPTH_WRITE)
                    .build(false));

    public static RenderType LASER_MAIN_BEAM = makeType("MiningLaserMainBeam",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder().texture(new TextureState(laserBeam2, false, false))
                    .layer(field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .depthTest(RenderState.DEPTH_LEQUAL)
                    .cull(CULL_DISABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_WRITE)
                    .build(false));

    public static final RenderType LASER_MAIN_ADDITIVE = makeType("MiningLaserAdditiveBeam",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder().texture(new TextureState(laserBeamGlow, false, false))
                    .layer(field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .depthTest(DEPTH_ALWAYS)
                    .cull(CULL_DISABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_WRITE)
                    .build(false));

    public static RenderType LASER_MAIN_CORE = makeType("MiningLaserCoreBeam",
            DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder().texture(new TextureState(laserBeam, false, false))
                    .layer(field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .depthTest(DEPTH_LEQUAL)
                    .cull(CULL_DISABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_DEPTH_WRITE)
                    .build(false));

    public static RenderType AntiGooOverlay = makeType("AntiGooOverlay",
            DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder()
                    .layer(field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .texture(NO_TEXTURE)
                    .depthTest(DEPTH_LEQUAL)
                    .cull(CULL_DISABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_WRITE)
                    .build(false));

    public static RenderType AntiGooLines = makeType("AntiGooLines",
            DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINE_STRIP, 256,
            RenderType.State.getBuilder()
                    .line(new LineState(OptionalDouble.of(2.0D)))
                    .layer(field_239235_M_)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .texture(NO_TEXTURE)
                    .depthTest(DEPTH_LEQUAL)
                    .cull(CULL_DISABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_WRITE)
                    .build(false));
}
