package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.loot.function.AlaskaLootFunctionTypes;
import com.github.platymemo.alaskanativecraft.recipe.AlaskaRecipes;
import com.github.platymemo.alaskanativecraft.sound.AlaskaSoundEvents;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import com.github.platymemo.alaskanativecraft.worldgen.feature.AlaskaFeatures;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class AlaskaNativeCraft implements ModInitializer {
    public static final String MOD_ID = "alaskanativecraft";

    @Override
    public void onInitialize(ModContainer container) {

        AlaskaBlocks.register();
        AlaskaItems.register();
        AlaskaLootFunctionTypes.register();
        AlaskaEntities.register();
        AlaskaTags.register();
        AlaskaFeatures.register();
        AlaskaSoundEvents.register();
        AlaskaRecipes.register();
    }
}
