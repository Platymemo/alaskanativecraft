package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.loot.function.AlaskaLootFunctionTypes;
import com.github.platymemo.alaskanativecraft.recipe.AlaskaRecipes;
import com.github.platymemo.alaskanativecraft.sound.AlaskaSoundEvents;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import com.github.platymemo.alaskanativecraft.worldgen.feature.AlaskaFeatures;
import com.github.platymemo.alaskanativecraft.worldgen.structure.AlaskaStructures;
import net.fabricmc.api.ModInitializer;

public class AlaskaNativeCraft implements ModInitializer {
    public static final String MOD_ID = "alaskanativecraft";

    @Override
    public void onInitialize() {
        AlaskaBlocks.register();
        AlaskaItems.register();
        AlaskaLootFunctionTypes.register();
        AlaskaEntities.register();
        AlaskaFeatures.register();
        AlaskaStructures.register();
        AlaskaTags.register();
        AlaskaSoundEvents.register();
        AlaskaRecipes.register();
    }
}
