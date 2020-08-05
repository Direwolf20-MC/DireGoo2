package com.direwolf20.diregoo.client.renderer;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

public class OurRenderTypes extends RenderType {
    // Dummy
    public OurRenderTypes(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable runnablePre, Runnable runnablePost) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, runnablePre, runnablePost);
    }

    public static final RenderType RenderBlock = makeType("GooEntity",
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
