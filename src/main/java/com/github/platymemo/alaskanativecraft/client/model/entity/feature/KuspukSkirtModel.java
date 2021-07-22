package com.github.platymemo.alaskanativecraft.client.model.entity.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

public class KuspukSkirtModel<T extends LivingEntity> extends AnimalModel<T> {
    private final ModelPart skirt;

    public KuspukSkirtModel(ModelPart modelPart) {
        skirt = modelPart.getChild("skirt");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData skirt = modelPartData.addChild("skirt", ModelPartBuilder.create()
                        .uv(0, 0),
                ModelTransform.pivot(0.0f, 18.0f, 2.0f));

        skirt.addChild("cube_r1", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(3.7f, 0.0f, -0.3f,
                        -1.309f, 0.7854f, 0.0f));

        skirt.addChild("cube_r2", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, -5.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(-3.7f, 0.0f, -3.7f,
                        1.309f, 0.7854f, 0.0f));

        skirt.addChild("cube_r3", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(-3.7f, 0.0f, -0.3f,
                        -1.309f, -0.7854f, 0.0f));

        skirt.addChild("cube_r4", ModelPartBuilder.create()
                        .uv(0, 5)
                        .cuboid(-5.0f, 0.0f, -2.0f, 5.0f, 0.0f, 4.0f),
                ModelTransform.of(-4.0f, 0.0f, -2.0f,
                        0.0f, 0.0f, -1.309f));

        skirt.addChild("cube_r5", ModelPartBuilder.create()
                        .uv(0, 5)
                        .cuboid(0.0f, 0.0f, -2.0f, 5.0f, 0.0f, 4.0f),
                ModelTransform.of(4.0f, 0.0f, -2.0f,
                        0.0f, 0.0f, 1.309f));

        skirt.addChild("cube_r6", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0f, 0.0f, -5.0f, 8.0f, 0.0f, 5.0f),
                ModelTransform.of(0.0f, 0.0f, -4.0f,
                        1.309f, 0.0f, 0.0f));

        skirt.addChild("cube_r7", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0f, 0.0f, 0.0f, 8.0f, 0.0f, 5.0f),
                ModelTransform.of(0.0f, 0.0f, 0.0f,
                        -1.309f, 0.0f, 0.0f));

        skirt.addChild("cube_r8", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, -5.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(3.7f, 0.0f, -3.7f,
                        1.309f, -0.7854f, 0.0f));

        return TexturedModelData.of(modelData, 32, 64);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
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