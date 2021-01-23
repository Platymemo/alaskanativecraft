package com.github.platymemo.alaskanativecraft.client.renderer.entity.feature;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.feature.KuspukSkirtModel;
import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class KuspukSkirtFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final KuspukSkirtModel<T> model = new KuspukSkirtModel<>();
    private final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/feature/skirt_layer.png");

    public KuspukSkirtFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack armorItemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (armorItemStack.getItem() == AlaskaNativeItems.KUSPUK_BODY) {
            int color = ((DyeableArmorItem) armorItemStack.getItem()).getColor(armorItemStack);
            float f = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float h = (float) (color & 255) / 255.0F;
            matrixStack.push();
            if (livingEntity.isInSneakingPose()) {
                matrixStack.translate(0.0D, -2.55D, 0.3D);
            } else {
                matrixStack.translate(0.0D, -2.7D, 0.0D);
            }
            matrixStack.scale(2.5F, 2.5F, 2.5F);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(TEXTURE), false, armorItemStack.hasGlint());
            this.model.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            this.model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, f, g, h, 1.0F);
            matrixStack.pop();
        }
    }
}