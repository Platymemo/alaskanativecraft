package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.PtarmiganEntity;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class PtarmiganEntityModel extends CompositeEntityModel<PtarmiganEntity> {
    private final ModelPart torso;
    private final ModelPart tail;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public PtarmiganEntityModel() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.torso = new ModelPart(this, 0, 7);
        this.torso.addCuboid(-2.5F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F);
        this.torso.setPivot(0.0F, 16.5F, -3.0F);
        this.tail = new ModelPart(this, 24, 0);
        this.tail.addCuboid(-1.5F, -1.0F, 0.0F, 3.0F, 4.0F, 1.0F);
        this.tail.setPivot(0.0F, 21.07F, 1.16F);
        this.leftWing = new ModelPart(this, 2, 22);
        this.leftWing.addCuboid(-2.0F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F);
        this.leftWing.setPivot(2.5F, 16.94F, -2.76F);
        this.rightWing = new ModelPart(this, 2, 22);
        this.rightWing.addCuboid(1.0F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F);
        this.rightWing.setPivot(-2.5F, 16.94F, -2.76F);
        this.head = new ModelPart(this, 0, 0);
        this.head.addCuboid(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F);
        this.head.setPivot(0.0F, 15.69F, -2.76F);
        this.beak = new ModelPart(this, 9, 0);
        this.beak.addCuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        this.beak.setPivot(0.0F, -0.5F, -1.5F);
        this.head.addChild(this.beak);
        this.leftLeg = new ModelPart(this, 14, 24);
        this.leftLeg.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        this.leftLeg.setPivot(1.0F, 22.0F, -1.05F);
        this.rightLeg = new ModelPart(this, 14, 24);
        this.rightLeg.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        this.rightLeg.setPivot(-1.0F, 22.0F, -1.05F);
    }

    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(this.torso, this.leftWing, this.rightWing, this.tail, this.head, this.leftLeg, this.rightLeg);
    }

    public void setAngles(PtarmiganEntity ptarmiganEntity, float f, float g, float h, float i, float j) {
        this.setAngles(getPose(ptarmiganEntity), ptarmiganEntity.age, f, g, h, i, j);
    }

    public void animateModel(PtarmiganEntity ptarmiganEntity, float f, float g, float h) {
        this.animateModel(getPose(ptarmiganEntity));
    }

    public void poseOnShoulder(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float limbAngle, float limbDistance, float headYaw, float headPitch, int danceAngle) {
        this.animateModel(PtarmiganEntityModel.Pose.ON_SHOULDER);
        this.setAngles(PtarmiganEntityModel.Pose.ON_SHOULDER, danceAngle, limbAngle, limbDistance, 0.0F, headYaw, headPitch);
        this.getParts().forEach((modelPart) -> modelPart.render(matrices, vertexConsumer, light, overlay));
    }

    private void setAngles(PtarmiganEntityModel.Pose pose, int danceAngle, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.head.roll = 0.0F;
        this.head.pivotX = 0.0F;
        this.torso.pivotX = 0.0F;
        this.tail.pivotX = 0.0F;
        this.rightWing.pivotX = -1.5F;
        this.leftWing.pivotX = 1.5F;
        switch (pose) {
            case SITTING:
                break;
            case PARTY:
                float cosDanceAngle = MathHelper.cos((float) danceAngle);
                float sinDanceAngle = MathHelper.sin((float) danceAngle);
                this.head.pivotX = cosDanceAngle;
                this.head.pivotY = 15.69F + sinDanceAngle;
                this.head.pitch = 0.0F;
                this.head.yaw = 0.0F;
                this.head.roll = MathHelper.sin((float) danceAngle) * 0.4F;
                this.torso.pivotX = cosDanceAngle;
                this.torso.pivotY = 16.5F + sinDanceAngle;
                this.leftWing.roll = -0.0873F - age;
                this.leftWing.pivotX = 1.5F + cosDanceAngle;
                this.leftWing.pivotY = 16.94F + sinDanceAngle;
                this.rightWing.roll = 0.0873F + age;
                this.rightWing.pivotX = -1.5F + cosDanceAngle;
                this.rightWing.pivotY = 16.94F + sinDanceAngle;
                this.tail.pivotX = cosDanceAngle;
                this.tail.pivotY = 21.07F + sinDanceAngle;
                break;
            case STANDING:
                ModelPart leg = this.leftLeg;
                leg.pitch += MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
                leg = this.rightLeg;
                leg.pitch += MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            case FLYING:
            case ON_SHOULDER:
            default:
                float thirdOfAge = age * 0.3F;
                this.head.pivotY = 15.69F + thirdOfAge;
                this.tail.pitch = 1.015F + MathHelper.cos(limbAngle * 0.6662F) * 0.3F * limbDistance;
                this.tail.pivotY = 21.07F + thirdOfAge;
                this.torso.pivotY = 16.5F + thirdOfAge;
                this.leftWing.roll = -0.0873F - age;
                this.leftWing.pivotY = 16.94F + thirdOfAge;
                this.rightWing.roll = 0.0873F + age;
                this.rightWing.pivotY = 16.94F + thirdOfAge;
                this.leftLeg.pivotY = 22.0F + thirdOfAge;
                this.rightLeg.pivotY = 22.0F + thirdOfAge;
        }

    }

    private void animateModel(PtarmiganEntityModel.Pose pose) {
        this.torso.pitch = 0.4937F;
        this.leftWing.pitch = -0.6981F;
        this.leftWing.yaw = -3.1415927F;
        this.rightWing.pitch = -0.6981F;
        this.rightWing.yaw = -3.1415927F;
        this.leftLeg.pitch = -0.0299F;
        this.rightLeg.pitch = -0.0299F;
        this.leftLeg.pivotY = 22.0F;
        this.rightLeg.pivotY = 22.0F;
        this.leftLeg.roll = 0.0F;
        this.rightLeg.roll = 0.0F;
        switch (pose) {
            case SITTING:
                float f = 1.9F;
                this.head.pivotY = 17.59F;
                this.tail.pitch = 1.5388988F;
                this.tail.pivotY = 22.97F;
                this.torso.pivotY = 18.4F;
                this.leftWing.roll = -0.0873F;
                this.leftWing.pivotY = 18.84F;
                this.rightWing.roll = 0.0873F;
                this.rightWing.pivotY = 18.84F;
                ++this.leftLeg.pivotY;
                ++this.rightLeg.pivotY;
                ++this.leftLeg.pitch;
                ++this.rightLeg.pitch;
                break;
            case PARTY:
                this.leftLeg.roll = -0.34906584F;
                this.rightLeg.roll = 0.34906584F;
            case STANDING:
            case ON_SHOULDER:
            default:
                break;
            case FLYING:
                this.leftLeg.pitch += 0.6981317F;
                this.rightLeg.pitch += 0.6981317F;
        }

    }

    private static PtarmiganEntityModel.Pose getPose(PtarmiganEntity ptarmigan) {
        if (ptarmigan.getSongPlaying()) {
            return PtarmiganEntityModel.Pose.PARTY;
        } else if (ptarmigan.isInSittingPose()) {
            return PtarmiganEntityModel.Pose.SITTING;
        } else {
            return ptarmigan.isInAir() ? PtarmiganEntityModel.Pose.FLYING : PtarmiganEntityModel.Pose.STANDING;
        }
    }

    @Environment(EnvType.CLIENT)
    public enum Pose {
        FLYING,
        STANDING,
        SITTING,
        PARTY,
        ON_SHOULDER
    }
}
