package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.minecraft.enchantment.EfficiencyEnchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EfficiencyEnchantment.class)
public class EfficiencyEnchantmentMixin {

    @Inject(at = @At("HEAD"), method = "isAcceptableItem", cancellable = true)
    private void isUlu(ItemStack itemStack, CallbackInfoReturnable<Boolean> ci) {
        if (itemStack.getItem() == AlaskaItems.ULU) {
            ci.setReturnValue(true);
        }
    }
}
