package com.github.platymemo.alaskanativecraft.mixin;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RepairItemRecipe;

@Mixin(RepairItemRecipe.class)
public abstract class RepairItemRecipeMixin {
	@Unique
	private ItemStack anc$cachedStack;

	@Inject(method = "craft(Lnet/minecraft/inventory/CraftingInventory;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void cacheItemStack(CraftingInventory craftingInventory, CallbackInfoReturnable<ItemStack> cir, @NotNull List<ItemStack> list) {
		this.anc$cachedStack = list.get(0);
	}

	@Redirect(method = "craft(Lnet/minecraft/inventory/CraftingInventory;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getMaxDamage()I"))
	private int redirectGetMaxDamage(Item item) {
		return this.anc$cachedStack.getMaxDamage();
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "craft(Lnet/minecraft/inventory/CraftingInventory;)Lnet/minecraft/item/ItemStack;", at = @At("RETURN"))
	private void addDurabilityMultiplier(CraftingInventory craftingInventory, @NotNull CallbackInfoReturnable<ItemStack> cir) {
		if (!cir.getReturnValue().isEmpty() && this.anc$cachedStack.hasNbt() && this.anc$cachedStack.getNbt().contains("DurabilityMultiplier")) {
			cir.getReturnValue().getOrCreateNbt().putFloat("DurabilityMultiplier", this.anc$cachedStack.getNbt().getFloat("DurabilityMultiplier"));
		}
	}
}
