package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import java.util.HashMap;
import java.util.Map;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.HarpoonEntityModel;
import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;

public class HarpoonEntityRenderer extends EntityRenderer<HarpoonEntity> {
	private static final Map<EntityType<?>, Identifier> TEXTURES = new HashMap<>();
	private final HarpoonEntityModel model;

	public HarpoonEntityRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		this.model = new HarpoonEntityModel(ctx.getPart(AlaskaModels.HARPOON));
	}

	public static Identifier getTexture(EntityType<?> type) {
		if (!TEXTURES.containsKey(type)) {
			TEXTURES.put(type, new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/harpoon/" + Registries.ENTITY_TYPE.getId(type).getPath() + ".png"));
		}

		return TEXTURES.get(type);
	}

	@Override
	public void render(@NotNull HarpoonEntity harpoon, float yaw, float tickDelta, @NotNull MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		{
			matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(MathHelper.lerp(tickDelta, harpoon.prevYaw, harpoon.getYaw()) - 90.0F));
			matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(MathHelper.lerp(tickDelta, harpoon.prevPitch, harpoon.getPitch()) + 90.0F));
			VertexConsumer itemGlint = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.model.getLayer(this.getTexture(harpoon)), false, harpoon.isEnchanted());
			this.model.render(matrices, itemGlint, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			matrices.scale(2.0F, -2.0F, -2.0F);
		}

		matrices.pop();
		super.render(harpoon, yaw, tickDelta, matrices, vertexConsumers, light);
	}

	@Override
	public Identifier getTexture(@NotNull HarpoonEntity harpoon) {
		return getTexture(harpoon.getType());
	}
}
