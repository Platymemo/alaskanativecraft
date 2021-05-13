package com.github.platymemo.alaskanativecraft.compat.rei;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.recipe.DryingRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.util.Identifier;

public class DryingPlugin  implements REIPluginV0 {
    public static final Identifier DRYING_PLUGIN = new Identifier(AlaskaNativeCraft.MOD_ID, "plugins/drying");

    @Override
    public Identifier getPluginIdentifier() {
        return DRYING_PLUGIN;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new DryingCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(DRYING_PLUGIN, DryingRecipe.class, DryingDisplay::new);
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(DRYING_PLUGIN, EntryStack.create(AlaskaBlocks.DRYING_RACK));
    }
}
