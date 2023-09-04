package com.github.platymemo.alaskanativecraft.client.renderer.item;

import com.github.platymemo.alaskanativecraft.client.model.entity.feature.SnowshoeModel;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.SnowshoeFeatureRenderer;
import org.jetbrains.annotations.NotNull;

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
public enum SnowshoeItemRenderer {
    INSTANCE;
    private final SnowshoeModel<LivingEntity> snowshoeModel = new SnowshoeModel<>(SnowshoeModel.getTexturedModelData().createModel());

    public boolean render(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, @NotNull MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, @NotNull BakedModel model) {
        matrices.push();
        {
            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);

            if (renderMode.isFirstPerson() || renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) {
                matrices.multiply(Axis.X_POSITIVE.rotationDegrees(90));
                matrices.translate(0, -0.05, -0.15);
                matrices.scale(2.0F, 2.0F, 2.0F);
            } else {
                matrices.multiply(Axis.X_POSITIVE.rotationDegrees(90));
                matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(45));
                matrices.scale(1.5F, 1.5F, 1.5F);
            }

            VertexConsumer snowshoe = ItemRenderer.getArmorGlintConsumer(
                    vertexConsumers,
                    this.snowshoeModel.getLayer(SnowshoeFeatureRenderer.TEXTURE),
                    false,
                    stack.hasGlint()
            );
            this.snowshoeModel.render(matrices, snowshoe, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        matrices.pop();
        return true;
    }
}
