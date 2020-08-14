package com.direwolf20.diregoo.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;

public class MyRenderHelper {

    public static void rotateToPlayer(MatrixStack matrixStack) {
        Quaternion rotation = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getRotation();
        matrixStack.rotate(rotation);
    }

    public static void drawBeam(Matrix4f matrix, IVertexBuilder builder, TextureAtlasSprite sprite, Vector S, Vector E, Vector P, float width) {
        Vector PS = Sub(S, P);
        Vector SE = Sub(E, S);

        Vector normal = Cross(PS, SE);
        normal = normal.normalize();

        Vector half = Mul(normal, width);
        Vector p1 = Add(S, half);
        Vector p2 = Sub(S, half);
        Vector p3 = Add(E, half);
        Vector p4 = Sub(E, half);

        drawQuad(matrix, builder, sprite, p1, p3, p4, p2, 1, 0, 0, 1);
    }

    public static void drawQuad(Matrix4f matrix, IVertexBuilder buffer, TextureAtlasSprite sprite, Vector p1, Vector p2, Vector p3, Vector p4,
                                int r, int g, int b, int a) {

        vt(buffer, matrix, p1.getX(), p1.getY(), p1.getZ(), sprite.getMinU(), sprite.getMinV(), 65535, 65535, r, g, b, a);
        vt(buffer, matrix, p2.getX(), p2.getY(), p2.getZ(), sprite.getMaxU(), sprite.getMinV(), 65535, 65535, r, g, b, a);
        vt(buffer, matrix, p3.getX(), p3.getY(), p3.getZ(), sprite.getMaxU(), sprite.getMaxV(), 65535, 65535, r, g, b, a);
        vt(buffer, matrix, p4.getX(), p4.getY(), p4.getZ(), sprite.getMinU(), sprite.getMaxV(), 65535, 65535, r, g, b, a);
    }

    public static void vt(IVertexBuilder renderer, Matrix4f matrix, float x, float y, float z, float u, float v, int lu, int lv, int r, int g, int b, int a) {
        renderer
                .pos(matrix, x, y, z)
                .color(r, g, b, a)
                .tex(u, v)
                .lightmap(lu, lv)
                .overlay(OverlayTexture.NO_OVERLAY)
                .normal(1, 0, 0)
                .endVertex();
    }

    public static class Vector {
        public final float x;
        public final float y;
        public final float z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static Vector Vector(float x, float y, float z) {
            return new Vector(x, y, z);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public float norm() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        public Vector normalize() {
            float n = norm();
            return new Vector(x / n, y / n, z / n);
        }
    }

    public static Vector Cross(Vector a, Vector b) {
        float x = a.y * b.z - a.z * b.y;
        float y = a.z * b.x - a.x * b.z;
        float z = a.x * b.y - a.y * b.x;
        return new Vector(x, y, z);
    }

    public static Vector Sub(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector Add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector Mul(Vector a, float f) {
        return new Vector(a.x * f, a.y * f, a.z * f);
    }

}
