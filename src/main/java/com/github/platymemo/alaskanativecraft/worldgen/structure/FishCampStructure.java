package com.github.platymemo.alaskanativecraft.worldgen.structure;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.registry.Holder;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.structure.StructureType;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.heightprovider.HeightProvider;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class FishCampStructure extends StructureFeature {
    public static final Codec<FishCampStructure> CODEC = RecordCodecBuilder.<FishCampStructure>mapCodec(instance ->
            instance.group(FishCampStructure.settingsCodec(instance),
                    StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Type.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, FishCampStructure::new)).codec();

    private final Holder<StructurePool> startPool;
    private final Optional<Identifier> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Type> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public FishCampStructure(StructureSettings config,
                             Holder<StructurePool> startPool,
                             Optional<Identifier> startJigsawName,
                             int size,
                             HeightProvider startHeight,
                             Optional<Heightmap.Type> projectStartToHeightmap,
                             int maxDistanceFromCenter) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    @Override
    public Optional<GenerationStub> findGenerationPos(GenerationContext context) {
        BlockPos spawnXZPosition = context.chunkPos().getCenterAtY(0);
        int landHeight = context.chunkGenerator().getHeightInGround(spawnXZPosition.getX(), spawnXZPosition.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world(), context.randomState());

        var biomes = context.chunkGenerator().getBiomeSource().getBiomesInArea(spawnXZPosition.getX(), landHeight, spawnXZPosition.getZ(), 32, context.randomState().getSampler());
        if (biomes.stream().noneMatch(biome -> biome.isIn(BiomeTags.RIVER) || biome.isIn(BiomeTags.OCEAN))) {
            return Optional.empty();
        }

        return StructurePoolBasedGenerator.generate(context, this.startPool, this.startJigsawName, this.size, spawnXZPosition.up(landHeight), false, this.projectStartToHeightmap, this.maxDistanceFromCenter);
    }

    @Override
    public StructureType<?> getType() {
        return AlaskaStructures.FISH_CAMP;
    }
}
