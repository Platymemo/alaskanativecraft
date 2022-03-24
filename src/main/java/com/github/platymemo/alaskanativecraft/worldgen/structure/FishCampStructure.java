package com.github.platymemo.alaskanativecraft.worldgen.structure;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.mojang.serialization.Codec;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.ChunkRandom;

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
        ChunkPos chunkpos = context.chunkPos();
        BlockPos spawnXZPosition = context.chunkPos().getCenterAtY(0);
        int landHeight = context.chunkGenerator().getHeightInGround(spawnXZPosition.getX(), spawnXZPosition.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());

        Set<RegistryEntry<Biome>> biomes = context.chunkGenerator().getBiomeSource().getBiomesInArea(spawnXZPosition.getX(), landHeight, spawnXZPosition.getZ(), 32, context.chunkGenerator().getMultiNoiseSampler());
        return biomes.stream().anyMatch(biome -> biome.isIn(BiomeTags.IS_RIVER) || biome.isIn(BiomeTags.IS_OCEAN)) && !context.chunkGenerator().method_41053(StructureSetKeys.VILLAGES, context.seed(), chunkpos.x, chunkpos.z, 10);
    }

    public static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> createPiecesGenerator(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        if (!FishCampStructure.isFeatureChunk(context)) {
            return Optional.empty();
        }

        StructurePoolFeatureConfig newConfig = new StructurePoolFeatureConfig(() -> context.registryManager().get(Registry.STRUCTURE_POOL_KEY)
                .get(new Identifier(AlaskaNativeCraft.MOD_ID, "fish_camp/base")), 3);

        StructureGeneratorFactory.Context<StructurePoolFeatureConfig> newContext = new StructureGeneratorFactory.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.world(),
                context.validBiome(),
                context.structureManager(),
                context.registryManager());
        BlockPos blockpos = context.chunkPos().getCenterAtY(0);
        return StructurePoolBasedGenerator.generate(newContext, PoolStructurePiece::new, blockpos, false, true);
    }
}
