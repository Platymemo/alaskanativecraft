package com.github.platymemo.alaskanativecraft.tags.common;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CommonBlockTags {
    public static final TagKey<Block> LOGS_WITH_BARK = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "logs_with_bark"));

    public static void register() {
    }
}
