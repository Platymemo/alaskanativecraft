package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.AlaskaNativeCraftModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.MooseEntityModel;
import com.github.platymemo.alaskanativecraft.entity.MooseEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MooseEntityRenderer extends MobEntityRenderer<MooseEntity, MooseEntityModel<MooseEntity>> {
    private static final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/moose.png");

    public MooseEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MooseEntityModel<>(ctx.getPart(AlaskaNativeCraftModels.MOOSE_MODEL)), 1.5F);
    }

    public Identifier getTexture(MooseEntity mooseEntity) {
        return TEXTURE;
    }

    protected void setupTransforms(MooseEntity mooseEntity, MatrixStack matrixStack, float f, float g, float h) {
        if (!mooseEntity.isBaby()) {
            super.setupTransforms(mooseEntity, matrixStack, f, g, h);
            matrixStack.scale(2.0F, 2.0F, 2.0F);
        } else {
            super.setupTransforms(mooseEntity, matrixStack, f, g, h);
        }
    }
}