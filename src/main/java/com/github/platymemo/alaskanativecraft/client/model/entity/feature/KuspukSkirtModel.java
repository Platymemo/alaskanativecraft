package com.github.platymemo.alaskanativecraft.client.model.entity.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

public class KuspukSkirtModel<T extends LivingEntity> extends AnimalModel<T> {
	private final ModelPart Skirt;

	public KuspukSkirtModel() {
		textureWidth = 32;
		textureHeight = 16;

		Skirt = new ModelPart(this);
		Skirt.setPivot(0.0F, 12.0F, 2.0F);


		ModelPart cube_r1 = new ModelPart(this);
		cube_r1.setPivot(3.7F, 0.0F, -0.3F);
		Skirt.addChild(cube_r1);
		setRotationAngle(cube_r1, -1.309F, 0.7854F, 0.0F);
		cube_r1.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);

		ModelPart cube_r2 = new ModelPart(this);
		cube_r2.setPivot(-3.7F, 0.0F, -3.7F);
		Skirt.addChild(cube_r2);
		setRotationAngle(cube_r2, 1.309F, 0.7854F, 0.0F);
		cube_r2.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, -5.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);

		ModelPart cube_r3 = new ModelPart(this);
		cube_r3.setPivot(-3.7F, 0.0F, -0.3F);
		Skirt.addChild(cube_r3);
		setRotationAngle(cube_r3, -1.309F, -0.7854F, 0.0F);
		cube_r3.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);

		ModelPart cube_r4 = new ModelPart(this);
		cube_r4.setPivot(-4.0F, 0.0F, -2.0F);
		Skirt.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, -1.309F);
		cube_r4.setTextureOffset(0, 5).addCuboid(-5.0F, 0.0F, -2.0F, 5.0F, 0.0F, 4.0F, 0.0F, false);

		ModelPart cube_r5 = new ModelPart(this);
		cube_r5.setPivot(4.0F, 0.0F, -2.0F);
		Skirt.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 1.309F);
		cube_r5.setTextureOffset(0, 5).addCuboid(0.0F, 0.0F, -2.0F, 5.0F, 0.0F, 4.0F, 0.0F, false);

		ModelPart cube_r6 = new ModelPart(this);
		cube_r6.setPivot(0.0F, 0.0F, -4.0F);
		Skirt.addChild(cube_r6);
		setRotationAngle(cube_r6, 1.309F, 0.0F, 0.0F);
		cube_r6.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, -5.0F, 8.0F, 0.0F, 5.0F, 0.0F, false);

		ModelPart cube_r7 = new ModelPart(this);
		cube_r7.setPivot(0.0F, 0.0F, 0.0F);
		Skirt.addChild(cube_r7);
		setRotationAngle(cube_r7, -1.309F, 0.0F, 0.0F);
		cube_r7.setTextureOffset(0, 0).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 5.0F, 0.0F, false);

		ModelPart cube_r8 = new ModelPart(this);
		cube_r8.setPivot(3.7F, 0.0F, -3.7F);
		Skirt.addChild(cube_r8);
		setRotationAngle(cube_r8, 1.309F, -0.7854F, 0.0F);
		cube_r8.setTextureOffset(0, 9).addCuboid(-0.5F, 0.0F, -5.0F, 1.0F, 0.0F, 5.0F, 0.0F, false);
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
		return ImmutableList.of(this.Skirt);
	}

	public void setRotationAngle(ModelPart ModelPart, float x, float y, float z) {
		ModelPart.pivotX = x;
		ModelPart.pivotY = y;
		ModelPart.pivotZ = z;
	}
}