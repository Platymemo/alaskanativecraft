package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class DogsledEntityModel extends CompositeEntityModel<DogsledEntity> {
	private final ModelPart frame;
	private final ImmutableList<ModelPart> parts;

	public DogsledEntityModel() {
		textureWidth = 64;
		textureHeight = 64;
		frame = new ModelPart(this);
		frame.setPivot(0.0F, 24.0F, 3.0F);
		frame.setTextureOffset(20, 18).addCuboid(-5.0F, -9.0F, -18.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		frame.setTextureOffset(0, 0).addCuboid(-4.0F, -9.0F, -17.0F, 8.0F, 1.0F, 17.0F, 0.0F, false);
		frame.setTextureOffset(0, 18).addCuboid(-4.0F, -14.0F, -1.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);

		ModelPart leftRunner = new ModelPart(this);
		leftRunner.setPivot(5.0F, 0.0F, 0.0F);
		leftRunner.setTextureOffset(0, 18).addCuboid(-1.0F, -1.0F, -9.0F, 1.0F, 1.0F, 18.0F, 0.0F, false);
		leftRunner.setTextureOffset(0, 0).addCuboid(-1.0F, -5.0F, -5.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		leftRunner.setTextureOffset(0, 20).addCuboid(-1.0F, -10.0F, 2.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		leftRunner.setTextureOffset(20, 20).addCuboid(-1.0F, -5.0F, -14.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);

		ModelPart cube_r1 = new ModelPart(this);
		cube_r1.setPivot(0.0F, -10.0F, 2.0F);
		setRotationAngle(cube_r1, 0.6109F, 0.0F, 0.0F);
		cube_r1.setTextureOffset(29, 30).addCuboid(-0.999F, 0.0F, -9.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
		leftRunner.addChild(cube_r1);

		ModelPart cube_r2 = new ModelPart(this);
		cube_r2.setPivot(-0.5F, 0.0F, -9.0F);
		setRotationAngle(cube_r2, -0.6109F, 0.0F, 0.0F);
		cube_r2.setTextureOffset(0, 0).addCuboid(-0.499F, -1.0F, -7.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
		leftRunner.addChild(cube_r2);

		ModelPart rightRunner = new ModelPart(this);
		rightRunner.setPivot(-4.0F, 0.0F, 0.0F);
		rightRunner.setTextureOffset(0, 18).addCuboid(-1.0F, -1.0F, -9.0F, 1.0F, 1.0F, 18.0F, 0.0F, false);
		rightRunner.setTextureOffset(0, 0).addCuboid(-1.0F, -5.0F, -5.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		rightRunner.setTextureOffset(0, 20).addCuboid(-1.0F, -10.0F, 2.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		rightRunner.setTextureOffset(20, 20).addCuboid(-1.0F, -5.0F, -14.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);

		ModelPart cube_r3 = new ModelPart(this);
		cube_r3.setPivot(0.0F, -10.0F, 2.0F);
		setRotationAngle(cube_r3, 0.6109F, 0.0F, 0.0F);
		cube_r3.setTextureOffset(29, 30).addCuboid(-1.001F, 0.0F, -9.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
		rightRunner.addChild(cube_r3);

		ModelPart cube_r4 = new ModelPart(this);
		cube_r4.setPivot(-0.5F, 0.0F, -9.0F);
		setRotationAngle(cube_r4, -0.6109F, 0.0F, 0.0F);
		cube_r4.setTextureOffset(0, 0).addCuboid(-0.501F, -1.0F, -7.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
		rightRunner.addChild(cube_r4);

		ImmutableList.Builder<ModelPart> builder = ImmutableList.builder();
		builder.add(this.frame).add(leftRunner).add(rightRunner);
		this.parts = builder.build();
	}

	@Override
	public void setAngles(DogsledEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.frame.pivotY = 4.0F - animationProgress;
	}

	@Override
	public ImmutableList<ModelPart> getParts() {
		return this.parts;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.translate(0.0F, 0.0F, 0.3F);
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}