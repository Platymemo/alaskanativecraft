package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.PtarmiganEntityModel;
import com.github.platymemo.alaskanativecraft.entity.PtarmiganEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class PtarmiganEntityRenderer extends MobEntityRenderer<PtarmiganEntity, PtarmiganEntityModel> {
    public static final Identifier TEXTURE = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/ptarmigan/ptarmigan.png");

    public PtarmiganEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new PtarmiganEntityModel(), 0.3F);
    }

    public Identifier getTexture(PtarmiganEntity ptarmiganEntity) {
        return TEXTURE;
    }

    public float getAnimationProgress(PtarmiganEntity ptarmiganEntity, float f) {
        float g = MathHelper.lerp(f, ptarmiganEntity.prevFlapProgress, ptarmiganEntity.flapProgress);
        float h = MathHelper.lerp(f, ptarmiganEntity.prevMaxWingDeviation, ptarmiganEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0F) * h;
    }
}
