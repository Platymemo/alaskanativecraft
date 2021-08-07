package com.github.platymemo.alaskanativecraft.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow public abstract boolean isDamageable();

    @Shadow @Final private int maxDamage;

    @Redirect(method = {"getItemBarStep", "getItemBarColor"}, at = @At(value = "FIELD", target = "Lnet/minecraft/item/Item;maxDamage:I"))
    private int redirectForDurabilityMultiplier(Item item, ItemStack stack) {
        int newMaxDamage = this.maxDamage;
        final NbtCompound tag = stack.getNbt();
        if (this.isDamageable() && tag!=null && tag.contains("DurabilityMultiplier")) {
            newMaxDamage *= tag.getFloat("DurabilityMultiplier");
        }
        return newMaxDamage;
    }
}
