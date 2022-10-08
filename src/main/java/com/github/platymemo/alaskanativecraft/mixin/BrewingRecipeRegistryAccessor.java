package com.github.platymemo.alaskanativecraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;

@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryAccessor {
	@Invoker("registerPotionRecipe")
	static void registerRecipe(Potion input, Item item, Potion output) {
		throw new AssertionError("Something is very wrong!! Dummy method in BrewingRecipeRegistryAccessor called!");
	}
}
