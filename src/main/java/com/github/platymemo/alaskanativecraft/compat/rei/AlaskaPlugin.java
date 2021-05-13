package com.github.platymemo.alaskanativecraft.compat.rei;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.recipe.DryingRecipe;
import me.shedaniel.rei.api.BuiltinPlugin;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class AlaskaPlugin implements REIPluginV0 {
    public static final Identifier ALASKA_PLUGIN = new Identifier(AlaskaNativeCraft.MOD_ID, "plugin");
    public static final Identifier DRYING_ID = new Identifier(AlaskaNativeCraft.MOD_ID, "plugins/drying");

    @Override
    public Identifier getPluginIdentifier() {
        return ALASKA_PLUGIN;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new DryingCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(DRYING_ID, DryingRecipe.class, DryingDisplay::new);
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(DRYING_ID, EntryStack.create(AlaskaBlocks.DRYING_RACK));
        // You shouldn't be able to auto-craft this due to it requiring direct interaction with the block.
        recipeHelper.removeAutoCraftButton(DRYING_ID);
        // Add info for entries
        BuiltinPlugin.getInstance().registerInformation(
                EntryStack.create(AlaskaItems.AKUTAQ),
                new TranslatableText("item.alaskanativecraft.akutaq"),
                (list) -> {
                    list.add(new TranslatableText("alaskanativecraft.akutaq.line1"));
                    list.add(new TranslatableText("alaskanativecraft.akutaq.line2"));
                    return list;
                });
        BuiltinPlugin.getInstance().registerInformation(
                EntryStack.create(AlaskaItems.FISH_STRIP),
                new TranslatableText("item.alaskanativecraft.fish_strip"),
                (list) -> {
                    list.add(new TranslatableText("alaskanativecraft.fish_strip.line1"));
                    list.add(new TranslatableText("alaskanativecraft.fish_strip.line2"));
                    return list;
                });
    }
}
