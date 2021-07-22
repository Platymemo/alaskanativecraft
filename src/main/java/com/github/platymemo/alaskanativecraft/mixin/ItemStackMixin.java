package com.github.platymemo.alaskanativecraft.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract NbtCompound getOrCreateNbt();
    @Shadow @Nullable public abstract NbtCompound getNbt();

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void durabilityMultiplier(CallbackInfoReturnable<Integer> cir) {
        if (this.getOrCreateNbt().contains("DurabilityMultiplier")) {
            int newDurability = cir.getReturnValue();
            newDurability *= this.getNbt().getFloat("DurabilityMultiplier");
            cir.setReturnValue(newDurability);
        }
    }
}
