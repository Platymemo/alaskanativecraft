package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.feature.AlaskaFeatures;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.recipe.AlaskaRecipes;
import com.github.platymemo.alaskanativecraft.sound.AlaskaSoundEvents;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;

public class AlaskaNativeCraft implements ModInitializer {

    public static final String MOD_ID = "alaskanativecraft";

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        AlaskaBlocks.register();
        AlaskaItems.register();
        AlaskaEntities.register();
        AlaskaFeatures.register();
        AlaskaTags.register();
        AlaskaSoundEvents.register();
        AlaskaRecipes.register();

        addSnowGogglesToLootTable();
    }

    private static void addSnowGogglesToLootTable() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (LootTables.VILLAGE_SNOWY_HOUSE_CHEST.equals(id) || LootTables.VILLAGE_TAIGA_HOUSE_CHEST.equals(id)) {
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(UniformLootTableRange.between(0.0F, 1.0F))
                        .withEntry(ItemEntry.builder(AlaskaItems.SNOW_GOGGLES).build());

                supplier.pool(poolBuilder);
            }
        });
    }
}
