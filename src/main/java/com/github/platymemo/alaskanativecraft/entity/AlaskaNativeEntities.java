package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlaskaNativeEntities {
    private static final Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

	public static final EntityType<HarpoonEntity> WOODEN_HARPOON = add("wooden_harpoon", createHarpoon(AlaskaNativeItems.WOODEN_HARPOON));
	public static final EntityType<HarpoonEntity> STONE_HARPOON = add("stone_harpoon", createHarpoon(AlaskaNativeItems.STONE_HARPOON));
	public static final EntityType<HarpoonEntity> IRON_HARPOON = add("iron_harpoon", createHarpoon(AlaskaNativeItems.IRON_HARPOON));
	public static final EntityType<HarpoonEntity> GOLDEN_HARPOON = add("golden_harpoon", createHarpoon(AlaskaNativeItems.GOLDEN_HARPOON));
	public static final EntityType<HarpoonEntity> DIAMOND_HARPOON = add("diamond_harpoon", createHarpoon(AlaskaNativeItems.DIAMOND_HARPOON));
	public static final EntityType<HarpoonEntity> NETHERITE_HARPOON = add("netherite_harpoon", createHarpoon(AlaskaNativeItems.NETHERITE_HARPOON));

	public static final EntityType<SealEntity> HARBOR_SEAL = add("harbor_seal", createEntity(SpawnGroup.WATER_CREATURE, SealEntity::new, false, 1.0F, 0.6F));
	public static final EntityType<PtarmiganEntity> PTARMIGAN = add("ptarmigan", createEntity(SpawnGroup.AMBIENT, PtarmiganEntity::new, false, 0.5F, 0.5F));
	public static final EntityType<MooseEntity> MOOSE = add("moose", createEntity(SpawnGroup.CREATURE, MooseEntity::new, true, 1.5F, 1.3F));
	public static final EntityType<DogsledEntity> DOGSLED = add("dogsled", createEntity(SpawnGroup.MISC, DogsledEntity::new, false, 1.5F, 1.0F));

	public static void register() {
		for (Identifier id : ENTITY_TYPES.keySet()) {
			Registry.register(Registry.ENTITY_TYPE, id, ENTITY_TYPES.get(id));
		}

		FabricDefaultAttributeRegistry.register(AlaskaNativeEntities.HARBOR_SEAL, SealEntity.createSealAttributes());
		FabricDefaultAttributeRegistry.register(AlaskaNativeEntities.PTARMIGAN, PtarmiganEntity.createPtarmiganAttributes());
		FabricDefaultAttributeRegistry.register(AlaskaNativeEntities.MOOSE, MooseEntity.createMooseAttributes());

		//initSpawns();
	}

	private static void initSpawns() {
		// TODO
		// Fill this out and remove the biome mixins
		BiomeModifications.addSpawn(
				ctx -> ctx.getBiome().getCategory() == Biome.Category.OCEAN, SpawnGroup.CREATURE, AlaskaNativeEntities.HARBOR_SEAL, 1, 2, 3
		);
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
