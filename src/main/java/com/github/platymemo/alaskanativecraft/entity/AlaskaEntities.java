package com.github.platymemo.alaskanativecraft.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.config.AlaskaConfig.SpawnOptions.SpawnSettings;
import com.github.platymemo.alaskanativecraft.entity.damage.AlaskaDamageTypes;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors;

public class AlaskaEntities {
	private static final Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

	public static final EntityType<SealEntity> SEAL = add("seal", createEntity(SpawnGroup.WATER_CREATURE, SealEntity::new, true, 1.0F, 0.6F));
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
			Registry.register(Registries.ENTITY_TYPE, id, ENTITY_TYPES.get(id));
		}

		AlaskaDamageTypes.register();

		initAttributes();
		initSpawns();
		initSpawnRestrictions();
	}

	private static void initSpawnRestrictions() {
		SpawnRestriction.register(SEAL, SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SealEntity::canSpawn);
		SpawnRestriction.register(MOOSE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
		SpawnRestriction.register(PTARMIGAN, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, PtarmiganEntity::isValidSpawn);
	}

	// IntelliJ wrongly infers that the register call fails due to calling an accessor that throws an AssertionError
	@SuppressWarnings("ConstantConditions")
	private static void initAttributes() {
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(SEAL, SealEntity.createSealAttributes().build());
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(PTARMIGAN, PtarmiganEntity.createPtarmiganAttributes().build());
		DefaultAttributeRegistry.DEFAULT_ATTRIBUTE_REGISTRY.put(MOOSE, MooseEntity.createMooseAttributes().build());
	}

	private static void initSpawns() {
		SpawnSettings sealSettings = AlaskaNativeCraft.CONFIG.spawning.sealSettings;
		BiomeModifications.addSpawn(BiomeSelectors.isIn(AlaskaTags.HAS_SEAL),
				SpawnGroup.WATER_CREATURE, SEAL,
				sealSettings.weight.value(),
				sealSettings.minGroupSize.value(),
				sealSettings.maxGroupSize.value()
		);

		SpawnSettings mooseSettings = AlaskaNativeCraft.CONFIG.spawning.mooseSettings;
		BiomeModifications.addSpawn(BiomeSelectors.isIn(AlaskaTags.HAS_MOOSE),
				SpawnGroup.CREATURE, MOOSE,
				mooseSettings.weight.value(),
				mooseSettings.minGroupSize.value(),
				mooseSettings.maxGroupSize.value()
		);

		SpawnSettings ptarmiganSettings = AlaskaNativeCraft.CONFIG.spawning.ptarmiganSettings;
		BiomeModifications.addSpawn(BiomeSelectors.isIn(AlaskaTags.HAS_PTARMIGAN),
				SpawnGroup.AMBIENT, PTARMIGAN,
				ptarmiganSettings.weight.value(),
				ptarmiganSettings.minGroupSize.value(),
				ptarmiganSettings.maxGroupSize.value()
		);
	}

	private static <E extends EntityType<?>> E add(String name, E type) {
		Identifier id = new Identifier(AlaskaNativeCraft.MOD_ID, name);
		ENTITY_TYPES.put(id, type);
		return type;
	}

	private static EntityType<HarpoonEntity> createHarpoon(HarpoonItem item) {
		return QuiltEntityTypeBuilder.<HarpoonEntity>create(SpawnGroup.MISC, (entity, world) -> new HarpoonEntity(entity, world, item)).setDimensions(EntityDimensions.fixed(0.5F, 0.5F)).build();
	}

	private static <T extends Entity> EntityType<T> createEntity(SpawnGroup group, EntityType.EntityFactory<T> factory, boolean changingDimensions, float width, float height) {
		if (changingDimensions) {
			return QuiltEntityTypeBuilder.create(group, factory).setDimensions(EntityDimensions.changing(width, height)).build();
		}

		return QuiltEntityTypeBuilder.create(group, factory).setDimensions(EntityDimensions.fixed(width, height)).build();
	}
}
