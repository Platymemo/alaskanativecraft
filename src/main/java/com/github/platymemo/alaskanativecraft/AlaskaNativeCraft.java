package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.block.AlaskaNativeBlocks;
import com.github.platymemo.alaskanativecraft.entity.AlaskaNativeEntities;
import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import com.github.platymemo.alaskanativecraft.sound.AlaskaNativeSoundEvents;
import com.github.platymemo.alaskanativecraft.tags.AlaskaNativeTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class AlaskaNativeCraft implements ModInitializer {

    public static final String MOD_ID = "alaskanativecraft";

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        AlaskaNativeItems.register();
        AlaskaNativeBlocks.register();
        AlaskaNativeEntities.register();
        AlaskaNativeTags.register();
        AlaskaNativeSoundEvents.register();

        FabricItemGroupBuilder.create(new Identifier(MOD_ID, "items")).icon(() -> AlaskaNativeItems.MUKTUK.asItem().getDefaultStack()).appendItems(stacks -> Registry.ITEM.forEach(item -> {
            if (Registry.ITEM.getId(item).getNamespace().equals(MOD_ID)) {
                item.appendStacks(item.getGroup(), (DefaultedList<ItemStack>) stacks);
            }
        })).build();
    }
}
