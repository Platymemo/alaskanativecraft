package com.github.platymemo.alaskanativecraft.feature;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

import java.util.Set;

public class AlaskaFeatures {
    public static void register() {
        registerBerryPatch(AlaskaBlocks.BLUEBERRY_BUSH, "blueberry_bush");
        registerBerryPatch(AlaskaBlocks.CLOUDBERRY_BUSH, "cloudberry_bush");
        registerBerryPatch(AlaskaBlocks.RASPBERRY_BUSH, "raspberry_bush");
        registerBerryPatch(AlaskaBlocks.SALMONBERRY_BUSH, "salmonberry_bush");
        if (AlaskaConfig.getConfig().genDriftwood) {
            registerPatch(AlaskaBlocks.DRIFTWOOD_LOG.getDefaultState(), "washed_up_driftwood", Biome.Category.BEACH, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.CLAY, Blocks.SAND, Blocks.RED_SAND);
        }
    }

    private static void registerBerryPatch(Block berryBush, String bushName) {
        registerPatch(
                berryBush.getDefaultState().with(SweetBerryBushBlock.AGE, 3),
                bushName,
                Biome.Category.TAIGA,
                Blocks.GRASS_BLOCK
        );
    }

    private static void registerPatch(BlockState blockState, String featureName, Biome.Category biomeCategory, Block... whitelist) {
        ConfiguredFeature<?, ?> patchFeature = Feature.RANDOM_PATCH.configure((new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(blockState), SimpleBlockPlacer.INSTANCE)).tries(64).whitelist(Set.of(whitelist)).cannotProject().build());

        // Sparse feature
        ConfiguredFeature<?, ?> sparsePatch = patchFeature.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE);
        RegistryKey<ConfiguredFeature<?, ?>> sparsePatchRegistryKey = RegistryKey.of(
                Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + featureName + "_sparse")
        );
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, sparsePatchRegistryKey.getValue(), sparsePatch);
        BiomeModifications.addFeature(
                BiomeSelectors.categories(biomeCategory)
                              .and(ctx -> !AlaskaConfig.getConfig().snowyGen)
                              .and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.RAIN),
                GenerationStep.Feature.VEGETAL_DECORATION,
                sparsePatchRegistryKey
        );
        BiomeModifications.addFeature(
                BiomeSelectors.categories(Biome.Category.ICY),
                GenerationStep.Feature.VEGETAL_DECORATION,
                sparsePatchRegistryKey
        );

        // Decorated (extra sparse) patches
        ConfiguredFeature<?, ?> decoratedPatch = patchFeature.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_SPREAD_DOUBLE).applyChance(12);
        RegistryKey<ConfiguredFeature<?, ?>> decoratedPatchRegistryKey = RegistryKey.of(
                Registry.CONFIGURED_FEATURE_KEY,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + featureName + "_decorated")
        );
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, decoratedPatchRegistryKey.getValue(), decoratedPatch);
        BiomeModifications.addFeature(
                BiomeSelectors.categories(biomeCategory)
                              .and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.SNOW),
                GenerationStep.Feature.VEGETAL_DECORATION,
                decoratedPatchRegistryKey
        );
    }
}
