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

public class KuspukSkirtModel<T extends LivingEntity> extends AnimalModel<T> {
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData skirt = modelPartData.addChild("skirt", ModelPartBuilder.create()
                        .uv(0, 0),
                ModelTransform.pivot(0.0f, 18.0f, 2.0f));

        ModelPartData cube_r1 = skirt.addChild("cube_r1", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(3.7f, 0.0f, -0.3f,
                        -1.309f, 0.7854f, 0.0f));

        ModelPartData cube_r2 = skirt.addChild("cube_r2", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, -5.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(-3.7f, 0.0f, -3.7f,
                        1.309f, 0.7854f, 0.0f));

        ModelPartData cube_r3 = skirt.addChild("cube_r3", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(-3.7f, 0.0f, -0.3f,
                        -1.309f, -0.7854f, 0.0f));

        ModelPartData cube_r4 = skirt.addChild("cube_r4", ModelPartBuilder.create()
                        .uv(0, 5)
                        .cuboid(-5.0f, 0.0f, -2.0f, 5.0f, 0.0f, 4.0f),
                ModelTransform.of(-4.0f, 0.0f, -2.0f,
                        0.0f, 0.0f, -1.309f));

        ModelPartData cube_r5 = skirt.addChild("cube_r5", ModelPartBuilder.create()
                        .uv(0, 5)
                        .cuboid(0.0f, 0.0f, -2.0f, 5.0f, 0.0f, 4.0f),
                ModelTransform.of(4.0f, 0.0f, -2.0f,
                        0.0f, 0.0f, 1.309f));

        ModelPartData cube_r6 = skirt.addChild("cube_r6", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0f, 0.0f, -5.0f, 8.0f, 0.0f, 5.0f),
                ModelTransform.of(0.0f, 0.0f, -4.0f,
                        1.309f, 0.0f, 0.0f));

        ModelPartData cube_r7 = skirt.addChild("cube_r7", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0f, 0.0f, 0.0f, 8.0f, 0.0f, 5.0f),
                ModelTransform.of(0.0f, 0.0f, 0.0f,
                        -1.309f, 0.0f, 0.0f));

        ModelPartData cube_r8 = skirt.addChild("cube_r8", ModelPartBuilder.create()
                        .uv(0, 9)
                        .cuboid(-0.5f, 0.0f, -5.0f, 1.0f, 0.0f, 5.0f),
                ModelTransform.of(3.7f, 0.0f, -3.7f,
                        1.309f, -0.7854f, 0.0f));

        return TexturedModelData.of(modelData, 32, 64);
    }

    private final ModelPart skirt;

    public KuspukSkirtModel(ModelPart modelPart) {
        /*textureWidth = 32;
        textureHeight = 16;

        skirt = new ModelPart(this);
        skirt.setPivot(0.0F, 18.0F, 2.0F);

        ModelPart cube_r1 = new ModelPart(this);
        cube_r1.setPivot(3.7F, 0.0F, -0.3F);
        skirt.addChild(cube_r1);
        setRotationAngle(cube_r1, -1.309F, 0.7854F, 0.0F);
        cube_r1.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);

        ModelPart cube_r2 = new ModelPart(this);
        cube_r2.setPivot(-3.7F, 0.0F, -3.7F);
        skirt.addChild(cube_r2);
        setRotationAngle(cube_r2, 1.309F, 0.7854F, 0.0F);
        cube_r2.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, -5.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);

        ModelPart cube_r3 = new ModelPart(this);
        cube_r3.setPivot(-3.7F, 0.0F, -0.3F);
        skirt.addChild(cube_r3);
        setRotationAngle(cube_r3, -1.309F, -0.7854F, 0.0F);
        cube_r3.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);

        ModelPart cube_r4 = new ModelPart(this);
        cube_r4.setPivot(-4.0F, 0.0F, -2.0F);
        skirt.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -1.309F);
        cube_r4.setTextureOffset(0, 5).addCuboid(-5.0F, 0.0F, -2.0F, 5.0F, 0.0F, 4.0F, 0.0F, false);

        ModelPart cube_r5 = new ModelPart(this);
        cube_r5.setPivot(4.0F, 0.0F, -2.0F);
        skirt.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, 1.309F);
        cube_r5.setTextureOffset(0, 5).addCuboid(0.0F, 0.0F, -2.0F, 5.0F, 0.0F, 4.0F, 0.0F, false);

        ModelPart cube_r6 = new ModelPart(this);
        cube_r6.setPivot(0.0F, 0.0F, -4.0F);
        skirt.addChild(cube_r6);
        setRotationAngle(cube_r6, 1.309F, 0.0F, 0.0F);
        cube_r6.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, -5.0F, 8.0F, 0.0F, 5.0F, 0.0F, false);

        ModelPart cube_r7 = new ModelPart(this);
        cube_r7.setPivot(0.0F, 0.0F, 0.0F);
        skirt.addChild(cube_r7);
        setRotationAngle(cube_r7, -1.309F, 0.0F, 0.0F);
        cube_r7.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 5.0F, 0.0F, false);

        ModelPart cube_r8 = new ModelPart(this);
        cube_r8.setPivot(3.7F, 0.0F, -3.7F);
        skirt.addChild(cube_r8);
        setRotationAngle(cube_r8, 1.309F, -0.7854F, 0.0F);
        cube_r8.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, -5.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);*/

        skirt = modelPart.getChild("skirt");
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

    public void setRotationAngle(ModelPart ModelPart, float pitch, float yaw, float roll) {
        ModelPart.pitch = pitch;
        ModelPart.yaw = yaw;
        ModelPart.roll = roll;
    }
}