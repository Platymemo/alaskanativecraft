package com.github.platymemo.alaskanativecraft.feature;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
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
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

public class AlaskaFeatures {

    public static void register() {
        registerBerryPatche(AlaskaBlocks.BLUEBERRY_BUSH, "blueberry_bush");
        registerBerryPatche(AlaskaBlocks.CLOUDBERRY_BUSH, "cloudberry_bush");
        registerBerryPatche(AlaskaBlocks.RASPBERRY_BUSH, "raspberry_bush");
        registerBerryPatche(AlaskaBlocks.SALMONBERRY_BUSH, "salmonberry_bush");
    }

    private static void registerBerryPatche(Block berryBush, String bushName) {
        ConfiguredFeature<?, ?> patch_bush = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(berryBush.getDefaultState().with(SweetBerryBushBlock.AGE, 3)), SimpleBlockPlacer.INSTANCE)).tries(64).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build());

        ConfiguredFeature<?, ?> patch_bush_sparse = patch_bush.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE);
        ConfiguredFeature<?, ?> patch_bush_decorated = patch_bush.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(12);

        RegistryKey<ConfiguredFeature<?, ?>> sparseBushPatchRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + bushName + "_sparse"));
        RegistryKey<ConfiguredFeature<?, ?>> decoratedBushPatchRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + bushName + "_decorated"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, sparseBushPatchRegistryKey.getValue(), patch_bush_sparse);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, decoratedBushPatchRegistryKey.getValue(), patch_bush_decorated);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.TAIGA).and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.RAIN), GenerationStep.Feature.VEGETAL_DECORATION, sparseBushPatchRegistryKey);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.TAIGA).and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.SNOW), GenerationStep.Feature.VEGETAL_DECORATION, decoratedBushPatchRegistryKey);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.ICY), GenerationStep.Feature.VEGETAL_DECORATION, decoratedBushPatchRegistryKey);
    }
}
