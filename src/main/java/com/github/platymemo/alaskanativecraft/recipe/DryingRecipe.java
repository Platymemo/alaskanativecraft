package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class DryingRecipe extends AbstractCookingRecipe {
    public DryingRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(AlaskaRecipes.DRYING, id, group, input, output, experience, cookTime);
    }

    @Environment(EnvType.CLIENT)
    public ItemStack createIcon() {
        return new ItemStack(AlaskaBlocks.DRYING_RACK);
    }

    public RecipeSerializer<?> getSerializer() {
        return AlaskaRecipes.DRYING_SERIALIZER;
    }
}
