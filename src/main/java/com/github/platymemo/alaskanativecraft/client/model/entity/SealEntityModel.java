package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.SealEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SealEntityModel<T extends SealEntity> extends QuadrupedEntityModel<T> {

    protected final ModelPart stomach;
    protected final ModelPart tail;

    public SealEntityModel(float scale) {
        super(12, scale, true, 120.0F, 0.0F, 9.0F, 6.0F, 120);
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.head = new ModelPart(this);
        this.head.setPivot(-0.5F, 19.0F, -8.0F);
        this.head.setTextureOffset(29, 31).addCuboid(-4.5F, -5.0F, -3.0F, 9.0F, 9.0F, 3.0F, scale);
        this.head.setTextureOffset(32, 18).addCuboid(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 2.0F, scale);
        this.head.setTextureOffset(38, 10).addCuboid(-2.5F, -1.0F, -7.0F, 5.0F, 3.0F, 2.0F, scale);

        this.torso = new ModelPart(this);
        this.torso.setPivot(-0.5F, 19.0F, -4.0F);
        this.torso.setTextureOffset(0, 0).addCuboid(-5.5F, -5.0F, -4.0F, 11.0F, 10.0F, 8.0F, scale);

        this.stomach = new ModelPart(this);
        this.stomach.setPivot(-0.5F, 19.5F, 3.5F);
        this.stomach.setTextureOffset(0, 18).addCuboid(-4.5F, -4.5F, -3.5F, 9.0F, 9.0F, 7.0F, scale);

        this.tail = new ModelPart(this);
        this.tail.setPivot(-0.5F, 21.0F, 9.0F);
        this.tail.setTextureOffset(0, 34).addCuboid(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 4.0F, scale);

        this.backLeftLeg = new ModelPart(this);
        this.backLeftLeg.setPivot(2.0F, 23.0F, 11.1F);
        this.backLeftLeg.setTextureOffset(13, 38).addCuboid(-1.0F, 0.0F, -1.1F, 3.0F, 1.0F, 5.0F, scale);

        this.backRightLeg = new ModelPart(this);
        this.backRightLeg.setPivot(-3.0F, 23.0F, 11.0F);
        this.backRightLeg.setTextureOffset(30, 0).addCuboid(-2.0F, 0.0F, -1.0F, 3.0F, 1.0F, 5.0F, scale);

        this.frontLeftLeg = new ModelPart(this);
        this.frontLeftLeg.setPivot(5.0F, 24.0F, -6.0F);
        this.frontLeftLeg.setTextureOffset(32, 27).addCuboid(0.0F, -1.0F, -1.0F, 5.0F, 1.0F, 3.0F, scale);

        this.frontRightLeg = new ModelPart(this);
        this.frontRightLeg.setPivot(-6.0F, 24.0F, -6.0F);
        this.frontRightLeg.setTextureOffset(38, 6).addCuboid(-5.0F, -1.0F, -1.0F, 5.0F, 1.0F, 3.0F, scale);
    }

    protected Iterable<ModelPart> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.stomach), ImmutableList.of(this.tail));
    }

    /*
    * TODO
    * Make the animations more seal-like (currently copies Turtles)
    */
    @Override
    public void setAngles(T sealEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.torso.pitch = 0.0F;
        this.backRightLeg.pitch = MathHelper.cos(limbAngle * 0.6662F * 0.6F) * 0.5F * limbDistance;
        this.backLeftLeg.pitch = MathHelper.cos(limbAngle * 0.6662F * 0.6F + 3.1415927F) * 0.5F * limbDistance;
        this.frontRightLeg.roll = MathHelper.cos(limbAngle * 0.6662F * 0.6F + 3.1415927F) * 0.5F * limbDistance;
        this.frontLeftLeg.roll = MathHelper.cos(limbAngle * 0.6662F * 0.6F) * 0.5F * limbDistance;
        this.frontRightLeg.pitch = 0.0F;
        this.frontLeftLeg.pitch = 0.0F;
        this.frontRightLeg.yaw = 0.0F;
        this.frontLeftLeg.yaw = 0.0F;
        this.backRightLeg.yaw = 0.0F;
        this.backLeftLeg.yaw = 0.0F;
        if (!sealEntity.isTouchingWater() && sealEntity.isOnGround()) {
            this.frontRightLeg.yaw = MathHelper.cos(limbAngle * 5.0F + 3.1415927F) * 8.0F * limbDistance;
            this.frontRightLeg.roll = 0.0F;
            this.frontLeftLeg.yaw = MathHelper.cos(limbAngle * 5.0F) * 8.0F * limbDistance;
            this.frontLeftLeg.roll = 0.0F;
            this.backRightLeg.yaw = MathHelper.cos(limbAngle * 5.0F + 3.1415927F) * 3.0F * limbDistance;
            this.backRightLeg.pitch = 0.0F;
            this.backLeftLeg.yaw = MathHelper.cos(limbAngle * 5.0F) * 3.0F * limbDistance;
            this.backLeftLeg.pitch = 0.0F;
        }
    }

    private void setRotations(ModelPart modelPart, float pitch, float yaw, float roll) {
        modelPart.pitch = pitch;
        modelPart.yaw = yaw;
        modelPart.roll = roll;
    }
}
