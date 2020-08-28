package com.direwolf20.diregoo.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.pipeline.LightUtil;

import java.util.List;

import static net.minecraft.util.Direction.*;

public class MyRenderMethods {
    public static void renderModelBrightnessColorQuads(MatrixStack.Entry matrixEntry, IVertexBuilder builder, float red, float green, float blue, float alpha, List<BakedQuad> listQuads, int combinedLightsIn, int combinedOverlayIn) {
        for (BakedQuad bakedquad : listQuads) {
            float f;
            float f1;
            float f2;

            if (bakedquad.hasTintIndex()) {
                f = red * 1f;
                f1 = green * 1f;
                f2 = blue * 1f;
            } else {
                f = 1f;
                f1 = 1f;
                f2 = 1f;
            }

            float diffuse = LightUtil.diffuseLight(bakedquad.getFace());
            f *= diffuse;
            f1 *= diffuse;
            f2 *= diffuse;
            builder.addVertexData(matrixEntry, bakedquad, f, f1, f2, alpha, combinedLightsIn, combinedOverlayIn);
        }
    }

    public static void renderModelBrightnessColorQuads(MatrixStack.Entry matrixEntry, IVertexBuilder builder, float red, float green, float blue, float alpha, List<BakedQuad> listQuads, int combinedLightsIn, int combinedOverlayIn, Direction direction) {
        for (BakedQuad bakedquad : listQuads) {
            float f;
            float f1;
            float f2;

            if (bakedquad.hasTintIndex()) {
                f = red * 1f;
                f1 = green * 1f;
                f2 = blue * 1f;
            } else {
                f = 1f;
                f1 = 1f;
                f2 = 1f;
            }

            Direction quadDirection = bakedquad.getFace();
            if (direction == DOWN) {
                quadDirection = rotateAround(rotateAround(quadDirection, Axis.X), Axis.X);
            } else if (direction == NORTH) {
                quadDirection = rotateAround(quadDirection, Axis.X);
            } else if (direction == SOUTH) {
                quadDirection = rotateAround(rotateAround(rotateAround(quadDirection, Axis.X), Axis.X), Axis.X);
            } else if (direction == WEST) {
                quadDirection = rotateAround(rotateAround(rotateAround(quadDirection, Axis.Z), Axis.Z), Axis.Z);
            } else if (direction == EAST) {
                quadDirection = rotateAround(quadDirection, Axis.Z);
            }


            float diffuse = LightUtil.diffuseLight(quadDirection);
            f *= diffuse;
            f1 *= diffuse;
            f2 *= diffuse;
            builder.addVertexData(matrixEntry, bakedquad, f, f1, f2, alpha, combinedLightsIn, combinedOverlayIn);
        }
    }

    public static Direction rotateAround(Direction dir, Direction.Axis axis) {
        switch (axis) {
            case X:
                if (dir != WEST && dir != EAST) {
                    return rotateX(dir);
                }

                return dir;
            case Y:
                if (dir != UP && dir != DOWN) {
                    return dir.rotateY();
                }

                return dir;
            case Z:
                if (dir != NORTH && dir != SOUTH) {
                    return rotateZ(dir);
                }

                return dir;
            default:
                throw new IllegalStateException("Unable to get CW facing for axis " + axis);
        }
    }

    public static Direction rotateX(Direction dir) {
        switch (dir) {
            case NORTH:
                return DOWN;
            case EAST:
            case WEST:
            default:
                throw new IllegalStateException("Unable to get X-rotated facing of " + dir);
            case SOUTH:
                return UP;
            case UP:
                return NORTH;
            case DOWN:
                return SOUTH;
        }
    }

    public static Direction rotateZ(Direction dir) {
        switch (dir) {
            case EAST:
                return DOWN;
            case SOUTH:
            default:
                throw new IllegalStateException("Unable to get Z-rotated facing of " + dir);
            case WEST:
                return UP;
            case UP:
                return EAST;
            case DOWN:
                return WEST;
        }
    }
}
