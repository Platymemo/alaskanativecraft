package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlaskaRecipes {
    public static SpecialRecipeSerializer<AkutaqRecipe> AKUTAQ = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(AlaskaNativeCraft.MOD_ID, "crafting_special_akutaq"), new SpecialRecipeSerializer<>(AkutaqRecipe::new));
    public static SpecialRecipeSerializer<FishStripRecipe> FISH_STRIP = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(AlaskaNativeCraft.MOD_ID, "crafting_special_fish_strip"), new SpecialRecipeSerializer<>(FishStripRecipe::new));

    public static void register() {

    }
}
