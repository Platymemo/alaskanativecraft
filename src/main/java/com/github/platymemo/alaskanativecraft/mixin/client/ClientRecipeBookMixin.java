package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.recipe.AlaskaRecipes;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.recipe_book.ClientRecipeBook;
import net.minecraft.client.recipe_book.RecipeBookGroup;
import net.minecraft.recipe.Recipe;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void removeDryingRecipe(@NotNull Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.getType() == AlaskaRecipes.DRYING) {
            cir.setReturnValue(RecipeBookGroup.UNKNOWN);
        }
    }
}
