package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Collection;

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

    /**
     * Don't question it :ioa:
     * Used to allow driftwood, antler, and ivory to be used in replace of sticks in recipes.
     * Not converting to a tag because theres other trickery to be done later on. // TODO hopefully
     */
    public static class AlaskaSticksEntry implements Ingredient.Entry {
        @Override
        public Collection<ItemStack> getStacks() {
            return Arrays.asList(
                    Items.STICK.getDefaultStack(),
                    AlaskaItems.DRIFTWOOD_CHUNK.getDefaultStack(),
                    AlaskaItems.ANTLER.getDefaultStack(),
                    AlaskaItems.IVORY.getDefaultStack()
            );
        }

        @Override
        public JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", Registry.ITEM.getId(Items.STICK).toString());
            return jsonObject;
        }
    }
}
