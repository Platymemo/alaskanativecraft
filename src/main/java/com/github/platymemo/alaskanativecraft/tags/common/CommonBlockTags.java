package com.github.platymemo.alaskanativecraft.tags.common;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CommonBlockTags {
    public static final TagKey<Block> LOGS_WITH_BARK = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "logs_with_bark"));

    public static void register() {
    }
}
