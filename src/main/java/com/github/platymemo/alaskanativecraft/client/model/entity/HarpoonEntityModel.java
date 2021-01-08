package com.github.platymemo.alaskanativecraft.client.model.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class HarpoonEntityModel extends EntityModel<Entity> {
    private final ModelPart harpoon;

    public HarpoonEntityModel() {
        textureWidth = 32;
        textureHeight = 32;

        harpoon = new ModelPart(this);
        harpoon.setPivot(-6.0F, 16.0F, 7.0F);
        harpoon.setTextureOffset(4, 0).addCuboid(6.0F, -19.0F, -10.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        harpoon.setTextureOffset(0, 0).addCuboid(6.0F, -19.0F, -8.0F, 1.0F, 27.0F, 1.0F, 0.0F, false);
        harpoon.setTextureOffset(4, 4).addCuboid(6.0F, -20.0F, -9.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        harpoon.setTextureOffset(4, 0).addCuboid(6.0F, -23.0F, -8.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        // Not Called
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer	buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        harpoon.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}