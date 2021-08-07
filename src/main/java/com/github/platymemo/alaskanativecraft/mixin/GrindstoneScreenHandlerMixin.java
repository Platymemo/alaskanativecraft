package com.github.platymemo.alaskanativecraft.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneScreenHandler.class)
public abstract class GrindstoneScreenHandlerMixin {
    @Shadow
    @Final
    Inventory input;

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getMaxDamage()I"))
    private int redirectGetMaxDamage(Item item) {
        return this.input.getStack(0).getMaxDamage();
    }

    @Inject(method = "transferEnchantments", at = @At("RETURN"))
    private void copyDurabilityMultiplier(ItemStack target, @NotNull ItemStack source, CallbackInfoReturnable<ItemStack> cir) {
        final NbtCompound tag = source.getNbt();
        if (tag!=null && tag.contains("DurabilityMultiplier")) {
            cir.getReturnValue().getOrCreateNbt().putFloat("DurabilityMultiplier", source.getOrCreateNbt().getFloat("DurabilityMultiplier"));
        }
    }
}
