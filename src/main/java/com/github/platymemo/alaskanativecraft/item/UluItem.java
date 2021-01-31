package com.github.platymemo.alaskanativecraft.item;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UluItem extends ShearsItem {
    public UluItem(Settings settings) {
        super(settings);
    }

    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        boolean shears = super.postMine(stack, world, state, pos, miner);
        return shears || state.isOf(AlaskaBlocks.WHALE_MEAT_BLOCK);
    }

    public boolean isEffectiveOn(BlockState state) {
        return super.isEffectiveOn(state) || state.isOf(AlaskaBlocks.WHALE_MEAT_BLOCK);
    }

    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return state.isOf(AlaskaBlocks.WHALE_MEAT_BLOCK) ? 1.5F : super.getMiningSpeedMultiplier(stack, state);
    }
}
