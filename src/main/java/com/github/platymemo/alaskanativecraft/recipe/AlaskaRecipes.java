package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlaskaRecipes {
    public static final SpecialRecipeSerializer<AkutaqRecipe> AKUTAQ = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(AlaskaNativeCraft.MOD_ID, "crafting_special_akutaq"), new SpecialRecipeSerializer<>(AkutaqRecipe::new));
    public static final SpecialRecipeSerializer<FishStripRecipe> FISH_STRIP = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(AlaskaNativeCraft.MOD_ID, "crafting_special_fish_strip"), new SpecialRecipeSerializer<>(FishStripRecipe::new));

    public static final RecipeType<DryingRecipe> DRYING = Registry.register(Registry.RECIPE_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "drying"), new RecipeType<DryingRecipe>() {
        public String toString() {
            return "drying";
        }
    });
    public static final CookingRecipeSerializer<DryingRecipe> DRYING_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(AlaskaNativeCraft.MOD_ID, "drying"), new CookingRecipeSerializer<>(DryingRecipe::new, 1200));

    public static void register() {

    }
}
