package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import org.jetbrains.annotations.NotNull;

public class HarpoonEntityModel extends SinglePartEntityModel<HarpoonEntity> {
    private static final String BODY = "body";
    private static final String TIP = "tip";
    private static final String WRAP = "wrap";
    private static final String PRONG = "prong";
    private final ModelPart root;

    public HarpoonEntityModel(ModelPart root) {
        this.root = root;
    }

    public static @NotNull TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild(BODY,
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(6.0F, -19.0F, -8.0F, 1.0F, 27.0F, 1.0F),
                ModelTransform.pivot(-6.0F, 16.0F, 7.0F)
        );
        root.addChild(WRAP,
                ModelPartBuilder.create()
                        .uv(4, 4)
                        .cuboid(6.0F, -20.0F, -9.0F, 1.0F, 1.0F, 2.0F),
                ModelTransform.pivot(-6.0F, 16.0F, 7.0F)
        );
        root.addChild(PRONG,
                ModelPartBuilder.create()
                        .uv(4, 0)
                        .cuboid(6.0F, -19.0F, -10.0F, 1.0F, 2.0F, 1.0F),
                ModelTransform.pivot(-6.0F, 16.0F, 7.0F)
        );
        root.addChild(TIP,
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
}