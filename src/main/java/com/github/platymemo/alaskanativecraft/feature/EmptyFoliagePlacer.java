package com.github.platymemo.alaskanativecraft.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class EmptyFoliagePlacer extends FoliagePlacer {
    public static final Codec<EmptyFoliagePlacer> CODEC = Codec.unit(new EmptyFoliagePlacer());

    protected FoliagePlacerType<EmptyFoliagePlacer> getType() {
        return AlaskaFeatures.EMPTY_FOLIAGE;
    }

    public EmptyFoliagePlacer() {
        super(UniformIntProvider.create(0, 0), UniformIntProvider.create(0, 0));
    }

    @Override
    protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {

    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 0;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int baseHeight, int dx, int y, int dz, boolean giantTrunk) {
        return false;
    }
}
