package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    @Inject(method = "canWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
    private static void isWearingSnowshoes(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity
                && (livingEntity.getEquippedStack(EquipmentSlot.FEET).isOf(AlaskaItems.SNOWSHOES)
                || livingEntity.getEquippedStack(EquipmentSlot.FEET).isOf(AlaskaItems.MUKLUKS))) {
            cir.setReturnValue(true);
        }
    }
}
