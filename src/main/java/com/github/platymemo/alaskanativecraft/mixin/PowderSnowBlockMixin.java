package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
	@Inject(method = "canWalkOnPowderSnow", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private static void isWearingSnowshoesOrMukluks(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof LivingEntity livingEntity) {
			var stack = livingEntity.getEquippedStack(EquipmentSlot.FEET);
			cir.setReturnValue(stack.isOf(AlaskaItems.SNOWSHOES) || stack.isOf(AlaskaItems.MUKLUKS));
			return;
		}

		// Prevents leather boots from allowing snow walking
		cir.setReturnValue(false);
	}
}
