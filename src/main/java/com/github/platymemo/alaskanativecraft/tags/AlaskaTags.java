package com.github.platymemo.alaskanativecraft.tags;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.tags.common.CommonBlockTags;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlaskaTags {
    public static final TagKey<Item> ULUS = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "ulus"));
    public static final TagKey<Item> KUSPUKS = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "kuspuks"));
    public static final TagKey<Item> HARPOONS = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "harpoons"));
    public static final TagKey<Item> AKUTAQ_MEATS = TagFactory.ITEM.create(new Identifier(AlaskaNativeCraft.MOD_ID, "akutaq_meats"));
    public static final TagKey<Item> AKUTAQ_BERRIES = registerItem("akutaq_berries");
    public static final TagKey<Item> SLICEABLE_FISH = registerItem("sliceable_fish");
    public static final TagKey<Item> SEAL_FOOD = registerItem("seal_food");

    public static final TagKey<Block> SNOWSHOE_SPEED_BLOCKS = TagFactory.BLOCK.create(new Identifier(AlaskaNativeCraft.MOD_ID, "snowshoe_speed_blocks"));
    public static final TagKey<Block> ULU_MINEABLE = TagFactory.BLOCK.create(new Identifier(AlaskaNativeCraft.MOD_ID, "mineable/ulu"));

    private static TagKey<EntityType<?>> registerEntity(String id) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(id));
    }

    private static TagKey<Item> registerItem(String id) {
        return TagKey.of(Registry.ITEM_KEY, new Identifier(id));
    }

    private static TagKey<Block> registerBlock(String id) {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier(id));
    }

    public static void register() {
        CommonBlockTags.register();
    }
}
