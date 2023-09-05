package com.github.platymemo.alaskanativecraft.tags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CommonTags {
	public static final TagKey<Block> LOGS_WITH_BARK = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "logs_with_bark"));
	public static final TagKey<Item> STICKS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "sticks"));

	public static void register() {
	}
}
