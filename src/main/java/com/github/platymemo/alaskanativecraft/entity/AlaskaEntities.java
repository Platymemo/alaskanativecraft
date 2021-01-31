package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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

        FabricDefaultAttributeRegistry.register(AlaskaEntities.HARP_SEAL, SealEntity.createSealAttributes());
        FabricDefaultAttributeRegistry.register(AlaskaEntities.PTARMIGAN, PtarmiganEntity.createPtarmiganAttributes());
        FabricDefaultAttributeRegistry.register(AlaskaEntities.MOOSE, MooseEntity.createMooseAttributes());

        initSpawns();
    }

    private static void initSpawns() {
        BiomeModifications.addSpawn(ctx -> ctx.getBiome().getCategory() == Biome.Category.OCEAN, SpawnGroup.WATER_CREATURE, AlaskaEntities.HARP_SEAL, 1, 1, 4);
        BiomeModifications.addSpawn(ctx -> ctx.getBiome().getCategory() == Biome.Category.RIVER, SpawnGroup.CREATURE, AlaskaEntities.HARP_SEAL, 1, 1, 2);
        BiomeModifications.addSpawn(ctx -> ctx.getBiome().getCategory() == Biome.Category.ICY, SpawnGroup.CREATURE, AlaskaEntities.MOOSE, 1, 1, 3);
        BiomeModifications.addSpawn(ctx -> ctx.getBiome().getCategory() == Biome.Category.ICY, SpawnGroup.CREATURE, AlaskaEntities.PTARMIGAN, 2, 2, 5);
        BiomeModifications.addSpawn(ctx -> ctx.getBiome().getCategory() == Biome.Category.TAIGA, SpawnGroup.CREATURE, AlaskaEntities.MOOSE, 1, 1, 3);
        BiomeModifications.addSpawn(ctx -> ctx.getBiome().getCategory() == Biome.Category.TAIGA, SpawnGroup.AMBIENT, AlaskaEntities.PTARMIGAN, 25, 2, 5);
        BiomeModifications.addSpawn(ctx -> ctx.getBiome().getCategory() == Biome.Category.FOREST, SpawnGroup.CREATURE, AlaskaEntities.MOOSE, 1, 1, 3);
        BiomeModifications.addSpawn(ctx -> ctx.getBiome().getCategory() == Biome.Category.FOREST, SpawnGroup.AMBIENT, AlaskaEntities.PTARMIGAN, 5, 1, 3);
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
