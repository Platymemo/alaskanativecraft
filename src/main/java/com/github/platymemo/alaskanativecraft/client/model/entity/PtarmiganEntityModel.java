package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.PtarmiganEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class PtarmiganEntityModel extends SinglePartEntityModel<PtarmiganEntity> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public PtarmiganEntityModel(@NotNull ModelPart root) {
        this.root = root;
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.tail = root.getChild(EntityModelPartNames.TAIL);
        this.leftWing = root.getChild(EntityModelPartNames.LEFT_WING);
        this.rightWing = root.getChild(EntityModelPartNames.RIGHT_WING);
        this.head = root.getChild(EntityModelPartNames.HEAD);
        this.leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
        this.rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
    }

    public static @NotNull TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create()
                        .uv(0, 7)
                        .cuboid(-2.5F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F),
                ModelTransform.pivot(0.0F, 16.5F, -3.0F)
        );
        root.addChild(EntityModelPartNames.TAIL,
                ModelPartBuilder.create()
                        .uv(24, 0)
                        .cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 4.0F, 1.0F),
                ModelTransform.pivot(0.0F, 21.07F, 1.16F)
        );
        root.addChild(EntityModelPartNames.LEFT_WING,
                ModelPartBuilder.create()
                        .uv(2, 22)
                        .cuboid(-2.0F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F),
                ModelTransform.pivot(2.5F, 16.94F, -2.76F)
        );
        root.addChild(EntityModelPartNames.RIGHT_WING,
                ModelPartBuilder.create()
                        .uv(2, 22)
                        .cuboid(1.0F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F),
                ModelTransform.pivot(-2.5F, 16.94F, -2.76F)
        );
        ModelPartData head = root.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F),
                ModelTransform.pivot(0.0F, 15.69F, -2.76F)
        );
        head.addChild("beak",
                ModelPartBuilder.create()
                        .uv(9, 0)
                        .cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                ModelTransform.pivot(0.0F, -0.5F, -1.5F)
        );

        ModelPartBuilder legBuilder = ModelPartBuilder.create().uv(14, 24).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        root.addChild(EntityModelPartNames.LEFT_LEG, legBuilder, ModelTransform.pivot(1.0F, 22.0F, -1.05F));
        root.addChild(EntityModelPartNames.RIGHT_LEG, legBuilder, ModelTransform.pivot(-1.0F, 22.0F, -1.05F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    private static PtarmiganEntityModel.Pose getPose(@NotNull PtarmiganEntity ptarmigan) {
        if (ptarmigan.isSongPlaying()) {
            return PtarmiganEntityModel.Pose.PARTY;
        } else if (ptarmigan.isInSittingPose()) {
            return PtarmiganEntityModel.Pose.SITTING;
        } else {
            return ptarmigan.isInAir() ? PtarmiganEntityModel.Pose.FLYING : PtarmiganEntityModel.Pose.STANDING;
        }
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(PtarmiganEntity ptarmiganEntity, float f, float g, float h, float i, float j) {
        this.setAngles(getPose(ptarmiganEntity), ptarmiganEntity.age, f, g, h, i, j);
    }

    @Override
    public void animateModel(PtarmiganEntity ptarmiganEntity, float f, float g, float h) {
        this.animateModel(getPose(ptarmiganEntity));
    }

    public void poseOnShoulder(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float limbAngle, float limbDistance, float headYaw, float headPitch, int danceAngle) {
        this.animateModel(PtarmiganEntityModel.Pose.ON_SHOULDER);
        this.setAngles(PtarmiganEntityModel.Pose.ON_SHOULDER, danceAngle, limbAngle, limbDistance, 0.0F, headYaw, headPitch);
        this.root.render(matrices, vertexConsumer, light, overlay);
    }

    private void setAngles(@NotNull PtarmiganEntityModel.Pose pose, int danceAngle, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.head.roll = 0.0F;
        this.head.pivotX = 0.0F;
        this.body.pivotX = 0.0F;
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
                this.body.pivotX = cosDanceAngle;
                this.body.pivotY = 16.5F + sinDanceAngle;
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
                this.leftLeg.pitch += MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
                this.rightLeg.pitch += MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
            case FLYING:
            case ON_SHOULDER:
            default:
                float thirdOfAge = age * 0.3F;
                this.head.pivotY = 15.69F + thirdOfAge;
                this.tail.pitch = 1.015F + MathHelper.cos(limbAngle * 0.6662F) * 0.3F * limbDistance;
                this.tail.pivotY = 21.07F + thirdOfAge;
                this.body.pivotY = 16.5F + thirdOfAge;
                this.leftWing.roll = -0.0873F - age;
                this.leftWing.pivotY = 16.94F + thirdOfAge;
                this.rightWing.roll = 0.0873F + age;
                this.rightWing.pivotY = 16.94F + thirdOfAge;
                this.leftLeg.pivotY = 22.0F + thirdOfAge;
                this.rightLeg.pivotY = 22.0F + thirdOfAge;
        }

    }

    private void animateModel(@NotNull PtarmiganEntityModel.Pose pose) {
        this.body.pitch = 0.4937F;
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
                this.head.pivotY = 17.59F;
                this.tail.pitch = 1.5388988F;
                this.tail.pivotY = 22.97F;
                this.body.pivotY = 18.4F;
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

    @Environment(EnvType.CLIENT)
    public enum Pose {
        FLYING,
        STANDING,
        SITTING,
        PARTY,
        ON_SHOULDER
    }
}
