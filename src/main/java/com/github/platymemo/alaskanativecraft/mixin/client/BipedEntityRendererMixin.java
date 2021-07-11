package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.KuspukSkirtFeatureRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityRenderer.class)
public abstract class BipedEntityRendererMixin<T extends MobEntity, M extends BipedEntityModel<T>> extends MobEntityRenderer<T, M> {

    protected BipedEntityRendererMixin(EntityRendererFactory.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
        throw new AssertionError("Mixin constructor called, something is very wrong!");
    }

    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Lnet/minecraft/client/render/entity/model/BipedEntityModel;FFFF)V")
    private void addFeatures(EntityRendererFactory.Context context, M bipedEntityModel, float f, float g, float h, float i, CallbackInfo ci) {
        this.addFeature(new KuspukSkirtFeatureRenderer<>(this, context.getModelLoader()));
    }
}
