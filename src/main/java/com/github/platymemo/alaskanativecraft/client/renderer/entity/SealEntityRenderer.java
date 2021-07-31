package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.SealEntityModel;
import com.github.platymemo.alaskanativecraft.entity.SealEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SealEntityRenderer extends MobEntityRenderer<SealEntity, SealEntityModel<SealEntity>> {
    private static final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/seal/seal.png");

    public SealEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SealEntityModel<>(ctx.getPart(AlaskaModels.SEAL)), 0.7F);
    }

    public Identifier getTexture(SealEntity sealEntity) {
        return TEXTURE;
    }
}