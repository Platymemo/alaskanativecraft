package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class DogsledEntityModel extends SinglePartEntityModel<DogsledEntity> {
    private static final String FRAME = "frame";
    private static final String LEFT_RUNNER = "left_runner";
    private static final String RIGHT_RUNNER = "right_runner";
    private final ModelPart root;

    public DogsledEntityModel(ModelPart root) {
        this.root = root;

        ModelPart leftRunner = new ModelPart(this);

        ModelPart cube_r1 = new ModelPart(this);
        cube_r1.setPivot(0.0F, -10.0F, 2.0F);
        setRotationAngle(cube_r1, 0.6109F, 0.0F, 0.0F);
        cube_r1.uv(29, 30).cuboid(-0.999F, 0.0F, -9.0F, 1.0F, 1.0F, 9.0F);
        leftRunner.addChild(cube_r1);

        ModelPart cube_r2 = new ModelPart(this);
        cube_r2.setPivot(-0.5F, 0.0F, -9.0F);
        setRotationAngle(cube_r2, -0.6109F, 0.0F, 0.0F);
        cube_r2.uv(0, 0).cuboid(-0.499F, -1.0F, -7.0F, 1.0F, 1.0F, 7.0F);
        leftRunner.addChild(cube_r2);

        ModelPart rightRunner = new ModelPart(this);

        ModelPart cube_r3 = new ModelPart(this);
        cube_r3.setPivot(0.0F, -10.0F, 2.0F);
        setRotationAngle(cube_r3, 0.6109F, 0.0F, 0.0F);
        cube_r3.uv(29, 30).cuboid(-1.001F, 0.0F, -9.0F, 1.0F, 1.0F, 9.0F);
        rightRunner.addChild(cube_r3);

        ModelPart cube_r4 = new ModelPart(this);
        cube_r4.setPivot(-0.5F, 0.0F, -9.0F);
        setRotationAngle(cube_r4, -0.6109F, 0.0F, 0.0F);
        cube_r4.uv(0, 0).cuboid(-0.501F, -1.0F, -7.0F, 1.0F, 1.0F, 7.0F);
        rightRunner.addChild(cube_r4);

        ImmutableList.Builder<ModelPart> builder = ImmutableList.builder();
        builder.add(this.frame).add(leftRunner).add(rightRunner);
        this.parts = builder.build();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild(FRAME,
                ModelPartBuilder.create()
                        .uv(20, 18).cuboid(-5.0F, -9.0F, -18.0F, 10.0F, 1.0F, 1.0F)
                        .uv(0, 0).cuboid(-4.0F, -9.0F, -17.0F, 8.0F, 1.0F, 17.0F)
                        .uv(0, 18).cuboid(-4.0F, -14.0F, -1.0F, 8.0F, 1.0F, 1.0F),
                ModelTransform.pivot(0.0F, 24.0F, 3.0F)
        );
        ModelPartBuilder baseRunnerFrame = ModelPartBuilder.create()
                .uv(0, 18).cuboid(-1.0F, -1.0F, -9.0F, 1.0F, 1.0F, 18.0F)
                .uv(0, 0).cuboid(-1.0F, -5.0F, -5.0F, 1.0F, 4.0F, 1.0F)
                .uv(0, 20).cuboid(-1.0F, -10.0F, 2.0F, 1.0F, 9.0F, 1.0F)
                .uv(20, 20).cuboid(-1.0F, -5.0F, -14.0F, 1.0F, 1.0F, 9.0F)
        root.addChild(LEFT_RUNNER,
                baseRunnerFrame,
                ModelTransform.pivot(5.0F, 0.0F, 0.0F)
        );
        root.addChild(RIGHT_RUNNER,
                baseRunnerFrame,
                ModelTransform.pivot(-4.0F, 0.0F, 0.0F)
        );

        ModelPartData leftRunner = root.getChild(LEFT_RUNNER);
        leftRunner.addChild()

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(DogsledEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root.pivotY = 4.0F - animationProgress;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.translate(0.0F, 0.0F, 0.3F);
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    static class
}