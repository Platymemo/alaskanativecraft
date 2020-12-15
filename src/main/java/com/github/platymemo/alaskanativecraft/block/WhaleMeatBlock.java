package com.github.platymemo.alaskanativecraft.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;

public class WhaleMeatBlock extends FacingBlock {

    public WhaleMeatBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)((BlockState)this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite()));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
