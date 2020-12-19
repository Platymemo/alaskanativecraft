package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.client.model.entity.SealEntityModel;
import com.github.platymemo.alaskanativecraft.entity.SealEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SealEntityRenderer extends MobEntityRenderer<SealEntity, SealEntityModel> {

    public SealEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, SealEntityModel entityModel, float f) {
        super(entityRenderDispatcher, entityModel, f);
    }

    @Override
    public Identifier getTexture(SealEntity entity) {
        return null;
    }
}
