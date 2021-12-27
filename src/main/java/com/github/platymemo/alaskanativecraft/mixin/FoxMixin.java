package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.PtarmiganEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoxEntity.class)
public abstract class FoxMixin extends AnimalEntity {

    protected FoxMixin(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
        throw new AssertionError("AlaskaNativeCraft's FoxMixin constructor called!");
    }

    @Inject(at = @At("HEAD"), method = "method_18262", cancellable = true, remap = false)
    private static void addPtarmiganTarget(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof PtarmiganEntity) {
            cir.setReturnValue(true);
        }
    }
}
