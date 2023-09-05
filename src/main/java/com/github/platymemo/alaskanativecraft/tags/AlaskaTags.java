package com.github.platymemo.alaskanativecraft.tags;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class AlaskaTags {
	public static final TagKey<Item> ULUS = TagKey.of(RegistryKeys.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, "ulus"));
	public static final TagKey<Item> KUSPUKS = TagKey.of(RegistryKeys.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, "kuspuks"));
	public static final TagKey<Item> HARPOONS = TagKey.of(RegistryKeys.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, "harpoons"));
	public static final TagKey<Item> AKUTAQ_MEATS = TagKey.of(RegistryKeys.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, "akutaq_meats"));
	public static final TagKey<Item> AKUTAQ_BERRIES = TagKey.of(RegistryKeys.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, "akutaq_berries"));
	public static final TagKey<Item> SLICEABLE_FISH = TagKey.of(RegistryKeys.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, "sliceable_fish"));
	public static final TagKey<Item> SEAL_FOOD = TagKey.of(RegistryKeys.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, "seal_food"));

	public static final TagKey<Block> SNOWSHOE_SPEED_BLOCKS = TagKey.of(RegistryKeys.BLOCK, new Identifier(AlaskaNativeCraft.MOD_ID, "snowshoe_speed_blocks"));
	public static final TagKey<Block> ULU_MINEABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier(AlaskaNativeCraft.MOD_ID, "mineable/ulu"));

	public static final TagKey<Biome> HAS_DRIFTWOOD = TagKey.of(RegistryKeys.BIOME, new Identifier(AlaskaNativeCraft.MOD_ID, "has_feature/driftwood"));
	public static final TagKey<Biome> HAS_BUSHES = TagKey.of(RegistryKeys.BIOME, new Identifier(AlaskaNativeCraft.MOD_ID, "has_feature/berry_bushes"));
	public static final TagKey<Biome> HAS_LABRADOR_TEA = TagKey.of(RegistryKeys.BIOME, new Identifier(AlaskaNativeCraft.MOD_ID, "has_feature/labrador_tea"));
	public static final TagKey<Biome> HAS_SEAL = TagKey.of(RegistryKeys.BIOME, new Identifier(AlaskaNativeCraft.MOD_ID, "has_mob/seal"));
	public static final TagKey<Biome> HAS_MOOSE = TagKey.of(RegistryKeys.BIOME, new Identifier(AlaskaNativeCraft.MOD_ID, "has_mob/moose"));
	public static final TagKey<Biome> HAS_PTARMIGAN = TagKey.of(RegistryKeys.BIOME, new Identifier(AlaskaNativeCraft.MOD_ID, "has_mob/ptarmigan"));

	public static void register() {
		CommonTags.register();
	}
}
