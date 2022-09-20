package com.github.platymemo.alaskanativecraft.client.renderer.entity.feature;

import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.PtarmiganEntityModel;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.PtarmiganEntityRenderer;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ShoulderPtarmiganFeatureRenderer<T extends PlayerEntity> extends FeatureRenderer<T, PlayerEntityModel<T>> {
    private final PtarmiganEntityModel model;

    public ShoulderPtarmiganFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context, @NotNull EntityModelLoader loader) {
        super(context);
        this.model = new PtarmiganEntityModel(loader.getModelPart(AlaskaModels.PTARMIGAN));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T playerEntity, float f, float g, float h, float j, float k, float l) {
        this.renderShoulderPtarmigan(matrixStack, vertexConsumerProvider, i, playerEntity, f, g, k, l, true);
        this.renderShoulderPtarmigan(matrixStack, vertexConsumerProvider, i, playerEntity, f, g, k, l, false);
    }

    private void renderShoulderPtarmigan(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T player, float limbAngle, float limbDistance, float headYaw, float headPitch, boolean leftShoulder) {
        NbtCompound compoundTag = leftShoulder ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
        EntityType.get(compoundTag.getString("id")).filter((entityType) -> entityType == AlaskaEntities.PTARMIGAN).ifPresent((entityType) -> {
            matrices.push();
            matrices.translate(leftShoulder ? 0.4D : -0.4D, player.isInSneakingPose() ? -1.3D : -1.5D, 0.0D);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(PtarmiganEntityRenderer.TEXTURES[compoundTag.getInt("Type")]));
            this.model.poseOnShoulder(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, limbAngle, limbDistance, headYaw, headPitch, player.age);
            matrices.pop();
        });
    }
}
