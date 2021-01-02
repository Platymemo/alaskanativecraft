package com.github.platymemo.alaskanativecraft.tags.common;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CommonBlockTags {
    public static final Tag<Block> LOGS_WITH_BARK = TagRegistry.block(new Identifier("c", "logs_with_bark"));
    public static final Tag<Block> CHESTS = TagRegistry.block(new Identifier("c", "chests"));

    public static void register() { }
}
