package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlaskaEntities {
    private static final Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

    public static final EntityType<HarpoonEntity> WOODEN_HARPOON = add("wooden_harpoon", createHarpoon(AlaskaItems.WOODEN_HARPOON));
    public static final EntityType<HarpoonEntity> STONE_HARPOON = add("stone_harpoon", createHarpoon(AlaskaItems.STONE_HARPOON));
    public static final EntityType<HarpoonEntity> IRON_HARPOON = add("iron_harpoon", createHarpoon(AlaskaItems.IRON_HARPOON));
    public static final EntityType<HarpoonEntity> GOLDEN_HARPOON = add("golden_harpoon", createHarpoon(AlaskaItems.GOLDEN_HARPOON));
    public static final EntityType<HarpoonEntity> DIAMOND_HARPOON = add("diamond_harpoon", createHarpoon(AlaskaItems.DIAMOND_HARPOON));
    public static final EntityType<HarpoonEntity> NETHERITE_HARPOON = add("netherite_harpoon", createHarpoon(AlaskaItems.NETHERITE_HARPOON));

    public static final EntityType<SealEntity> HARP_SEAL = add("harp_seal", createEntity(SpawnGroup.WATER_CREATURE, SealEntity::new, false, 1.0F, 0.6F));
    public static final EntityType<PtarmiganEntity> PTARMIGAN = add("ptarmigan", createEntity(SpawnGroup.AMBIENT, PtarmiganEntity::new, false, 0.5F, 0.5F));
    public static final EntityType<MooseEntity> MOOSE = add("moose", createEntity(SpawnGroup.CREATURE, MooseEntity::new, true, 1.5F, 1.3F));
    public static final EntityType<DogsledEntity> DOGSLED = add("dogsled", createEntity(SpawnGroup.MISC, DogsledEntity::new, false, 1.5F, 1.0F));

    public static void register() {
        for (Identifier id : ENTITY_TYPES.keySet()) {
            Registry.register(Registry.ENTITY_TYPE, id, ENTITY_TYPES.get(id));
        }

        initAttributes();
        initSpawns();
    }

    public static void registerSpawnRestrictions(Map<EntityType<?>, SpawnRestriction.Entry> restrictions) {
        restrictions.put(HARP_SEAL,
                new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                        SpawnRestriction.Location.NO_RESTRICTIONS,
                        SealEntity::canSpawn)
        );
        restrictions.put(MOOSE,
                new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                        SpawnRestriction.Location.ON_GROUND,
                        (SpawnRestriction.SpawnPredicate<MooseEntity>) AnimalEntity::isValidNaturalSpawn)
        );
        restrictions.put(PTARMIGAN,
                new SpawnRestriction.Entry(Heightmap.Type.MOTION_BLOCKING,
                        SpawnRestriction.Location.ON_GROUND,
                        (SpawnRestriction.SpawnPredicate<ParrotEntity>) ParrotEntity::canSpawn)
        );
    }

    private static void initAttributes() {
        FabricDefaultAttributeRegistry.register(AlaskaEntities.HARP_SEAL, SealEntity.createSealAttributes());
        FabricDefaultAttributeRegistry.register(AlaskaEntities.PTARMIGAN, PtarmiganEntity.createPtarmiganAttributes());
        FabricDefaultAttributeRegistry.register(AlaskaEntities.MOOSE, MooseEntity.createMooseAttributes());
    }

    private static void initSpawns() {
        AlaskaConfig.SpawnOptions spawnOptions = AlaskaConfig.getConfig().spawnOptions;
        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.OCEAN),
                SpawnGroup.WATER_CREATURE,
                AlaskaEntities.HARP_SEAL,
                spawnOptions.sealOceanSettings.weight,
                spawnOptions.sealOceanSettings.minGroupSize,
                spawnOptions.sealOceanSettings.maxGroupSize);
        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.RIVER),
                SpawnGroup.WATER_CREATURE,
                AlaskaEntities.HARP_SEAL,
                spawnOptions.sealRiverSettings.weight,
                spawnOptions.sealRiverSettings.minGroupSize,
                spawnOptions.sealRiverSettings.maxGroupSize);
        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.TAIGA, Biome.Category.ICY, Biome.Category.FOREST),
                SpawnGroup.CREATURE, AlaskaEntities.MOOSE,
                spawnOptions.mooseSettings.weight,
                spawnOptions.mooseSettings.minGroupSize,
                spawnOptions.mooseSettings.maxGroupSize);
        BiomeModifications.addSpawn(BiomeSelectors.categories(Biome.Category.TAIGA, Biome.Category.ICY, Biome.Category.FOREST),
                SpawnGroup.AMBIENT, AlaskaEntities.PTARMIGAN,
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

    private static <T extends Entity> EntityType<T> createEntity(SpawnGroup group, net.minecraft.entity.EntityType.EntityFactory<T> factory, boolean changingDimensions, float width, float height) {
        if (changingDimensions) {
            return FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.changing(width, height)).build();
        }
        return FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.fixed(width, height)).build();
    }
}
