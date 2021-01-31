package com.github.platymemo.alaskanativecraft.item;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class UluItem extends MiningToolItem {
    public UluItem(Settings settings) {
        super(1.0F, -2.8F, ToolMaterials.IRON, ImmutableSet.of(AlaskaBlocks.WHALE_MEAT_BLOCK), settings);
    }

    public boolean isEffectiveOn(BlockState state) {
        return state.isOf(Blocks.COBWEB) || state.isOf(Blocks.REDSTONE_WIRE) || state.isOf(Blocks.TRIPWIRE) || state.isOf(AlaskaBlocks.WHALE_MEAT_BLOCK);
    }

    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (!state.isOf(Blocks.COBWEB) && !state.isIn(BlockTags.LEAVES)) {
            return state.isIn(BlockTags.WOOL) ? 5.0F : super.getMiningSpeedMultiplier(stack, state);
        } else if (state.isOf(AlaskaBlocks.WHALE_MEAT_BLOCK)) {
            return 1.5F;
        } else {
            return 15.0F;
        }
    }
}
