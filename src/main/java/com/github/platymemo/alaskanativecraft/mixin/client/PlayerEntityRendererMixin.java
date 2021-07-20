package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.KuspukSkirtFeatureRenderer;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.ShoulderPtarmiganFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer {

    protected PlayerEntityRendererMixin(EntityRendererFactory.Context context, EntityModel model, float shadowRadius) {
        super(context, model, shadowRadius);
        throw new AssertionError("Mixin constructor called, something is very wrong!");
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void addFeatures(EntityRendererFactory.Context context, boolean bl, CallbackInfo ci) {
        this.addFeature(new KuspukSkirtFeatureRenderer(this, context.getModelLoader()));
        this.addFeature(new ShoulderPtarmiganFeatureRenderer(this, context.getModelLoader()));
    }
}
