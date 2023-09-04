package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.math.MathHelper;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
	protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}

	@ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;animateModel(Lnet/minecraft/entity/Entity;FFF)V"),
			method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			ordinal = 7)
	private float changeLimbDistance(float limbDistance, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		if (livingEntity instanceof WolfEntity && livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof DogsledEntity) {
			limbDistance = MathHelper.lerp(g, livingEntity.limbData.getLimbAngle(g), livingEntity.limbData.getLimbDistance(g));

			if (limbDistance > 1.0F) {
				limbDistance = 1.0F;
			}
		}

		return limbDistance;
	}

	@ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;animateModel(Lnet/minecraft/entity/Entity;FFF)V"),
			method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			ordinal = 8)
	private float changeLimbAngle(float limbAngle, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		if (livingEntity instanceof WolfEntity && livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof DogsledEntity) {
			limbAngle = livingEntity.limbData.getLimbAngle(g) - livingEntity.limbData.getLimbDistance(g) * (1.0F - g);
		}

		return limbAngle;
	}
}
