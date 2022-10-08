package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.PtarmiganEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.world.World;

@Mixin(FoxEntity.class)
public abstract class FoxMixin extends AnimalEntity {
	protected FoxMixin(EntityType<? extends AnimalEntity> type, World world) {
		super(type, world);
		throw new AssertionError("AlaskaNativeCraft's FoxMixin constructor called!");
	}

	// Mixin to the lambdas for chicken and rabbit predicate
	// There is some very weird stuff happening, but it works:tm:
	@Inject(at = @At("HEAD"), method = {"m_hklcoapa", "m_clkjuklc", "method_18262", "method_18261"}, cancellable = true, require = 1, remap = false)
	private static void addPtarmiganTarget(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof PtarmiganEntity) {
			cir.setReturnValue(true);
		}
	}
}
