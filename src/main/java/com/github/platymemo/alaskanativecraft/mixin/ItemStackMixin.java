package com.github.platymemo.alaskanativecraft.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    @Nullable
    public abstract NbtCompound getNbt();

    @Shadow
    public abstract boolean hasNbt();

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void durabilityMultiplier(CallbackInfoReturnable<Integer> cir) {
        if (this.hasNbt() && this.getNbt().contains("DurabilityMultiplier")) {
            int newDurability = cir.getReturnValue();
            newDurability *= this.getNbt().getFloat("DurabilityMultiplier");
            cir.setReturnValue(newDurability);
        }
    }
}
