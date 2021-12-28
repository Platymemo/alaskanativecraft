package com.github.platymemo.alaskanativecraft.worldgen.structure;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.SnowyVillageData;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class AlaskaStructures {
    public static StructureFeature<StructurePoolFeatureConfig> FISH_CAMP = new FishCampStructure(StructurePoolFeatureConfig.CODEC);
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_FISH_CAMP = FISH_CAMP.configure(
            new StructurePoolFeatureConfig(() -> SnowyVillageData.STRUCTURE_POOLS, 0)
    );

    public static void register() {
        FabricStructureBuilder.create(new Identifier(AlaskaNativeCraft.MOD_ID, "fish_camp"), FISH_CAMP)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(new StructureConfig(16, 10, 423681754))
                .adjustsSurface()
                .register();

        Registry.register(
                BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
                new Identifier(AlaskaNativeCraft.MOD_ID, "configured_fish_camp"),
                CONFIGURED_FISH_CAMP
        );

        BiomeModifications.addStructure(
                BiomeSelectors.categories(Biome.Category.ICY, Biome.Category.PLAINS, Biome.Category.TAIGA),
                RegistryKey.of(
                        Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
                        BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(AlaskaStructures.CONFIGURED_FISH_CAMP)
                )
        );
    }
}
