package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingCategory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

import org.quiltmc.loader.api.minecraft.ClientOnly;

public class DryingRecipe extends AbstractCookingRecipe {
	public DryingRecipe(Identifier id, String group, CookingCategory category, Ingredient input, ItemStack output, float experience, int cookTime) {
		super(AlaskaRecipes.DRYING, id, group, category, input, output, experience, cookTime);
	}

	@Override
	@ClientOnly
	public ItemStack createIcon() {
		return new ItemStack(AlaskaBlocks.DRYING_RACK);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AlaskaRecipes.DRYING_SERIALIZER;
	}
}
