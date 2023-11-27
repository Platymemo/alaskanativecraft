package com.github.platymemo.alaskanativecraft.client.renderer.entity.feature;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.feature.KuspukSkirtModel;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class KuspukSkirtFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends FeatureRenderer<E, M> {
	private static final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/feature/skirt_layer.png");
	private final KuspukSkirtModel<E> model;

	public KuspukSkirtFeatureRenderer(FeatureRendererContext<E, M> context, @NotNull EntityModelLoader loader) {
		super(context);
		this.model = new KuspukSkirtModel<>(loader.getModelPart(AlaskaModels.KUSPUK_SKIRT));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, @NotNull E livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		ItemStack kuspukStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
		if (kuspukStack.isOf(AlaskaItems.KUSPUK_BODY)) {
			int color = AlaskaItems.KUSPUK_BODY.getColor(kuspukStack);
			// Unpack hex into 0.0-1.0 floats
			float r = (float) (color >> 16 & 255) / 255.0F;
			float g = (float) (color >> 8 & 255) / 255.0F;
			float b = (float) (color & 255) / 255.0F;

			matrices.push();

			if (livingEntity.isInSneakingPose()) {
				matrices.translate(0.0D, -2.55D, 0.3D);
			} else {
				matrices.translate(0.0D, -2.7D, 0.0D);
			}

			matrices.scale(2.5F, 2.5F, 2.5F);

			VertexConsumer armorGlint = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(TEXTURE), false, kuspukStack.hasGlint());
			this.model.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			this.model.render(matrices, armorGlint, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
			matrices.pop();
		}
	}
}
