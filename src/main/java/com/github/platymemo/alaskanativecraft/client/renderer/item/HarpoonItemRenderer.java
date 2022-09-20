package com.github.platymemo.alaskanativecraft.client.renderer.item;

import com.github.platymemo.alaskanativecraft.client.model.entity.HarpoonEntityModel;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.HarpoonEntityRenderer;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public enum HarpoonItemRenderer {
    INSTANCE;
    private final HarpoonEntityModel harpoonEntityModel = new HarpoonEntityModel(HarpoonEntityModel.getTexturedModelData().createModel());

    public boolean render(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        if (renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED) {
            return false;
        }

        matrices.push();
        {
            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);

            if (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && (renderMode == ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND || renderMode == ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND)) {
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
                matrices.translate(0, 2, 0);
            } else {
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
                matrices.translate(0, 0.85, 0);
            }

            matrices.scale(2.0F, -2.0F, -2.0F);
            VertexConsumer harpoon = ItemRenderer.getArmorGlintConsumer(
                    vertexConsumers,
                    this.harpoonEntityModel.getLayer(HarpoonEntityRenderer.getTexture(((HarpoonItem) stack.getItem()).getType())),
                    false,
                    stack.hasGlint()
            );
            this.harpoonEntityModel.render(matrices, harpoon, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        }
        matrices.pop();
        return true;
    }
}
