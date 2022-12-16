package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

// TODO refine
// Made with Blockbench 4.4.3
public class WhaleEntityModel extends SinglePartEntityModel<WhaleEntity> {
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart torso;
	private final ModelPart fins;
	private final ModelPart tail;
	private final ModelPart tailFin;

	public WhaleEntityModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.torso = root.getChild("torso");
		this.fins = root.getChild("fins");
		this.tail = this.torso.getChild("tail");
		this.tailFin = this.tail.getChild("tailFin");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -16.0F, -120.0F));
		head.addChild("mouth_r1", ModelPartBuilder.create().uv(352, 0).cuboid(-24.0F, -26.0F, -120.0F, 80.0F, 15.0F, 68.0F, new Dilation(0.0F)), ModelTransform.of(-16.0F, 40.0F, 54.0F, 0.1745F, 0.0F, 0.0F));
		head.addChild("head_r1", ModelPartBuilder.create().uv(262, 318).cuboid(-28.0F, -73.0F, -132.0F, 89.0F, 68.0F, 80.0F, new Dilation(0.0F)), ModelTransform.of(-16.0F, 40.0F, 54.0F, -0.0873F, 0.0F, 0.0F));

		ModelPartData chest = modelPartData.addChild("chest", ModelPartBuilder.create().uv(0, 0).cuboid(-47.0F, -80.0F, -121.0F, 96.0F, 80.0F, 160.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		chest.addChild("hump_r1", ModelPartBuilder.create().uv(419, 252).cuboid(8.0F, -79.0F, 64.0F, 16.0F, 16.0F, 32.0F, new Dilation(0.0F)), ModelTransform.of(-16.0F, 0.0F, -66.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData fins = modelPartData.addChild("fins", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		fins.addChild("left_fin_r1", ModelPartBuilder.create().uv(257, 240).cuboid(16.0F, -27.0F, -35.0F, 64.0F, 11.0F, 33.0F, new Dilation(0.0F)), ModelTransform.of(16.0F, 0.0F, -66.0F, 0.0F, -0.1745F, 0.3054F));
		fins.addChild("right_fin_r1", ModelPartBuilder.create().uv(352, 83).cuboid(-80.0F, -27.0F, -35.0F, 64.0F, 11.0F, 33.0F, new Dilation(0.0F)), ModelTransform.of(-16.0F, 0.0F, -66.0F, 0.0F, 0.1745F, -0.3054F));

		ModelPartData torso = modelPartData.addChild("torso", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -19.0F, 38.5F));
		torso.addChild("lower_torso_r1", ModelPartBuilder.create().uv(0, 398).cuboid(-23.0F, -69.0F, 188.0F, 77.0F, 65.0F, 45.0F, new Dilation(0.0F)), ModelTransform.of(-16.0F, 43.0F, -104.5F, 0.0873F, 0.0F, 0.0F));
		torso.addChild("upper_torso_r1", ModelPartBuilder.create().uv(0, 240).cuboid(-26.0F, -34.0F, -1.0F, 86.0F, 73.0F, 85.0F, new Dilation(0.0F)), ModelTransform.of(-16.0F, 0.0F, -3.5F, 0.0873F, 0.0F, 0.0F));

		ModelPartData tail = torso.addChild("tail", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -18.0F, 125.5F));
		tail.addChild("stock_r1", ModelPartBuilder.create().uv(0, 0).cuboid(8.0F, -20.0F, 235.0F, 16.0F, 16.0F, 50.0F, new Dilation(0.0F)), ModelTransform.of(-15.0F, 61.0F, -230.0F, 0.2182F, 0.0F, 0.0F));

		ModelPartData tailFin = tail.addChild("tailFin", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -9.0F, 29.0F));
		tailFin.addChild("left_fluke_r1", ModelPartBuilder.create().uv(0, 66).cuboid(75.0F, -16.0F, 248.0F, 48.0F, 12.0F, 26.0F, new Dilation(0.0F)), ModelTransform.of(-16.0F, 70.0F, -259.0F, 0.2182F, -0.2182F, 0.0F));
		tailFin.addChild("right_fluke_r1", ModelPartBuilder.create().uv(0, 104).cuboid(-94.0F, -16.0F, 255.0F, 48.0F, 12.0F, 26.0F, new Dilation(0.0F)), ModelTransform.of(-16.0F, 70.0F, -259.0F, 0.2182F, 0.2182F, 0.0F));

		return TexturedModelData.of(modelData, 1024, 1024);
	}

	@Override
	public void setAngles(WhaleEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.torso.pitch = headPitch * MathHelper.RADIANS_PER_DEGREE;
		this.torso.yaw = headYaw * MathHelper.RADIANS_PER_DEGREE;
		if (entity.getVelocity().horizontalLengthSquared() > 1.0E-7) {
			this.torso.pitch += -0.05F - 0.05F * MathHelper.cos(animationProgress * 0.3F);
			this.tail.pitch = -0.1F * MathHelper.cos(animationProgress * 0.3F);
			this.tailFin.pitch = -0.2F * MathHelper.cos(animationProgress * 0.3F);
		}
	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}
}
