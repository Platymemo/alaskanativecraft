package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.KuspukSkirtFeatureRenderer;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.ShoulderPtarmiganFeatureRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer {

    protected PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher, EntityModel model, float shadowRadius) {
        super(dispatcher, model, shadowRadius);
        throw new AssertionError("Mixin constructor called, something is very wrong!");
    }

    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V")
    private void addFeatures(EntityRenderDispatcher dispatcher, boolean bl, CallbackInfo ci) {
        this.addFeature(new KuspukSkirtFeatureRenderer(this));
        this.addFeature(new ShoulderPtarmiganFeatureRenderer(this));
    }
}
