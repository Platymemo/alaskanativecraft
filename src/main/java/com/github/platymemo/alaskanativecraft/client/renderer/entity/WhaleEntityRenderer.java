package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.WhaleEntityModel;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class WhaleEntityRenderer extends MobEntityRenderer<WhaleEntity, WhaleEntityModel> {
	private static final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/whale/dev.png"); // TODO use a real texture

	public WhaleEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx, new WhaleEntityModel(ctx.getPart(AlaskaModels.WHALE)), 0.7F);
	}

	@Override
	public Identifier getTexture(WhaleEntity whale) {
		return TEXTURE;
	}
}
