package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.SealEntityRenderer;
import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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

	public static final EntityType<SealEntity> HARBOR_SEAL = add("harbor_seal", FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, SealEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).build());

	public static void register() {
		for (Identifier id : ENTITY_TYPES.keySet()) {
			Registry.register(Registry.ENTITY_TYPE, id, ENTITY_TYPES.get(id));
		}

		FabricDefaultAttributeRegistry.register(AlaskaNativeEntities.HARBOR_SEAL, SealEntity.createMobAttributes().
				add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.75D).
				add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5D).
				add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0D));

		EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.HARBOR_SEAL, (dispatcher, context) -> new SealEntityRenderer(dispatcher));
	}

	private static <T extends EntityType<?>> T add(String name, T type) {
		Identifier id = new Identifier(AlaskaNativeCraft.MOD_ID, name);
		ENTITY_TYPES.put(id, type);
		return type;
	}

	private static EntityType<HarpoonEntity> createHarpoon(HarpoonItem item) {
		return FabricEntityTypeBuilder.<HarpoonEntity>create(SpawnGroup.MISC, (entity, world) -> new HarpoonEntity(entity, world, item)).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).build();
	}
}
