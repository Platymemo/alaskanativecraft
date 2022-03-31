package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.HarpoonEntityModel;
import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class HarpoonEntityRenderer extends EntityRenderer<HarpoonEntity> {
    private static final Map<EntityType<?>, Identifier> TEXTURES = new HashMap<>();
    private final HarpoonEntityModel model;

    public HarpoonEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model = new HarpoonEntityModel(ctx.getPart(AlaskaModels.HARPOON));
    }

    public static Identifier getTexture(EntityType<?> type) {
        if (!TEXTURES.containsKey(type)) {
            TEXTURES.put(type, new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/harpoon/" + Registry.ENTITY_TYPE.getId(type).getPath() + ".png"));
        }
        return TEXTURES.get(type);
    }

    @Override
    public void render(@NotNull HarpoonEntity harpoon, float f, float g, @NotNull MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        {
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, harpoon.prevYaw, harpoon.getYaw()) - 90.0F));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, harpoon.prevPitch, harpoon.getPitch()) + 90.0F));
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, model.getLayer(this.getTexture(harpoon)), false, harpoon.isEnchanted());
            model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.scale(2.0F, -2.0F, -2.0F);
        }
        matrixStack.pop();
        super.render(harpoon, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(@NotNull HarpoonEntity harpoon) {
        return getTexture(harpoon.getType());
    }
}

