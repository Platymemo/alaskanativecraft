package com.github.platymemo.alaskanativecraft.tags;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.tags.common.CommonBlockTags;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlaskaTags {
    public static final TagKey<Item> ULUS = TagKey.of(Registry.ITEM_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "ulus"));
    public static final TagKey<Item> KUSPUKS = TagKey.of(Registry.ITEM_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "kuspuks"));
    public static final TagKey<Item> HARPOONS = TagKey.of(Registry.ITEM_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "harpoons"));
    public static final TagKey<Item> AKUTAQ_MEATS = TagKey.of(Registry.ITEM_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "akutaq_meats"));
    public static final TagKey<Item> AKUTAQ_BERRIES = TagKey.of(Registry.ITEM_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "akutaq_berries"));
    public static final TagKey<Item> SLICEABLE_FISH = TagKey.of(Registry.ITEM_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "sliceable_fish"));
    public static final TagKey<Item> SEAL_FOOD = TagKey.of(Registry.ITEM_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "seal_food"));

    public static final TagKey<Block> SNOWSHOE_SPEED_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "snowshoe_speed_blocks"));
    public static final TagKey<Block> ULU_MINEABLE = TagKey.of(Registry.BLOCK_KEY, new Identifier(AlaskaNativeCraft.MOD_ID, "mineable/ulu"));

    public static void register() {
        CommonBlockTags.register();
    }
}
