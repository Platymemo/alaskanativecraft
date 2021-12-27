package com.github.platymemo.alaskanativecraft.tags.common;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CommonBlockTags {
    public static final Tag<Block> LOGS_WITH_BARK = TagFactory.BLOCK.create(new Identifier("c", "logs_with_bark"));

    public static void register() {
    }
}
