package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class HarpoonEntityModel extends SinglePartEntityModel<HarpoonEntity> {
    private final ModelPart root;

    public HarpoonEntityModel(ModelPart root) {
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild(HarpoonModelPartNames.BODY,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(6.0F, -19.0F, -8.0F, 1.0F, 27.0F, 1.0F),
                ModelTransform.pivot(-6.0F, 16.0F, 7.0F)
        );
        root.addChild(HarpoonModelPartNames.WRAP,
                ModelPartBuilder.create()
                        .uv(4, 4)
                        .cuboid(6.0F, -20.0F, -9.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.pivot(-6.0F, 16.0F, 7.0F)
        );
        root.addChild(HarpoonModelPartNames.PRONG,
                ModelPartBuilder.create()
                        .uv(4, 0)
                        .cuboid(6.0F, -19.0F, -10.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.pivot(-6.0F, 16.0F, 7.0F)
        );
        root.addChild(HarpoonModelPartNames.TIP,
                ModelPartBuilder.create()
                        .uv(4, 0)
                        .cuboid(6.0F, -23.0F, -8.0F, 1.0F, 3.0F, 1.0F),
                ModelTransform.pivot(-6.0F, 16.0F, 7.0F)
        );

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(HarpoonEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    static class HarpoonModelPartNames {
        static final String BODY = "body";
        static final String TIP = "tip";
        static final String WRAP = "wrap";
        static final String PRONG = "prong";
    }
}