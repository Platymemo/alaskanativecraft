package com.github.platymemo.alaskanativecraft.client.model.entity.feature;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class KuspukSkirtModel<E extends LivingEntity> extends AnimalModel<E> {
    private final ModelPart skirt;

    public KuspukSkirtModel(@NotNull ModelPart modelPart) {
        this.skirt = modelPart.getChild("skirt");
    }

    public static @NotNull TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData skirt = modelPartData.addChild("skirt", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0f, 18.0f, 2.0f));

        skirt.addChild("strip_1", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(3.7f, 0.0f, -0.3f,
                        -1.309f, 0.7854f, 0.0f));

        skirt.addChild("strip_2", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, -5.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(-3.7f, 0.0f, -3.7f,
                        1.309f, 0.7854f, 0.0f));

        skirt.addChild("strip_3", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(-3.7f, 0.0f, -0.3f,
                        -1.309f, -0.7854f, 0.0f));

        skirt.addChild("strip_4", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, -5.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(3.7f, 0.0f, -3.7f,
                        1.309f, -0.7854f, 0.0f));

        skirt.addChild("right", ModelPartBuilder.create()
                        .uv(0, 5)
                        .cuboid(-5.0f, 0.0f, -2.0f, 5.0f, 0.0f, 4.0f),
                ModelTransform.of(-4.0f, 0.0f, -2.0f,
                        0.0f, 0.0f, -1.309f));

        skirt.addChild("left", ModelPartBuilder.create()
                        .uv(0, 5)
                        .cuboid(0.0f, 0.0f, -2.0f, 5.0f, 0.0f, 4.0f),
                ModelTransform.of(4.0f, 0.0f, -2.0f,
                        0.0f, 0.0f, 1.309f));

        skirt.addChild("front", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0f, 0.0f, -5.0f, 8.0f, 0.0f, 5.0f),
                ModelTransform.of(0.0f, 0.0f, -4.0f,
                        1.309f, 0.0f, 0.0f));

        skirt.addChild("back", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0f, 0.0f, 0.0f, 8.0f, 0.0f, 5.0f),
                ModelTransform.of(0.0f, 0.0f, 0.0f,
                        -1.309f, 0.0f, 0.0f));

        return TexturedModelData.of(modelData, 32, 16);
    }

    @Override
    public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.skirt);
    }
}
