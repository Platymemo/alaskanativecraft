package com.github.platymemo.alaskanativecraft.mixin;

import java.util.Set;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
	@Shadow
	@Final
	ItemStack result;

	@Inject(at = @At("RETURN"), method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;", cancellable = true)
	private void addDurability(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager, CallbackInfoReturnable<ItemStack> cir) {
		if (recipeInputInventory.containsAny(Set.of(AlaskaItems.DRIFTWOOD_CHUNK, AlaskaItems.ANTLER, AlaskaItems.IVORY)) && this.result.getItem().isDamageable()) {
			ItemStack protectedItem = this.result.copy();
			NbtCompound tag = protectedItem.getOrCreateNbt();
			float durabilityMultiplier = 1;
			for (int i = 0; i < recipeInputInventory.size(); ++i) {
				ItemStack stack = recipeInputInventory.getStack(i);
				if (stack.isOf(AlaskaItems.DRIFTWOOD_CHUNK)) {
					durabilityMultiplier *= 1.05f;
				} else if (stack.isOf(AlaskaItems.ANTLER)) {
					durabilityMultiplier *= 1.2f;
				} else if (stack.isOf(AlaskaItems.IVORY)) {
					durabilityMultiplier *= 1.4f;
				}
			}

			tag.putFloat("DurabilityMultiplier", durabilityMultiplier);
			cir.setReturnValue(protectedItem);
		}
	}
}
