package com.github.platymemo.alaskanativecraft.item;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.BlockTags;

public class UluItem extends MiningToolItem {
	public UluItem(Settings settings) {
		super(1.0F, -2.8F, ToolMaterials.IRON, AlaskaTags.ULU_MINEABLE, settings);
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, @NotNull BlockState state) {
		if (!state.isOf(Blocks.COBWEB) && !state.isIn(BlockTags.LEAVES)) {
			return state.isIn(BlockTags.WOOL) ? 5.0F : super.getMiningSpeedMultiplier(stack, state);
		} else if (state.isOf(AlaskaBlocks.WHALE_MEAT_BLOCK)) {
			return 1.5F;
		} else {
			return 15.0F;
		}
	}
}
