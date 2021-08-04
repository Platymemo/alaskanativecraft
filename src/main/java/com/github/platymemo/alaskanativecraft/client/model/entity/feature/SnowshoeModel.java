package com.github.platymemo.alaskanativecraft.client.model.entity.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class SnowshoeModel<E extends LivingEntity> extends AnimalModel<E> {
    private final ModelPart snowshoe;

    public SnowshoeModel(@NotNull ModelPart root) {
        snowshoe = root.getChild("shoe");
    }

    public static @NotNull TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("shoe", ModelPartBuilder.create()
                        .uv(23, 5).cuboid(-1.0F, -24.0F, 8.0F, 2.0F, 1.0F, 5.0F)
                        .uv(23, 0).cuboid(-2.0F, -24.0F, 4.0F, 4.0F, 1.0F, 4.0F)
                        .uv(0, 0).cuboid(-3.0F, -24.0F, -7.0F, 6.0F, 1.0F, 11.0F)
                        .uv(0, 0).cuboid(-2.0F, -24.0F, -8.0F, 4.0F, 1.0F, 1.0F)
                        .uv(0, 9).cuboid(-1.0F, -24.0F, -10.0F, 2.0F, 1.0F, 2.0F),
                ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 16);
    }

    @Override
    public void setAngles(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.snowshoe);
    }
}