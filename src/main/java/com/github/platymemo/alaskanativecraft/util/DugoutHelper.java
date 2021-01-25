package com.github.platymemo.alaskanativecraft.util;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.LiteralText;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DugoutHelper {

    public static Direction.Axis getAxisOrNull(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isIn(BlockTags.LOGS_THAT_BURN) && state.get(PillarBlock.AXIS).isHorizontal()) {
            Direction.Axis axis = state.get(PillarBlock.AXIS);
            switch (axis) {
                case X:
                    if ((world.getBlockState(pos.east()).isIn(BlockTags.LOGS_THAT_BURN) && world.getBlockState(pos.east()).get(PillarBlock.AXIS) == axis)
                        && (world.getBlockState(pos.west()).isIn(BlockTags.LOGS_THAT_BURN) && world.getBlockState(pos.west()).get(PillarBlock.AXIS) == axis))
                        return axis;
                case Z:
                    if ((world.getBlockState(pos.north()).isIn(BlockTags.LOGS_THAT_BURN) && world.getBlockState(pos.north()).get(PillarBlock.AXIS) == axis)
                            && (world.getBlockState(pos.south()).isIn(BlockTags.LOGS_THAT_BURN) && world.getBlockState(pos.south()).get(PillarBlock.AXIS) == axis))
                        return axis;
                default:
                    return null;
            }
        }
        return null;
    }

    public static void createDugoutCanoe(Direction.Axis axis, World world, BlockPos pos) {
        if (!world.isClient) {
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            if (axis == Direction.Axis.X) {
                world.setBlockState(pos.east(), Blocks.AIR.getDefaultState());
                world.setBlockState(pos.west(), Blocks.AIR.getDefaultState());

                // TODO
                // Spawn the dugout canoe

            } else if (axis == Direction.Axis.Z) {
                world.setBlockState(pos.north(), Blocks.AIR.getDefaultState());
                world.setBlockState(pos.south(), Blocks.AIR.getDefaultState());

                // TODO
                // Spawn the dugout canoe

            }
        }
    }
}
