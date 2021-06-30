package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class DogsledEntityModel extends CompositeEntityModel<DogsledEntity> {
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData frame = modelPartData.addChild("frame", ModelPartBuilder.create()
                        .uv(20, 18)
                        .cuboid(-5.0F, -9.0F, -18.0F, 10.0F, 1.0F, 1.0F)
                        .uv(0, 0)
                        .cuboid(-4.0F, -9.0F, -17.0F, 8.0F, 1.0F, 17.0F)
                        .uv(0, 18)
                        .cuboid(-4.0f, -14.0f, -1.0f, 8.0f, 1.0f, 1.0f),
                ModelTransform.pivot(0.0f, 24.0f, 3.0f));

        ModelPartData leftRunner = modelPartData.addChild("leftRunner", ModelPartBuilder.create()
                        .uv(0, 18)
                        .cuboid(-1.0F, -1.0F, -9.0F, 1.0F, 1.0F, 18.0F)
                        .uv(0, 0)
                        .cuboid(-1.0F, -5.0F, -5.0F, 1.0F, 4.0F, 1.0F)
                        .uv(0, 20)
                        .cuboid(-1.0F, -10.0F, 2.0F, 1.0F, 9.0F, 1.0F)
                        .uv(20, 20)
                        .cuboid(-1.0f, -5.0f, -14.0f, 1.0f, 1.0f, 9.0f),
                ModelTransform.pivot(5.0f, 0.0f, 0.0f));

        leftRunner.addChild("cube_r1", ModelPartBuilder.create()
                        .uv(29, 30)
                        .cuboid(-0.999f, 0.0f, -9.0f, 1.0f, 1.0f, 9.0f),
                ModelTransform.of(0.0f, -10.0f, 2.0f,
                        0.6109f, 0.0f, 0.0f));

        leftRunner.addChild("cube_r2", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-0.499f, -1.0f, -7.0f, 1.0f, 1.0f, 7.0f),
                ModelTransform.of(-0.5f, 0.0f, -9.0f,
                        -0.6109f, 0.0f, 0.0f));

        ModelPartData rightRunner = modelPartData.addChild("rightRunner", ModelPartBuilder.create()
                        .uv(0, 18)
                        .cuboid(-1.0F, -1.0F, -9.0F, 1.0F, 1.0F, 18.0F)
                        .uv(0, 0)
                        .cuboid(-1.0F, -5.0F, -5.0F, 1.0F, 4.0F, 1.0F)
                        .uv(0, 20)
                        .cuboid(-1.0F, -10.0F, 2.0F, 1.0F, 9.0F, 1.0F)
                        .uv(20, 20)
                        .cuboid(-1.0f, -5.0f, -14.0f, 1.0f, 1.0f, 9.0f),
                ModelTransform.pivot(-4.0f, 0.0f, 0.0f));

        rightRunner.addChild("cube_r3", ModelPartBuilder.create()
                        .uv(29, 30)
                        .cuboid(-1.001f, 0.0f, -9.0f, 1.0f, 1.0f, 9.0f),
                ModelTransform.of(0.0f, -10.0f, 2.0f,
                        0.6109f, 0.0f, 0.0f));

        rightRunner.addChild("cube_r4", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-0.501f, -1.0f, -7.0f, 1.0f, 1.0f, 7.0f),
                ModelTransform.of(-0.5f, 0.0f, -9.0f,
                        -0.6109f, 0.0f, 0.0f));

        return TexturedModelData.of(modelData, 64, 64);
    }

    private final ModelPart frame;
    private final ImmutableList<ModelPart> parts;



    public DogsledEntityModel(ModelPart modelPart) {
        /*textureWidth = 64;
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
        rightRunner.addChild(cube_r4);*/

        frame = modelPart.getChild("frame");
        ModelPart leftRunner = modelPart.getChild("leftRunner");
        //ModelPart cube_r1 = leftRunner.getChild("cube_r1");
        //ModelPart cube_r2 = leftRunner.getChild("cube_r2");
        ModelPart rightRunner = modelPart.getChild("rightRunner");
        //ModelPart cube_r3 = rightRunner.getChild("cube_r3");
        //ModelPart cube_r4 = rightRunner.getChild("cube_r4");

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