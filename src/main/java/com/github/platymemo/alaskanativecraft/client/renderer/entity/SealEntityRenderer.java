package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.SealEntityModel;
import com.github.platymemo.alaskanativecraft.entity.SealEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SealEntityRenderer extends MobEntityRenderer<SealEntity, SealEntityModel<SealEntity>> {
    private static final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/seal/seal.png");

    public SealEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SealEntityModel<>(0.0F), 0.7F);
    }

    public void render(SealEntity sealEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (sealEntity.isBaby()) {
            this.shadowRadius *= 0.5F;
        }

        super.render(sealEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(SealEntity sealEntity) {
        return TEXTURE;
    }

    protected void setupTransforms(SealEntity sealEntity, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(sealEntity, matrixStack, f, g, h);
        if (sealEntity.isBaby()) {
            matrixStack.scale(0.5F, 0.5F, 0.5F);
        }
    }
}