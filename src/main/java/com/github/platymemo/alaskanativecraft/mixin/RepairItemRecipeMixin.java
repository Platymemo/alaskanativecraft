package com.github.platymemo.alaskanativecraft.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RepairItemRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(RepairItemRecipe.class)
public abstract class RepairItemRecipeMixin {

    @Unique
    private ItemStack anc$cachedStack;

    @Inject(method = "craft", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void cacheItemStack(CraftingInventory craftingInventory, CallbackInfoReturnable<ItemStack> cir, @NotNull List<ItemStack> list) {
        this.anc$cachedStack = list.get(0);
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getMaxDamage()I"))
    private int redirectGetMaxDamage(Item item) {
        return this.anc$cachedStack.getMaxDamage();
    }

    @Inject(method = "craft", at = @At("RETURN"))
    private void addDurabilityMultiplier(CraftingInventory craftingInventory, @NotNull CallbackInfoReturnable<ItemStack> cir) {
        if (!cir.getReturnValue().isEmpty() && anc$cachedStack.getOrCreateNbt().contains("DurabilityMultiplier")) {
            cir.getReturnValue().getOrCreateNbt().putFloat("DurabilityMultiplier", anc$cachedStack.getOrCreateNbt().getFloat("DurabilityMultiplier"));
        }
    }
}
