package com.github.platymemo.alaskanativecraft.tags;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.tags.common.CommonBlockTags;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AlaskaTags {
    public static final Tag<Item> ULUS = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "ulus"));
    public static final Tag<Item> KUSPUKS = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "kuspuks"));
    public static final Tag<Item> HARPOONS = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "harpoons"));
    public static final Tag<Item> AKUTAQ_MEATS = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "akutaq_meats"));
    public static final Tag<Item> AKUTAQ_BERRIES = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "akutaq_berries"));
    public static final Tag<Item> SLICEABLE_FISH = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "sliceable_fish"));
    public static final Tag<Item> SEAL_FOOD = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "seal_food"));

    public static final Tag<Block> SNOWSHOE_SPEED_BLOCKS = TagFactory.BLOCK.create(new Identifier(AlaskaNativeCraft.MOD_ID, "snowshoe_speed_blocks"));
    public static final Tag<Block> ULU_MINEABLE = TagFactory.BLOCK.create(new Identifier(AlaskaNativeCraft.MOD_ID, "mineable/ulu"));

    public static void register() {
        CommonBlockTags.register();
    }
}
