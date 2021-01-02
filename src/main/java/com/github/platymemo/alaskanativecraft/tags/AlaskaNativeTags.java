package com.github.platymemo.alaskanativecraft.tags;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.tags.common.CommonBlockTags;
import com.github.platymemo.alaskanativecraft.tags.common.CommonItemTags;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AlaskaNativeTags {
    public static final Tag<Item> HARPOONS = TagRegistry.item(new Identifier(AlaskaNativeCraft.MOD_ID, "harpoons"));

    public static void register() {
        CommonBlockTags.register();
        CommonItemTags.register();
    }
}
