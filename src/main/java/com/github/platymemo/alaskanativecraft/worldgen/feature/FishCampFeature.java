package com.github.platymemo.alaskanativecraft.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.JigsawFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Set;

public class FishCampFeature extends JigsawFeature {
    public FishCampFeature(Codec<StructurePoolFeatureConfig> configCodec) {
        super(configCodec, 0, true, true, FishCampFeature::canGenerate);
    }

    @SuppressWarnings("deprecation")
    private static boolean canGenerate(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        ChunkPos chunkPos = context.chunkPos();
        if (context.chunkGenerator().method_41053(StructureSetKeys.VILLAGES, context.seed(), chunkPos.x, chunkPos.z, 10)) return false;

        BlockPos spawnXZPosition = context.chunkPos().getCenterAtY(0);
        int landHeight = context.chunkGenerator().getHeightInGround(spawnXZPosition.getX(), spawnXZPosition.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());

        Set<RegistryEntry<Biome>> biomes = context.chunkGenerator().getBiomeSource().getBiomesInArea(spawnXZPosition.getX(), landHeight, spawnXZPosition.getZ(), 32, context.chunkGenerator().getMultiNoiseSampler());
        return biomes.stream().anyMatch(biome -> Biome.getCategory(biome) == Biome.Category.RIVER || Biome.getCategory(biome) == Biome.Category.OCEAN);
    }
}
