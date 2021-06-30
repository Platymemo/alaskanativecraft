package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.KuspukSkirtFeatureRenderer;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandEntityRendererMixin extends LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel> {

    protected ArmorStandEntityRendererMixin(EntityRendererFactory.Context context, ArmorStandArmorEntityModel model, float shadowRadius) {
        super(context, model, shadowRadius);
        throw new AssertionError("Mixin constructor called, something is very wrong!");
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void addFeatures(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.addFeature(new KuspukSkirtFeatureRenderer<>(this, context.getModelLoader()));
    }
}
