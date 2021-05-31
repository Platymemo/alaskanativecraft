package com.github.platymemo.alaskanativecraft.feature;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.mixin.FoliagePlacerTypeInvoker;
import com.github.platymemo.alaskanativecraft.mixin.TrunkPlacerTypeInvoker;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class AlaskaFeatures {

    public static final FoliagePlacerType<EmptyFoliagePlacer> EMPTY_FOLIAGE = FoliagePlacerTypeInvoker.callRegister(new Identifier(AlaskaNativeCraft.MOD_ID, "empty").toString(), EmptyFoliagePlacer.CODEC);
    public static final TrunkPlacerType<FallenTrunkPlacer> FALLEN_TRUNK = TrunkPlacerTypeInvoker.callRegister(new Identifier(AlaskaNativeCraft.MOD_ID, "fallen").toString(), FallenTrunkPlacer.CODEC);

    public static void register() {
        registerBerryPatch(AlaskaBlocks.BLUEBERRY_BUSH, "blueberry_bush");
        registerBerryPatch(AlaskaBlocks.CLOUDBERRY_BUSH, "cloudberry_bush");
        registerBerryPatch(AlaskaBlocks.RASPBERRY_BUSH, "raspberry_bush");
        registerBerryPatch(AlaskaBlocks.SALMONBERRY_BUSH, "salmonberry_bush");
        registerWashedUpTree(AlaskaBlocks.DRIFTWOOD_LOG, "washed_up_driftwood");
    }

    private static void registerBerryPatch(Block berryBush, String bushName) {
        ConfiguredFeature<?, ?> patchBush = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(berryBush.getDefaultState().with(SweetBerryBushBlock.AGE, 3)), SimpleBlockPlacer.INSTANCE)).tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build());

        // Sparse bushes
        ConfiguredFeature<?, ?> patchBushSparse = patchBush.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE);
        RegistryKey<ConfiguredFeature<?, ?>> sparseBushPatchRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + bushName + "_sparse"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, sparseBushPatchRegistryKey.getValue(), patchBushSparse);

        // Decorated (extra sparse) bushes
        ConfiguredFeature<?, ?> patchBushDecorated = patchBush.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(12);
        RegistryKey<ConfiguredFeature<?, ?>> decoratedBushPatchRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + bushName + "_decorated"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, decoratedBushPatchRegistryKey.getValue(), patchBushDecorated);

        // Add the features to actually spawn
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.TAIGA).and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.RAIN), GenerationStep.Feature.VEGETAL_DECORATION, sparseBushPatchRegistryKey);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.TAIGA).and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.SNOW), GenerationStep.Feature.VEGETAL_DECORATION, decoratedBushPatchRegistryKey);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.ICY), GenerationStep.Feature.VEGETAL_DECORATION, sparseBushPatchRegistryKey);
    }

    private static void registerWashedUpTree(Block treeLog, String treeName) {
        TreeFeatureConfig washedUpTree = new TreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(treeLog.getDefaultState()),
                new FallenTrunkPlacer(2, 1, 1),
                // For the memes :tnypto:
                new SimpleBlockStateProvider(Blocks.NETHERITE_BLOCK.getDefaultState()),
                new SimpleBlockStateProvider(Blocks.NETHERITE_BLOCK.getDefaultState()),
                new EmptyFoliagePlacer(),
                new TwoLayersFeatureSize(0, 0, 0)
        ).build();

        ConfiguredFeature<TreeFeatureConfig, ?> configuredWashedUpTree = Feature.TREE.configure(washedUpTree);

        // Sparse washed up trees
        ConfiguredFeature<?, ?> patchWashedUpTreeSparse = configuredWashedUpTree.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP);
        RegistryKey<ConfiguredFeature<?, ?>> sparseWashedUpTreePatchRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + treeName + "_sparse"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, sparseWashedUpTreePatchRegistryKey.getValue(), patchWashedUpTreeSparse);

        // Decorated (extra sparse) washed up trees
        ConfiguredFeature<?, ?> patchWashedUpTreeDecorated = configuredWashedUpTree.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).repeatRandomly(4);
        RegistryKey<ConfiguredFeature<?, ?>> decoratedWashedUpTreePatchRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + treeName + "_decorated"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, decoratedWashedUpTreePatchRegistryKey.getValue(), patchWashedUpTreeDecorated);

        // Add the features to actually spawn
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.BEACH)
                    .and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.RAIN)
                    .and(ctx -> AlaskaConfig.getConfig().genDriftwood),
                GenerationStep.Feature.VEGETAL_DECORATION,
                decoratedWashedUpTreePatchRegistryKey);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.BEACH)
                        .and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.SNOW)
                        .and(ctx -> AlaskaConfig.getConfig().genDriftwood),
                GenerationStep.Feature.VEGETAL_DECORATION,
                sparseWashedUpTreePatchRegistryKey);
    }
}
