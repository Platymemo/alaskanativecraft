package com.github.platymemo.alaskanativecraft.compat.rei;

import com.github.platymemo.alaskanativecraft.recipe.DryingRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class DryingDisplay implements RecipeDisplay {
    private List<List<EntryStack>> inputs;
    private List<EntryStack> output;
    private int dryTime;
    private DryingRecipe display;

    public DryingDisplay(DryingRecipe recipe) {
        this(recipe.getPreviewInputs(), recipe.getOutput(), recipe.getCookTime());
        this.display = recipe;
    }

    public DryingDisplay(DefaultedList<Ingredient> ingredients, ItemStack output, int dryTime) {
        this.inputs = EntryStack.ofIngredients(ingredients);
        this.output = Collections.singletonList(EntryStack.create(output));
        this.dryTime = dryTime;
    }

    public double getDryTime() {
        return dryTime;
    }

    @Override
    public @NotNull Optional<Identifier> getRecipeLocation() {
        return Optional.ofNullable(display).map(DryingRecipe::getId);
    }

    @Override
    public @NotNull List<List<EntryStack>> getInputEntries() {
        return inputs;
    }

    @Override
    public @NotNull List<List<EntryStack>> getResultingEntries() {
        return Collections.singletonList(output);
    }

    @Override
    public @NotNull List<List<EntryStack>> getRequiredEntries() {
        return inputs;
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return AlaskaPlugin.DRYING_ID;
    }
}
