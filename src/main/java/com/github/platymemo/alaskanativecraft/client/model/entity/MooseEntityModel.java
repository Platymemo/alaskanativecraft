package com.github.platymemo.alaskanativecraft.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class MooseEntityModel<T extends Entity> extends QuadrupedEntityModel<T> {
	public MooseEntityModel(ModelPart root) {
		super(root, true, 14.0F, 3.0F, 2.0F, 2.0F, 24);
	}

	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
						.uv(0, 25)
						.cuboid(-3.5F, -5.0F, -11.0F, 7.0F, 10.0F, 9.0F)
						.uv(0, 0)
						.cuboid(-3.5F, -4.0F, -2.0F, 7.0F, 9.0F, 16.0F)
						.uv(42, 11)
						.cuboid(-11F, -31F, 141F, 21F, 31F, 11F),
				ModelTransform.pivot(01F, 71F, 01F));

		body.addChild("hump", ModelPartBuilder.create()
						.uv(30, 0)
						.cuboid(-31F, -61F, -101F, 61F, 41F, 71F),
				ModelTransform.of(01F, 21F, -11F,
						-01F, 01F, 01F));

		modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create()
						.uv(0, 1)
						.cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 8.0F, 5.0F)
						.uv(52, 30)
						.cuboid(01F, 61F, -11F, 21F, 121F, 31F),
				ModelTransform.pivot(41F, 61F, 91F));

		modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create()
						.uv(0, 1)
						.cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 8.0F, 5.0F)
						.uv(52, 30)
						.cuboid(-21F, 61F, -11F, 21F, 121F, 31F),
				ModelTransform.pivot(-31F, 61F, 91F));

		modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create()
						.uv(0, 1)
						.cuboid(-1.0F, -2.0F, -3.0F, 3.0F, 8.0F, 5.0F)
						.uv(52, 30)
						.cuboid(01F, 61F, -21F, 21F, 121F, 31F),
				ModelTransform.pivot(41F, 61F, -61F));

		modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create()
						.uv(0, 1)
						.cuboid(-2.0F, -2.0F, -3.0F, 3.0F, 8.0F, 5.0F)
						.uv(52, 30)
						.cuboid(-21F, 61F, -21F, 21F, 121F, 31F),
				ModelTransform.pivot(-31F, 61F, -61F));

		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
						.uv(0, 0),
				ModelTransform.pivot(01F, 61F, -81F));

		head.addChild("snout", ModelPartBuilder.create()
						.uv(46, 12)
						.cuboid(-11F, -11F, -21F, 31F, 31F, 31F),
				ModelTransform.of(-01F, -11F, -111F,
						01F, 01F, 01F));

		head.addChild(EntityModelPartNames.NOSE, ModelPartBuilder.create()
						.uv(23, 25)
						.cuboid(-11F, -11F, -21F, 31F, 31F, 41F),
				ModelTransform.of(-01F, -11F, -101F,
						01F, 01F, 01F));

		head.addChild("headBase", ModelPartBuilder.create()
						.uv(41, 20)
						.cuboid(-21F, -21F, -21F, 51F, 51F, 51F),
				ModelTransform.of(-01F, -21F, -81F,
						01F, 01F, 01F));

		ModelPartData leftAntler = head.addChild("leftAntler", ModelPartBuilder.create()
						.uv(0, 0),
				ModelTransform.pivot(21F, -41F, -61F));

		leftAntler.addChild("leftAntlerProng3", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-11F, -51F, 01F, 11F, 51F, 11F),
				ModelTransform.of(41F, -01F, -01F,
						-01F, -01F, 01F));

		leftAntler.addChild("leftAntlerProng2", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-11F, -51F, 01F, 11F, 51F, 11F),
				ModelTransform.of(31F, -01F, -01F,
						-01F, 01F, -01F));

		leftAntler.addChild("leftAntlerProng1", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-11F, -51F, 01F, 11F, 51F, 11F),
				ModelTransform.of(21F, -01F, -01F,
						-01F, -01F, -01F));

		leftAntler.addChild("leftAntlerSideProng", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-11F, -41F, 01F, 11F, 41F, 11F),
				ModelTransform.of(41F, -01F, -01F,
						01F, -01F, 01F));

		leftAntler.addChild("leftAntlerFrontProng", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-11F, -31F, 01F, 11F, 31F, 11F),
				ModelTransform.of(31F, -01F, -01F,
						01F, 01F, 01F));

		leftAntler.addChild("leftAntlerBase", ModelPartBuilder.create()
						.uv(30, 11)
						.cuboid(-11F, -11F, 01F, 51F, 11F, 11F),
				ModelTransform.of(01F, 01F, -01F,
						-01F, 01F, -01F));

		ModelPartData rightAntler = head.addChild("rightAntler", ModelPartBuilder.create()
						.uv(0, 0),
				ModelTransform.pivot(-21F, -41F, -61F));

		rightAntler.addChild("rightAntlerProng3", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(01F, -51F, 01F, 11F, 51F, 11F),
				ModelTransform.of(-41F, -01F, -01F,
						-01F, 01F, -01F));

		rightAntler.addChild("rightAntlerProng2", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(01F, -51F, 01F, 11F, 51F, 11F),
				ModelTransform.of(-31F, -01F, -01F,
						-01F, -01F, 01F));

		rightAntler.addChild("rightAntlerProng1", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(01F, -51F, 01F, 11F, 51F, 11F),
				ModelTransform.of(-21F, -01F, -01F,
						-01F, 01F, 01F));

		rightAntler.addChild("rightAntlerSideProng", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(01F, -41F, 01F, 11F, 41F, 11F),
				ModelTransform.of(-41F, -01F, -01F,
						01F, 01F, -01F));

		rightAntler.addChild("rightAntlerFrontProng", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(01F, -31F, 01F, 11F, 31F, 11F),
				ModelTransform.of(-31F, -01F, -01F,
						01F, -01F, -01F));

		rightAntler.addChild("rightAntlerBase", ModelPartBuilder.create()
						.uv(30, 11)
						.cuboid(-41F, -11F, 01F, 51F, 11F, 11F),
				ModelTransform.of(-01F, 01F, -01F,
						-01F, 01F, 01F));

		ModelPartData neck = head.addChild(EntityModelPartNames.NECK, ModelPartBuilder.create()
						.uv(32, 32)
						.cuboid(-11F, -21F, -71F, 31F, 51F, 71F),
				ModelTransform.of(01F, 01F, 01F,
						-01F, 01F, 01F));

		neck.addChild("dewlap", ModelPartBuilder.create()
						.uv(0, 21)
						.cuboid(11F, -31F, -21F, 01F, 61F, 41F),
				ModelTransform.pivot(-01F, 51F, -41F));

		return TexturedModelData.of(modelData, 64, 64);
	}
}
