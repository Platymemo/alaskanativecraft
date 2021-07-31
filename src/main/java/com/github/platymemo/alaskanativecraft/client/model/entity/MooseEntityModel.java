package com.github.platymemo.alaskanativecraft.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class MooseEntityModel<T extends Entity> extends QuadrupedEntityModel<T> {
    public MooseEntityModel(ModelPart root) {
        super(root, true, 14.0F, 3.0F, 2.0F, 2.0F, 24);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
                        .uv(0, 25)
                        .cuboid(-3.5F, -5.0F, -11.0F, 7.0F, 10.0F, 9.0F)
                        .uv(0, 0)
                        .cuboid(-3.5F, -4.0F, -2.0F, 7.0F, 9.0F, 16.0F)
                        .uv(42, 11)
                        .cuboid(-1.0f, -3.0f, 14.0f, 2.0f, 3.0f, 1.0f),
                ModelTransform.pivot(0.5f, 7.0f, 0.0f));

        body.addChild("hump", ModelPartBuilder.create()
                        .uv(30, 0)
                        .cuboid(-3.0f, -6.0f, -10.0f, 6.0f, 4.0f, 7.0f),
                ModelTransform.of(0.0f, 2.0f, -1.8f,
                        -0.4363f, 0.0f, 0.0f));

        modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create()
                        .uv(0, 1)
                        .cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 8.0F, 5.0F)
                        .uv(52, 30)
                        .cuboid(0.0f, 6.0f, -1.0f, 2.0f, 12.0f, 3.0f),
                ModelTransform.pivot(4.0f, 6.0f, 9.0f));

        modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create()
                        .uv(0, 1)
                        .cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 8.0F, 5.0F)
                        .uv(52, 30)
                        .cuboid(-2.0f, 6.0f, -1.0f, 2.0f, 12.0f, 3.0f),
                ModelTransform.pivot(-3.0f, 6.0f, 9.0f));

        modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create()
                        .uv(0, 1)
                        .cuboid(-1.0F, -2.0F, -3.0F, 3.0F, 8.0F, 5.0F)
                        .uv(52, 30)
                        .cuboid(0.0f, 6.0f, -2.0f, 2.0f, 12.0f, 3.0f),
                ModelTransform.pivot(4.0f, 6.0f, -6.0f));

        modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create()
                        .uv(0, 1)
                        .cuboid(-2.0F, -2.0F, -3.0F, 3.0F, 8.0F, 5.0F)
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
}