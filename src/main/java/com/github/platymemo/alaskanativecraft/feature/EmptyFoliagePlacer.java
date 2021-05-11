package com.github.platymemo.alaskanativecraft.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

public class EmptyFoliagePlacer extends FoliagePlacer {
    public static final Codec<EmptyFoliagePlacer> CODEC = Codec.unit(new EmptyFoliagePlacer());

    protected FoliagePlacerType<EmptyFoliagePlacer> getType() {
        return AlaskaFeatures.EMPTY_FOLIAGE;
    }

    public EmptyFoliagePlacer() {
        super(UniformIntDistribution.of(0), UniformIntDistribution.of(0));
    }

    @Override
    protected void generate(ModifiableTestableWorld world, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, Set<BlockPos> leaves, int offset, BlockBox box) {

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
