package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;

import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AlaskaRecipes {
	public static final SpecialRecipeSerializer<AkutaqRecipe> AKUTAQ = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(AlaskaNativeCraft.MOD_ID, "crafting_special_akutaq"), new SpecialRecipeSerializer<>(AkutaqRecipe::new));

	public static final RecipeType<DryingRecipe> DRYING = Registry.register(Registries.RECIPE_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "drying"), new RecipeType<DryingRecipe>() {
		public String toString() {
			return "drying";
		}
	});
	public static final CookingRecipeSerializer<DryingRecipe> DRYING_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(AlaskaNativeCraft.MOD_ID, "drying"), new CookingRecipeSerializer<>(DryingRecipe::new, 1200));

	public static void register() {
	}
}
