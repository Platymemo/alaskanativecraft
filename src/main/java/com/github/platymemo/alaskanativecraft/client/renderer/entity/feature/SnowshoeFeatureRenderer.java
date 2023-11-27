package com.github.platymemo.alaskanativecraft.client.renderer.entity.feature;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.feature.SnowshoeModel;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class SnowshoeFeatureRenderer<E extends LivingEntity, M extends BipedEntityModel<E>> extends FeatureRenderer<E, M> {
	public static final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/feature/snowshoe.png");
	private final SnowshoeModel<E> model;

	public SnowshoeFeatureRenderer(FeatureRendererContext<E, M> context, @NotNull EntityModelLoader loader) {
		super(context);
		this.model = new SnowshoeModel<>(loader.getModelPart(AlaskaModels.SNOWSHOES));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, @NotNull E entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		ItemStack snowshoeStack = entity.getEquippedStack(EquipmentSlot.FEET);
		if (snowshoeStack.isOf(AlaskaItems.SNOWSHOES)) {
			VertexConsumer armorGlint = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(TEXTURE), false, snowshoeStack.hasGlint());

			matrices.push();
			this.translateToLeg(matrices, this.getContextModel(), entity, true);
			this.model.render(matrices, armorGlint, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			matrices.pop();

			matrices.push();
			this.translateToLeg(matrices, this.getContextModel(), entity, false);
			this.model.render(matrices, armorGlint, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			matrices.pop();
		}
	}

	private void translateToLeg(MatrixStack matrices, M model, @NotNull E entity, boolean right) {
		if (entity.isInSneakingPose() && !model.riding && !entity.isSwimming()) {
			matrices.translate(0.0F, 0.0F, 0.25F);
		}

		var leg = right ? model.rightLeg : model.leftLeg;
		matrices.translate(0.115F * (right ? -1.0F : 1.0F), 0.75F, 0.0F);
		matrices.multiply(Axis.Z_POSITIVE.rotation(leg.roll));
		matrices.multiply(Axis.Y_POSITIVE.rotation(leg.yaw));
		matrices.multiply(Axis.X_POSITIVE.rotation(leg.pitch));
		// This sets it just above the armor stand base.
		float bottomOfFoot = entity instanceof ArmorStandEntity ? 0.65F : 0.7F;
		matrices.translate(0.0F, bottomOfFoot, 0.0F);
		matrices.scale(2.0F, 2.0F, 2.0F);
	}
}
