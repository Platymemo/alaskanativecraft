package com.github.platymemo.alaskanativecraft.worldgen.feature;

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
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.RarityFilterPlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlaskaFeatures {
    private static final int DECORATED_MULTIPLIER = 12;
    private static final int BERRY_RARITY = 64;

    public static void register() {
        AlaskaConfig.GenerationOptions genOptions = AlaskaConfig.getConfig().generation;

        if (genOptions.genBlueberry) {
            registerBerryPatch(AlaskaBlocks.BLUEBERRY_BUSH, "blueberry_bush");
        }

        if (genOptions.genCloudberry) {
            registerBerryPatch(AlaskaBlocks.CLOUDBERRY_BUSH, "cloudberry_bush");
        }

        if (genOptions.genRaspberry) {
            registerBerryPatch(AlaskaBlocks.RASPBERRY_BUSH, "raspberry_bush");
        }

        if (genOptions.genSalmonberry) {
            registerBerryPatch(AlaskaBlocks.SALMONBERRY_BUSH, "salmonberry_bush");
        }

        if (genOptions.genDriftwood) {
            registerPatch(AlaskaBlocks.DRIFTWOOD_LOG.getDefaultState(), "washed_up_driftwood", BERRY_RARITY / 2, Biome.Category.BEACH, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.CLAY, Blocks.SAND, Blocks.RED_SAND);
        }
    }

    private static void registerBerryPatch(@NotNull Block berryBush, String bushName) {
        registerPatch(
                berryBush.getDefaultState().with(SweetBerryBushBlock.AGE, 3),
                bushName + "_taiga",
                BERRY_RARITY,
                Biome.Category.TAIGA,
                Blocks.GRASS_BLOCK
        );
        registerPatch(
                berryBush.getDefaultState().with(SweetBerryBushBlock.AGE, 3),
                bushName + "_tundra",
                BERRY_RARITY * 2,
                Biome.Category.TAIGA,
                Blocks.GRASS_BLOCK
        );
    }

    private static void registerPatch(BlockState blockState, String featureName, int rarity, Biome.Category biomeCategory, Block... whitelist) {
        ConfiguredFeature<RandomPatchFeatureConfig, ?> patchFeature = Feature.RANDOM_PATCH.configure(ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(blockState))), List.of(whitelist)));

        // Sparse feature
        PlacedFeature sparsePatch = patchFeature.withPlacement(RarityFilterPlacementModifier.of(rarity), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
        RegistryKey<PlacedFeature> sparsePatchRegistryKey = RegistryKey.of(
                Registry.PLACED_FEATURE_KEY,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + featureName + "_sparse")
        );
        Registry.register(BuiltinRegistries.PLACED_FEATURE, sparsePatchRegistryKey.getValue(), sparsePatch);
        BiomeModifications.addFeature(
                BiomeSelectors.categories(biomeCategory)
                        .and(ctx -> !AlaskaConfig.getConfig().generation.snowyGen)
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
        PlacedFeature decoratedPatch = patchFeature.withPlacement(RarityFilterPlacementModifier.of(rarity * DECORATED_MULTIPLIER), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
        RegistryKey<PlacedFeature> decoratedPatchRegistryKey = RegistryKey.of(
                Registry.PLACED_FEATURE_KEY,
                new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + featureName + "_decorated")
        );
        Registry.register(BuiltinRegistries.PLACED_FEATURE, decoratedPatchRegistryKey.getValue(), decoratedPatch);
        BiomeModifications.addFeature(
                BiomeSelectors.categories(biomeCategory)
                        .and(ctx -> ctx.getBiome().getPrecipitation() == Biome.Precipitation.SNOW),
                GenerationStep.Feature.VEGETAL_DECORATION,
                decoratedPatchRegistryKey
        );
    }
}
