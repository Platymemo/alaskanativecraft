package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.MooseEntityModel;
import com.github.platymemo.alaskanativecraft.entity.MooseEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class MooseEntityRenderer extends MobEntityRenderer<MooseEntity, MooseEntityModel<MooseEntity>> {
    private static final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/moose.png");

    public MooseEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MooseEntityModel<>(ctx.getPart(AlaskaModels.MOOSE)), 1.5F);
    }

    @Override
    public Identifier getTexture(MooseEntity mooseEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(MooseEntity entity, MatrixStack matrices, float amount) {
        super.scale(entity, matrices, amount);
        matrices.scale(2.0F, 2.0F, 2.0F);
    }
}
