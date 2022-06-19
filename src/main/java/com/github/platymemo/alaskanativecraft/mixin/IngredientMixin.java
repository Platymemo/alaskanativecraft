package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Ingredient.class)
public class IngredientMixin {

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/recipe/ShapedRecipe;getItem(Lcom/google/gson/JsonObject;)Lnet/minecraft/item/Item;"),
            method = "entryFromJson", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void addDriftwoodAntlersAndIvory(JsonObject json, CallbackInfoReturnable<Ingredient.Entry> cir, Item item) {
        if (item == Items.STICK) {
            cir.setReturnValue(new Ingredient.TagEntry(AlaskaTags.STICKS));
        }
    }
}
