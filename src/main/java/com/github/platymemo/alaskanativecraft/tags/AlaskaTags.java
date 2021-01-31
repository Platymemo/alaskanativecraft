package com.github.platymemo.alaskanativecraft.tags;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.tags.common.CommonBlockTags;
import com.github.platymemo.alaskanativecraft.tags.common.CommonItemTags;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AlaskaTags {
    public static final Tag<Item> ULUS = TagRegistry.item(new Identifier(AlaskaNativeCraft.MOD_ID, "ulus"));
    public static final Tag<Item> HARPOONS = TagRegistry.item(new Identifier(AlaskaNativeCraft.MOD_ID, "harpoons"));
    public static final Tag<Item> DOGSLEDS = TagRegistry.item(new Identifier(AlaskaNativeCraft.MOD_ID, "dogsleds"));
    public static final Tag<Item> KUSPUKS = TagRegistry.item(new Identifier(AlaskaNativeCraft.MOD_ID, "kuspuks"));
    public static final Tag<Item> AKUTAQ_MEATS = TagRegistry.item(new Identifier(AlaskaNativeCraft.MOD_ID, "akutaq_meats"));
    public static final Tag<Item> BERRIES = TagRegistry.item(new Identifier(AlaskaNativeCraft.MOD_ID, "berries"));

    public static void register() {
        CommonBlockTags.register();
        CommonItemTags.register();
    }
}
