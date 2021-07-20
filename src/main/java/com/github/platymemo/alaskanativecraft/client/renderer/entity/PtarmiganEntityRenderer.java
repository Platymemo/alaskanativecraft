package com.github.platymemo.alaskanativecraft.client.renderer.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.AlaskaNativeCraftModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.PtarmiganEntityModel;
import com.github.platymemo.alaskanativecraft.entity.PtarmiganEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class PtarmiganEntityRenderer extends MobEntityRenderer<PtarmiganEntity, PtarmiganEntityModel> {
    public static final Identifier[] TEXTURES = {new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/ptarmigan/ptarmigan.png"),
            new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/ptarmigan/brown_ptarmigan.png"),
            new Identifier(AlaskaNativeCraft.MOD_ID, "textures/entity/ptarmigan/grey_ptarmigan.png")};

    public PtarmiganEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PtarmiganEntityModel(ctx.getPart(AlaskaNativeCraftModels.PTARMIGAN)), 0.3F);
    }

    public Identifier getTexture(PtarmiganEntity ptarmiganEntity) {
        return TEXTURES[ptarmiganEntity.getPtarmiganType()];
    }

    public float getAnimationProgress(PtarmiganEntity ptarmiganEntity, float f) {
        float g = MathHelper.lerp(f, ptarmiganEntity.prevFlapProgress, ptarmiganEntity.flapProgress);
        float h = MathHelper.lerp(f, ptarmiganEntity.prevMaxWingDeviation, ptarmiganEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0F) * h;
    }
}
