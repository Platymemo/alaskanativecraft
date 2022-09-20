package com.github.platymemo.alaskanativecraft.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FoxEntity.class)
public abstract class FoxMixin extends AnimalEntity {

    protected FoxMixin(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
        throw new AssertionError("AlaskaNativeCraft's FoxMixin constructor called!");
    }

    //TODO: locate this method
//    @Inject(at = @At("HEAD"), method = "method_18262", cancellable = true, remap = false)
//    private static void addPtarmiganTarget(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
//        if (entity instanceof PtarmiganEntity) {
//            cir.setReturnValue(true);
//        }
//    }
}
