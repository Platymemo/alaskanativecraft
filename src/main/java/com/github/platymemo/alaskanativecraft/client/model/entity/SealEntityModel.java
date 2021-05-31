package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.SealEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SealEntityModel<T extends SealEntity> extends QuadrupedEntityModel<T> {
    public static final String STOMACH = "stomach";
    public static final String TAIL = "tail";
    protected final ModelPart stomach;
    protected final ModelPart tail;

    public SealEntityModel(ModelPart root, Dilation dilation) {
        super(root, true, 0.0F, 0.0F, 1.0F, 1.0F, 0);
        stomach = root.getChild(STOMACH);
        tail = root.getChild(TAIL);
    }

    public static TexturedModelData getTexturedModelData(Dilation dilation) {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild(EntityModelPartNames.RIGHT_FRONT_LEG,
                ModelPartBuilder.create().uv(38, 6).cuboid(-5.0F, -1.0F, -1.0F, 5.0F, 1.0F, 3.0F, dilation),
                ModelTransform.of(-6.0F, 24.0F, -6.0F, 0.0F, -1.0F, 0.0F)
        );
        root.addChild(EntityModelPartNames.LEFT_FRONT_LEG,
                ModelPartBuilder.create().uv(32, 27).cuboid(0.0F, -1.0F, -1.0F, 5.0F, 1.0F, 3.0F, dilation),
                ModelTransform.of(5.0F, 24.0F, -6.0F, 0.0F, 1.0F, 0.0F)
        );
        root.addChild(EntityModelPartNames.RIGHT_HIND_LEG,
                ModelPartBuilder.create().uv(30, 0).cuboid(-2.0F, 0.0F, -1.0F, 3.0F, 1.0F, 5.0F, dilation),
                ModelTransform.pivot(-3.0F, 23.0F, 11.0F)
        );
        root.addChild(EntityModelPartNames.LEFT_HIND_LEG,
                ModelPartBuilder.create().uv(13, 38).cuboid(-1.0F, 0.0F, -1.1F, 3.0F, 1.0F, 5.0F, dilation),
                ModelTransform.pivot(2.0F, 23.0F, 11.1F)
        );

        root.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create()
                        .uv(29, 31).cuboid(EntityModelPartNames.NECK, -4.5F, -5.0F, -3.0F, 9.0F, 9.0F, 3.0F, dilation)
                        .uv(32, 18).cuboid(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 2.0F, dilation)
                        .uv(38, 10).cuboid(EntityModelPartNames.NOSE, -2.5F, -1.0F, -7.0F, 5.0F, 3.0F, 2.0F, dilation),
                ModelTransform.pivot(-0.5F, 19.0F, -8.0F)
        );
        root.addChild(EntityModelPartNames.BODY,
                ModelPartBuilder.create().uv(0, 0).cuboid(-5.5F, -5.0F, -4.0F, 11.0F, 10.0F, 8.0F, dilation),
                ModelTransform.pivot(-0.5F, 19.0F, -4.0F)
        );
        root.addChild(STOMACH,
                ModelPartBuilder.create().uv(0, 18).cuboid(-4.5F, -4.5F, -3.5F, 9.0F, 9.0F, 7.0F, dilation),
                ModelTransform.pivot(-0.5F, 19.5F, 3.5F)
        );
        root.addChild(TAIL,
                ModelPartBuilder.create().uv(0, 34).cuboid(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 4.0F, dilation),
                ModelTransform.pivot(-0.5F, 21.0F, 9.0F)
        );
        return TexturedModelData.of(modelData, 64, 64);
    }

    protected Iterable<ModelPart> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.stomach), ImmutableList.of(this.tail));
    }

    @Override
    public void setAngles(T sealEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.body.pitch = 0.0F;
        float flipperAnim = MathHelper.cos(limbAngle * 0.4F) * 0.5F * limbDistance;
        if (!sealEntity.isTouchingWater() && sealEntity.isOnGround()) {
            this.rightHindLeg.setAngles(0.0F, -flipperAnim, 0.0F);
            this.leftHindLeg.setAngles(0.0F, flipperAnim, 0.0F);
            this.rightFrontLeg.setAngles(0.0F, -flipperAnim * 2.0F, 0.0F);
            this.leftFrontLeg.setAngles(0.0F, flipperAnim * 2.0F, 0.0F);
        } else {
            this.rightHindLeg.setAngles(flipperAnim, 0.0F, 0.0F);
            this.leftHindLeg.setAngles(-flipperAnim, 0.0F, 0.0F);
            this.rightFrontLeg.setAngles(1.0F + flipperAnim, -(flipperAnim * 2.0F), -(1.0F + flipperAnim));
            this.leftFrontLeg.setAngles(1.0F + flipperAnim, flipperAnim * 2.0F, 1.0F + flipperAnim);
        }
    }
}
