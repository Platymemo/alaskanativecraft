package com.github.platymemo.alaskanativecraft.worldgen.structure;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.mojang.serialization.Codec;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Optional;
import java.util.Set;

public class FishCampStructure extends StructureFeature<StructurePoolFeatureConfig> {

    public FishCampStructure(Codec<StructurePoolFeatureConfig> codec) {
        super(codec, FishCampStructure::createPiecesGenerator, PostPlacementProcessor.EMPTY);
    }

    /**
     * Spawns fish camps only if:
     * a) No village is within 10 chunks
     * b) A river or ocean is within 32 blocks
     */
    private static boolean isFeatureChunk(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        if (isVillageNearby(context.chunkGenerator(), context.seed(), context.chunkPos())) return false;

        BlockPos spawnXZPosition = context.chunkPos().getCenterAtY(0);
        int landHeight = context.chunkGenerator().getHeightInGround(spawnXZPosition.getX(), spawnXZPosition.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());

        Set<Biome> biomes = context.chunkGenerator().getBiomeSource().getBiomesInArea(spawnXZPosition.getX(), landHeight, spawnXZPosition.getZ(), 32, context.chunkGenerator().getMultiNoiseSampler());
        return biomes.stream().anyMatch(biome -> biome.getCategory() == Biome.Category.RIVER || biome.getCategory() == Biome.Category.OCEAN);
    }

    /**
     * Checks for a village within 10 chunks
     * identical to {@link net.minecraft.world.gen.feature.PillagerOutpostFeature::isVillageNearby}
     */
    private static boolean isVillageNearby(ChunkGenerator chunkGenerator, long seed, ChunkPos chunkPos) {
        StructureConfig structureConfig = chunkGenerator.getStructuresConfig().getForType(StructureFeature.VILLAGE);
        if (structureConfig != null) {
            for (int k = chunkPos.x - 10; k <= chunkPos.x + 10; ++k) {
                for (int l = chunkPos.z - 10; l <= chunkPos.z + 10; ++l) {
                    ChunkPos chunkPos2 = StructureFeature.VILLAGE.getStartChunk(structureConfig, seed, k, l);
                    if (k == chunkPos2.x && l == chunkPos2.z) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> createPiecesGenerator(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {

        // Check if the spot is valid for our structure.
        if (!FishCampStructure.isFeatureChunk(context)) {
            return Optional.empty();
        }

        StructurePoolFeatureConfig newConfig = new StructurePoolFeatureConfig(
                () -> context.registryManager()
                        .get(Registry.STRUCTURE_POOL_KEY)
                        .get(new Identifier(AlaskaNativeCraft.MOD_ID, "fish_camp/base")),
                3
        );

        StructureGeneratorFactory.Context<StructurePoolFeatureConfig> newContext = new StructureGeneratorFactory.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.world(),
                context.validBiome(),
                context.structureManager(),
                context.registryManager()
        );
        BlockPos blockpos = context.chunkPos().getCenterAtY(0);

        return StructurePoolBasedGenerator.generate(newContext, PoolStructurePiece::new, blockpos, false, true);
    }
}
