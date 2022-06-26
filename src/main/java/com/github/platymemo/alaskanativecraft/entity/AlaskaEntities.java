package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlaskaEntities {
    private static final Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

    public static final EntityType<SealEntity> HARP_SEAL = add("harp_seal", createEntity(SpawnGroup.WATER_CREATURE, SealEntity::new, true, 1.0F, 0.6F));
    public static final EntityType<PtarmiganEntity> PTARMIGAN = add("ptarmigan", createEntity(SpawnGroup.AMBIENT, PtarmiganEntity::new, false, 0.5F, 0.5F));
    public static final EntityType<MooseEntity> MOOSE = add("moose", createEntity(SpawnGroup.CREATURE, MooseEntity::new, true, 3F, 2.6F));
    public static final EntityType<DogsledEntity> DOGSLED = add("dogsled", createEntity(SpawnGroup.MISC, DogsledEntity::new, false, 1.5F, 1.0F));

    public static final EntityType<HarpoonEntity> WOODEN_HARPOON = add("wooden_harpoon", createHarpoon(AlaskaItems.WOODEN_HARPOON));
    public static final EntityType<HarpoonEntity> STONE_HARPOON = add("stone_harpoon", createHarpoon(AlaskaItems.STONE_HARPOON));
    public static final EntityType<HarpoonEntity> IRON_HARPOON = add("iron_harpoon", createHarpoon(AlaskaItems.IRON_HARPOON));
    public static final EntityType<HarpoonEntity> GOLDEN_HARPOON = add("golden_harpoon", createHarpoon(AlaskaItems.GOLDEN_HARPOON));
    public static final EntityType<HarpoonEntity> DIAMOND_HARPOON = add("diamond_harpoon", createHarpoon(AlaskaItems.DIAMOND_HARPOON));
    public static final EntityType<HarpoonEntity> NETHERITE_HARPOON = add("netherite_harpoon", createHarpoon(AlaskaItems.NETHERITE_HARPOON));

    public static void register() {
        for (Identifier id : ENTITY_TYPES.keySet()) {
            Registry.register(Registry.ENTITY_TYPE, id, ENTITY_TYPES.get(id));
        }

        initAttributes();
        initSpawns();
        initSpawnRestrictions();
    }

    private static void initSpawnRestrictions() {
        SpawnRestrictionAccessor.callRegister(HARP_SEAL, SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SealEntity::canSpawn);
        SpawnRestrictionAccessor.callRegister(MOOSE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
        SpawnRestrictionAccessor.callRegister(PTARMIGAN, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, PtarmiganEntity::isValidSpawn);
    }

    private static void initAttributes() {
        FabricDefaultAttributeRegistry.register(HARP_SEAL, SealEntity.createSealAttributes());
        FabricDefaultAttributeRegistry.register(PTARMIGAN, PtarmiganEntity.createPtarmiganAttributes());
        FabricDefaultAttributeRegistry.register(MOOSE, MooseEntity.createMooseAttributes());
    }

    private static void initSpawns() {
        AlaskaConfig.SpawnOptions spawnOptions = AlaskaConfig.getConfig().spawning;
        BiomeModifications.addSpawn(BiomeSelectors.tag(AlaskaTags.HAS_SEAL),
                SpawnGroup.WATER_CREATURE, HARP_SEAL,
                spawnOptions.sealOceanSettings.weight,
                spawnOptions.sealOceanSettings.minGroupSize,
                spawnOptions.sealOceanSettings.maxGroupSize);
        BiomeModifications.addSpawn(BiomeSelectors.tag(AlaskaTags.HAS_MOOSE),
                SpawnGroup.CREATURE, MOOSE,
                spawnOptions.mooseSettings.weight,
                spawnOptions.mooseSettings.minGroupSize,
                spawnOptions.mooseSettings.maxGroupSize);
        BiomeModifications.addSpawn(BiomeSelectors.tag(AlaskaTags.HAS_PTARMIGAN),
                SpawnGroup.AMBIENT, PTARMIGAN,
                spawnOptions.ptarmiganSettings.weight,
                spawnOptions.ptarmiganSettings.minGroupSize,
                spawnOptions.ptarmiganSettings.maxGroupSize);
    }

    private static <E extends EntityType<?>> E add(String name, E type) {
        Identifier id = new Identifier(AlaskaNativeCraft.MOD_ID, name);
        ENTITY_TYPES.put(id, type);
        return type;
    }

    private static EntityType<HarpoonEntity> createHarpoon(HarpoonItem item) {
        return FabricEntityTypeBuilder.<HarpoonEntity>create(SpawnGroup.MISC, (entity, world) -> new HarpoonEntity(entity, world, item)).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).build();
    }

    private static <T extends Entity> EntityType<T> createEntity(SpawnGroup group, EntityType.EntityFactory<T> factory, boolean changingDimensions, float width, float height) {
        if (changingDimensions) {
            return FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.changing(width, height)).build();
        }
        return FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.fixed(width, height)).build();
    }

}
