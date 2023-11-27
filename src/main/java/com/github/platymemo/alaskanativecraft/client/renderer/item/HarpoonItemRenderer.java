package com.github.platymemo.alaskanativecraft.client.renderer.item;

import com.github.platymemo.alaskanativecraft.client.model.entity.HarpoonEntityModel;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.HarpoonEntityRenderer;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Axis;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class HarpoonItemRenderer {
	private static final HarpoonEntityModel HARPOON_ENTITY_MODEL = new HarpoonEntityModel(HarpoonEntityModel.getTexturedModelData().createModel());

	public static boolean render(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
		if (renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED) {
			return false;
		}

		matrices.push();
		{
			model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);

			if (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && (renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {
				matrices.multiply(Axis.X_POSITIVE.rotationDegrees(180));
				matrices.translate(0, 2, 0);
			} else {
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(90));
				matrices.translate(0, 0.85, 0);
			}

			matrices.scale(2.0F, -2.0F, -2.0F);
			VertexConsumer harpoon = ItemRenderer.getArmorGlintConsumer(
					vertexConsumers,
					HARPOON_ENTITY_MODEL.getLayer(HarpoonEntityRenderer.getTexture(((HarpoonItem) stack.getItem()).getType())),
					false,
					stack.hasGlint()
			);
			HARPOON_ENTITY_MODEL.render(matrices, harpoon, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		matrices.pop();
		return true;
	}
}
