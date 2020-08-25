package com.direwolf20.diregoo.common.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRendersAsItem;

public class SpriteRendererGoo<T extends Entity & IRendersAsItem> extends SpriteRenderer<T> {

    public SpriteRendererGoo(EntityRendererManager renderManagerIn) {

        super(renderManagerIn, Minecraft.getInstance().getItemRenderer());
    }

}