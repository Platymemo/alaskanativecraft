package com.github.platymemo.alaskanativecraft.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class DriftwoodBlock extends PillarBlock {
    public DriftwoodBlock(AbstractBlock.Settings settings){
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        pos = pos.down();
        BlockState targetState = world.getBlockState(pos);
        return targetState.isIn(BlockTags.SAND) || targetState.isIn(BlockTags.ICE);
    }
}
