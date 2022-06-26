package com.github.platymemo.alaskanativecraft.compat.rei;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.recipe.DryingRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class AlaskaPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<DryingDisplay> DRYING_ID = CategoryIdentifier.of(AlaskaNativeCraft.MOD_ID, "plugins/drying");

    @Override
    public void registerCategories(@NotNull CategoryRegistry registry) {
        registry.add(new DryingCategory(), config -> config.addWorkstations(EntryStacks.of(AlaskaBlocks.DRYING_RACK)));
    }

    @Override
    public void registerDisplays(@NotNull DisplayRegistry registry) {
        registry.registerFiller(DryingRecipe.class, DryingDisplay::new);
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(AlaskaItems.AKUTAQ),
                Text.translatable("item.alaskanativecraft.akutaq"),
                (list) -> {
                    list.add(Text.translatable("alaskanativecraft.akutaq.line1"));
                    list.add(Text.translatable("alaskanativecraft.akutaq.line2"));
                    list.add(Text.translatable("alaskanativecraft.akutaq.line3"));
                    return list;
                });
        BuiltinClientPlugin.getInstance().registerInformation(
                EntryStacks.of(AlaskaItems.FISH_STRIP),
                Text.translatable("item.alaskanativecraft.fish_strip"),
                (list) -> {
                    list.add(Text.translatable("alaskanativecraft.fish_strip.line1"));
                    list.add(Text.translatable("alaskanativecraft.fish_strip.line2"));
                    return list;
                });
    }
}
