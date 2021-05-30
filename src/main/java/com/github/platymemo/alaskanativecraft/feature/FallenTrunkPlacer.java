package com.github.platymemo.alaskanativecraft.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

public class FallenTrunkPlacer extends StraightTrunkPlacer {
    public static final Codec<FallenTrunkPlacer> CODEC = RecordCodecBuilder.create(
            (instance) -> fillTrunkPlacerFields(instance).apply(instance, FallenTrunkPlacer::new)
    );

    public FallenTrunkPlacer(int i, int j, int k) {
        super(i, j, k);
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return AlaskaFeatures.FALLEN_TRUNK;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int trunkHeight, BlockPos pos, TreeFeatureConfig config) {
        Direction direction = Direction.fromHorizontal(random.nextInt(4));

        for (int i = 0; i < trunkHeight; ++i) {
            if (!getAndSetSpecificState(world, replacer, random, pos.offset(direction, i), config, direction.getAxis())) {
                return ImmutableList.of();
            }
        }

        return ImmutableList.of();
    }

    protected static boolean getAndSetSpecificState(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, BlockPos pos, TreeFeatureConfig config, Direction.Axis axis) {
        if (TreeFeature.canReplace(world, pos) && !world.testBlockState(pos.down(), BlockState::isAir)) {
            getAndSetState(world, replacer, random, pos, config,
                    (blockState) -> config.trunkProvider.getBlockState(random, pos).with(PillarBlock.AXIS, axis));
            return true;
        } else {
            return false;
        }
    }
}