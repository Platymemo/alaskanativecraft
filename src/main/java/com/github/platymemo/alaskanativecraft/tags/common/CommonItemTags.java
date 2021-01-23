package com.github.platymemo.alaskanativecraft.tags.common;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CommonItemTags {
    public static final Tag<Item> LOGS_WITH_BARK = TagRegistry.item(new Identifier("c", "logs_with_bark"));
    public static final Tag<Item> CHESTS = TagRegistry.item(new Identifier("c", "chests"));
    public static final Tag<Item> VENISON = TagRegistry.item(new Identifier("c", "venison"));
    public static final Tag<Item> COOKED_VENISON = TagRegistry.item(new Identifier("c", "cooked_venison"));

    public static void register() {
    }
}
