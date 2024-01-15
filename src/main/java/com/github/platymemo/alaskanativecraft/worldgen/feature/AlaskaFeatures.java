package com.github.platymemo.alaskanativecraft.worldgen.feature;

import java.util.function.Predicate;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors;

public class AlaskaFeatures {
	public static void register() {
		AlaskaConfig.GenerationOptions genOptions = AlaskaNativeCraft.CONFIG.generation;

		if (genOptions.genBlueberry.value()) {
			registerBerryPatch("blueberry");
		}

		if (genOptions.genCloudberry.value()) {
			registerBerryPatch("cloudberry");
		}

		if (genOptions.genRaspberry.value()) {
			registerBerryPatch("raspberry");
		}

		if (genOptions.genSalmonberry.value()) {
			registerBerryPatch("salmonberry");
		}

		if (genOptions.genLabradorTea.value()) {
			registerPatch("labrador_tea", BiomeSelectors.isIn(AlaskaTags.HAS_LABRADOR_TEA));
		}

		if (genOptions.genDriftwood.value()) {
			registerPatch("washed_up_driftwood", BiomeSelectors.isIn(AlaskaTags.HAS_DRIFTWOOD));
		}
	}

	private static void registerBerryPatch(String bushName) {
		registerPatch(
				bushName,
				BiomeSelectors.isIn(AlaskaTags.HAS_BUSHES)
		);
	}

	private static void registerPatch(String featureName, Predicate<BiomeSelectionContext> selector) {
		RegistryKey<PlacedFeature> commonPatch = RegistryKey.of(
				RegistryKeys.PLACED_FEATURE,
				new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + featureName + "_common")
		);

		BiomeModifications.addFeature(
				selector,
				GenerationStep.Feature.VEGETAL_DECORATION,
				commonPatch
		);

		RegistryKey<PlacedFeature> rarePatch = RegistryKey.of(
				RegistryKeys.PLACED_FEATURE,
				new Identifier(AlaskaNativeCraft.MOD_ID, "patch_" + featureName + "_rare")
		);

		BiomeModifications.addFeature(
				selector,
				GenerationStep.Feature.VEGETAL_DECORATION,
				rarePatch
		);
	}
}
