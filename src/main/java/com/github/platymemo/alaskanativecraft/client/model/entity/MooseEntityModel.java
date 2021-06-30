package com.github.platymemo.alaskanativecraft.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class MooseEntityModel<T extends Entity> extends QuadrupedEntityModel<T> {
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
                        .uv(42, 11)
                        .cuboid(-1.0f, -3.0f, 14.0f, 2.0f, 3.0f, 1.0f),
                ModelTransform.pivot(0.5f, 7.0f, 0.0f));

        body.addChild("hump", ModelPartBuilder.create()
                        .uv(30, 0)
                        .cuboid(-3.0f, -6.0f, -10.0f, 6.0f, 4.0f, 7.0f),
                ModelTransform.of(0.0f, 2.0f, -1.8f,
                        -0.4363f, 0.0f, 0.0f));

        modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create()
                        .uv(52, 30)
                        .cuboid(0.0f, 6.0f, -1.0f, 2.0f, 12.0f, 3.0f),
                ModelTransform.pivot(4.0f, 6.0f, 9.0f));

        modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create()
                        .uv(52, 30)
                        .cuboid(-2.0f, 6.0f, -1.0f, 2.0f, 12.0f, 3.0f),
                ModelTransform.pivot(-3.0f, 6.0f, 9.0f));

        modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create()
                        .uv(52, 30)
                        .cuboid(0.0f, 6.0f, -2.0f, 2.0f, 12.0f, 3.0f),
                ModelTransform.pivot(4.0f, 6.0f, -6.0f));

        modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create()
                        .uv(52, 30)
                        .cuboid(-2.0f, 6.0f, -2.0f, 2.0f, 12.0f, 3.0f),
                ModelTransform.pivot(-3.0f, 6.0f, -6.0f));

        ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                        .uv(0, 0),
                ModelTransform.pivot(0.5f, 6.5f, -8.0f));

        head.addChild("snout", ModelPartBuilder.create()
                        .uv(46, 12)
                        .cuboid(-1.0f, -1.6736f, -2.9848f, 3.0f, 3.0f, 3.0f),
                ModelTransform.of(-0.5f, -1.8f, -11.8f,
                        0.6109f, 0.0f, 0.0f));

        head.addChild(EntityModelPartNames.NOSE, ModelPartBuilder.create()
                        .uv(23, 25)
                        .cuboid(-1.0f, -1.6736f, -2.9848f, 3.0f, 3.0f, 4.0f),
                ModelTransform.of(-0.5f, -1.6f, -10.0f,
                        0.1745f, 0.0f, 0.0f));

         head.addChild("headBase", ModelPartBuilder.create()
                        .uv(41, 20)
                        .cuboid(-2.0f, -2.0f, -2.0f, 5.0f, 5.0f, 5.0f),
                ModelTransform.of(-0.5f, -2.0f, -8.0f,
                        0.5236f, 0.0f, 0.0f));

        ModelPartData leftAntler = head.addChild("leftAntler", ModelPartBuilder.create()
                        .uv(0, 0),
                ModelTransform.pivot(2.5f, -4.0f, -6.5f));

        leftAntler.addChild("leftAntlerProng3", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-1.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f),
                ModelTransform.of(4.3f, -0.8f, -0.6f,
                        -0.6116f, -0.0114f, 0.1304f));

        leftAntler.addChild("leftAntlerProng2", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-1.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f),
                ModelTransform.of(3.3f, -0.6f, -0.4f,
                        -0.6106f, 0.0907f, -0.0358f));

        leftAntler.addChild("leftAntlerProng1", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-1.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f),
                ModelTransform.of(2.3f, -0.6f, -0.4f,
                        -0.6979f, -0.0831f, -0.0511f));

        leftAntler.addChild("leftAntlerSideProng", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-1.0f, -4.0f, 0.0f, 1.0f, 4.0f, 1.0f),
                ModelTransform.of(4.5f, -0.1f, -0.7f,
                        0.0756f, -0.0436f, 0.5219f));

        leftAntler.addChild("leftAntlerFrontProng", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-1.0f, -3.0f, 0.0f, 1.0f, 3.0f, 1.0f),
                ModelTransform.of(3.7f, -0.1f, -0.7f,
                        0.8782f, 0.3477f, 0.0317f));

        leftAntler.addChild("leftAntlerBase", ModelPartBuilder.create()
                        .uv(30, 11)
                        .cuboid(-1.0f, -1.0f, 0.0f, 5.0f, 1.0f, 1.0f),
                ModelTransform.of(0.5f, 0.4f, -0.7f,
                        -0.1309f, 0.0f, -0.1745f));

        ModelPartData rightAntler = head.addChild("rightAntler", ModelPartBuilder.create()
                        .uv(0, 0),
                ModelTransform.pivot(-2.5f, -4.0f, -6.5f));

        rightAntler.addChild("rightAntlerProng3", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(0.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f),
                ModelTransform.of(-4.3f, -0.8f, -0.6f,
                        -0.6116f, 0.0114f, -0.1304f));

        rightAntler.addChild("rightAntlerProng2", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(0.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f),
                ModelTransform.of(-3.3f, -0.6f, -0.4f,
                        -0.6106f, -0.0907f, 0.0358f));

        rightAntler.addChild("rightAntlerProng1", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(0.0f, -5.0f, 0.0f, 1.0f, 5.0f, 1.0f),
                ModelTransform.of(-2.3f, -0.6f, -0.4f,
                        -0.6979f, 0.0831f, 0.0511f));

        rightAntler.addChild("rightAntlerSideProng", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(0.0f, -4.0f, 0.0f, 1.0f, 4.0f, 1.0f),
                ModelTransform.of(-4.5f, -0.1f, -0.7f,
                        0.0756f, 0.0436f, -0.5219f));

        rightAntler.addChild("rightAntlerFrontProng", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(0.0f, -3.0f, 0.0f, 1.0f, 3.0f, 1.0f),
                ModelTransform.of(-3.7f, -0.1f, -0.7f,
                        0.8782f, -0.3477f, -0.0317f));

        rightAntler.addChild("rightAntlerBase", ModelPartBuilder.create()
                        .uv(30, 11)
                        .cuboid(-4.0f, -1.0f, 0.0f, 5.0f, 1.0f, 1.0f),
                ModelTransform.of(-0.5f, 0.4f, -0.7f,
                        -0.1309f, 0.0f, 0.1745f));

        ModelPartData neck = head.addChild(EntityModelPartNames.NECK, ModelPartBuilder.create()
                        .uv(32, 32)
                        .cuboid(-1.5f, -2.5f, -7.0f, 3.0f, 5.0f, 7.0f),
                ModelTransform.of(0.0f, 0.0f, 0.0f,
                        -0.2182f, 0.0f, 0.0f));

        neck.addChild("dewlap", ModelPartBuilder.create()
                        .uv(0, 21)
                        .cuboid(1.0f, -3.0f, -2.0f, 0.0f, 6.0f, 4.0f),
                ModelTransform.pivot(-0.5f, 5.3817f, -4.3128f));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public MooseEntityModel(ModelPart modelPart) {
        super(modelPart, false, 0.0F, 0.0F, 1.0F, 1.0F, 0);

        /*textureWidth = 64;
        textureHeight = 64;

        body = new ModelPart(this);
        body.setPivot(0.5F, 7.0F, 0.0F);
        body.setTextureOffset(0, 25).addCuboid(-3.5F, -5.0F, -11.0F, 7.0F, 10.0F, 9.0F, 0.0F, false);
        body.setTextureOffset(0, 0).addCuboid(-3.5F, -4.0F, -2.0F, 7.0F, 9.0F, 16.0F, 0.0F, false);
        body.setTextureOffset(42, 11).addCuboid(-1.0F, -3.0F, 14.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart hump = new ModelPart(this);
        hump.setPivot(0.0F, 2.0F, -1.8F);
        body.addChild(hump);
        setRotationAngle(hump, -0.4363F, 0.0F, 0.0F);
        hump.setTextureOffset(30, 0).addCuboid(-3.0F, -6.0F, -10.0F, 6.0F, 4.0F, 7.0F, 0.0F, false);

        backLeftLeg = new ModelPart(this);
        backLeftLeg.setPivot(4.0F, 6.0F, 9.0F);
        backLeftLeg.setTextureOffset(0, 1).addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 8.0F, 5.0F, 0.0F, false);
        backLeftLeg.setTextureOffset(52, 30).addCuboid(0.0F, 6.0F, -1.0F, 2.0F, 12.0F, 3.0F, 0.0F, false);

        backRightLeg = new ModelPart(this);
        backRightLeg.setPivot(-3.0F, 6.0F, 9.0F);
        backRightLeg.setTextureOffset(0, 1).addCuboid(-2.0F, -2.0F, -2.0F, 3.0F, 8.0F, 5.0F, 0.0F, false);
        backRightLeg.setTextureOffset(52, 30).addCuboid(-2.0F, 6.0F, -1.0F, 2.0F, 12.0F, 3.0F, 0.0F, false);

        frontLeftLeg = new ModelPart(this);
        frontLeftLeg.setPivot(4.0F, 6.0F, -6.0F);
        frontLeftLeg.setTextureOffset(0, 1).addCuboid(-1.0F, -2.0F, -3.0F, 3.0F, 8.0F, 5.0F, 0.0F, false);
        frontLeftLeg.setTextureOffset(52, 30).addCuboid(0.0F, 6.0F, -2.0F, 2.0F, 12.0F, 3.0F, 0.0F, false);

        frontRightLeg = new ModelPart(this);
        frontRightLeg.setPivot(-3.0F, 6.0F, -6.0F);
        frontRightLeg.setTextureOffset(0, 1).addCuboid(-2.0F, -2.0F, -3.0F, 3.0F, 8.0F, 5.0F, 0.0F, false);
        frontRightLeg.setTextureOffset(52, 30).addCuboid(-2.0F, 6.0F, -2.0F, 2.0F, 12.0F, 3.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.5F, 6.5F, -8.0F);


        ModelPart snout = new ModelPart(this);
        snout.setPivot(-0.5F, -1.8F, -11.8F);
        head.addChild(snout);
        setRotationAngle(snout, 0.6109F, 0.0F, 0.0F);
        snout.setTextureOffset(46, 12).addCuboid(-1.0F, -1.6736F, -2.9848F, 3.0F, 3.0F, 3.0F, 0.0F, false);

        ModelPart nose = new ModelPart(this);
        nose.setPivot(-0.5F, -1.6F, -10.0F);
        head.addChild(nose);
        setRotationAngle(nose, 0.1745F, 0.0F, 0.0F);
        nose.setTextureOffset(23, 25).addCuboid(-1.0F, -1.6736F, -2.9848F, 3.0F, 3.0F, 4.0F, 0.0F, false);

        ModelPart headBase = new ModelPart(this);
        headBase.setPivot(-0.5F, -2.0F, -8.0F);
        head.addChild(headBase);
        setRotationAngle(headBase, 0.5236F, 0.0F, 0.0F);
        headBase.setTextureOffset(41, 20).addCuboid(-2.0F, -2.0F, -2.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);

        ModelPart leftAntler = new ModelPart(this);
        leftAntler.setPivot(2.5F, -4.0F, -6.5F);
        head.addChild(leftAntler);


        ModelPart leftAntlerProng3 = new ModelPart(this);
        leftAntlerProng3.setPivot(4.3F, -0.8F, -0.6F);
        leftAntler.addChild(leftAntlerProng3);
        setRotationAngle(leftAntlerProng3, -0.6116F, -0.0114F, 0.1304F);
        leftAntlerProng3.setTextureOffset(0, 0).addCuboid(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelPart leftAntlerProng2 = new ModelPart(this);
        leftAntlerProng2.setPivot(3.3F, -0.6F, -0.4F);
        leftAntler.addChild(leftAntlerProng2);
        setRotationAngle(leftAntlerProng2, -0.6106F, 0.0907F, -0.0358F);
        leftAntlerProng2.setTextureOffset(0, 0).addCuboid(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelPart leftAntlerProng1 = new ModelPart(this);
        leftAntlerProng1.setPivot(2.3F, -0.6F, -0.4F);
        leftAntler.addChild(leftAntlerProng1);
        setRotationAngle(leftAntlerProng1, -0.6979F, -0.0831F, -0.0511F);
        leftAntlerProng1.setTextureOffset(0, 0).addCuboid(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelPart leftAntlerSideProng = new ModelPart(this);
        leftAntlerSideProng.setPivot(4.5F, -0.1F, -0.7F);
        leftAntler.addChild(leftAntlerSideProng);
        setRotationAngle(leftAntlerSideProng, 0.0756F, -0.0436F, 0.5219F);
        leftAntlerSideProng.setTextureOffset(0, 0).addCuboid(-1.0F, -4.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelPart leftAntlerFrontProng = new ModelPart(this);
        leftAntlerFrontProng.setPivot(3.7F, -0.1F, -0.7F);
        leftAntler.addChild(leftAntlerFrontProng);
        setRotationAngle(leftAntlerFrontProng, 0.8782F, 0.3477F, 0.0317F);
        leftAntlerFrontProng.setTextureOffset(0, 0).addCuboid(-1.0F, -3.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart leftAntlerBase = new ModelPart(this);
        leftAntlerBase.setPivot(0.5F, 0.4F, -0.7F);
        leftAntler.addChild(leftAntlerBase);
        setRotationAngle(leftAntlerBase, -0.1309F, 0.0F, -0.1745F);
        leftAntlerBase.setTextureOffset(30, 11).addCuboid(-1.0F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);

        ModelPart rightAntler = new ModelPart(this);
        rightAntler.setPivot(-2.5F, -4.0F, -6.5F);
        head.addChild(rightAntler);


        ModelPart rightAntlerProng3 = new ModelPart(this);
        rightAntlerProng3.setPivot(-4.3F, -0.8F, -0.6F);
        rightAntler.addChild(rightAntlerProng3);
        setRotationAngle(rightAntlerProng3, -0.6116F, 0.0114F, -0.1304F);
        rightAntlerProng3.setTextureOffset(0, 0).addCuboid(0.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelPart rightAntlerProng2 = new ModelPart(this);
        rightAntlerProng2.setPivot(-3.3F, -0.6F, -0.4F);
        rightAntler.addChild(rightAntlerProng2);
        setRotationAngle(rightAntlerProng2, -0.6106F, -0.0907F, 0.0358F);
        rightAntlerProng2.setTextureOffset(0, 0).addCuboid(0.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelPart rightAntlerProng1 = new ModelPart(this);
        rightAntlerProng1.setPivot(-2.3F, -0.6F, -0.4F);
        rightAntler.addChild(rightAntlerProng1);
        setRotationAngle(rightAntlerProng1, -0.6979F, 0.0831F, 0.0511F);
        rightAntlerProng1.setTextureOffset(0, 0).addCuboid(0.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelPart rightAntlerSideProng = new ModelPart(this);
        rightAntlerSideProng.setPivot(-4.5F, -0.1F, -0.7F);
        rightAntler.addChild(rightAntlerSideProng);
        setRotationAngle(rightAntlerSideProng, 0.0756F, 0.0436F, -0.5219F);
        rightAntlerSideProng.setTextureOffset(0, 0).addCuboid(0.0F, -4.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        ModelPart rightAntlerFrontProng = new ModelPart(this);
        rightAntlerFrontProng.setPivot(-3.7F, -0.1F, -0.7F);
        rightAntler.addChild(rightAntlerFrontProng);
        setRotationAngle(rightAntlerFrontProng, 0.8782F, -0.3477F, -0.0317F);
        rightAntlerFrontProng.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        ModelPart rightAntlerBase = new ModelPart(this);
        rightAntlerBase.setPivot(-0.5F, 0.4F, -0.7F);
        rightAntler.addChild(rightAntlerBase);
        setRotationAngle(rightAntlerBase, -0.1309F, 0.0F, 0.1745F);
        rightAntlerBase.setTextureOffset(30, 11).addCuboid(-4.0F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F, 0.0F, false);

        ModelPart neck = new ModelPart(this);
        neck.setPivot(0.0F, 0.0F, 0.0F);
        head.addChild(neck);
        setRotationAngle(neck, -0.2182F, 0.0F, 0.0F);
        neck.setTextureOffset(32, 32).addCuboid(-1.5F, -2.5F, -7.0F, 3.0F, 5.0F, 7.0F, 0.0F, false);

        ModelPart dewlap = new ModelPart(this);
        dewlap.setPivot(-0.5F, 5.3817F, -4.3128F);
        neck.addChild(dewlap);
        dewlap.setTextureOffset(0, 21).addCuboid(1.0F, -3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 0.0F, false);*/
    }

    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.rightHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.leftHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}