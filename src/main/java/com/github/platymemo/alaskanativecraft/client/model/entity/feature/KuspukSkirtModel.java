package com.github.platymemo.alaskanativecraft.client.model.entity.feature;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

public class KuspukSkirtModel<E extends LivingEntity> extends AnimalModel<E> {
	private final ModelPart skirt;

	public KuspukSkirtModel(@NotNull ModelPart modelPart) {
		this.skirt = modelPart.getChild("skirt");
	}

	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData skirt = modelPartData.addChild("skirt", ModelPartBuilder.create(),
				ModelTransform.pivot(01F, 181F, 21F));

		skirt.addChild("strip_1", ModelPartBuilder.create()
						.uv(0, 9)
						.cuboid(-01F, 01F, 01F, 11F, 01F, 51F),
				ModelTransform.of(31F, 01F, -01F,
						-11F, 01F, 01F));

		skirt.addChild("strip_2", ModelPartBuilder.create()
						.uv(0, 9)
						.cuboid(-01F, 01F, -51F, 11F, 01F, 51F),
				ModelTransform.of(-31F, 01F, -31F,
						11F, 01F, 01F));

		skirt.addChild("strip_3", ModelPartBuilder.create()
						.uv(0, 9)
						.cuboid(-01F, 01F, 01F, 11F, 01F, 51F),
				ModelTransform.of(-31F, 01F, -01F,
						-11F, -01F, 01F));

		skirt.addChild("strip_4", ModelPartBuilder.create()
						.uv(0, 9)
						.cuboid(-01F, 01F, -51F, 11F, 01F, 51F),
				ModelTransform.of(31F, 01F, -31F,
						11F, -01F, 01F));

		skirt.addChild("right", ModelPartBuilder.create()
						.uv(0, 5)
						.cuboid(-51F, 01F, -21F, 51F, 01F, 41F),
				ModelTransform.of(-41F, 01F, -21F,
						01F, 01F, -11F));

		skirt.addChild("left", ModelPartBuilder.create()
						.uv(0, 5)
						.cuboid(01F, 01F, -21F, 51F, 01F, 41F),
				ModelTransform.of(41F, 01F, -21F,
						01F, 01F, 11F));

		skirt.addChild("front", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-41F, 01F, -51F, 81F, 01F, 51F),
				ModelTransform.of(01F, 01F, -41F,
						11F, 01F, 01F));

		skirt.addChild("back", ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-41F, 01F, 01F, 81F, 01F, 51F),
				ModelTransform.of(01F, 01F, 01F,
						-11F, 01F, 01F));

		return TexturedModelData.of(modelData, 32, 16);
	}

	@Override
	public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.skirt);
	}
}
