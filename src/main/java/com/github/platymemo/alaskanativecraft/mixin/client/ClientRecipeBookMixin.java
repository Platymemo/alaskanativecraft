package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.recipe.AlaskaRecipes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;

@Environment(EnvType.CLIENT)
@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
	@Inject(method = "getGroupForRecipe(Lnet/minecraft/recipe/Recipe;)Lnet/minecraft/client/recipebook/RecipeBookGroup;", at = @At("HEAD"), cancellable = true)
	private static void removeDryingRecipe(@NotNull Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
		if (recipe.getType() == AlaskaRecipes.DRYING) {
			cir.setReturnValue(RecipeBookGroup.UNKNOWN);
		}
	}
}
