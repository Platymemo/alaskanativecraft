package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.*;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.KuspukSkirtFeatureRenderer;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.ShoulderPtarmiganFeatureRenderer;
import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class AlaskaEntities {
    private static final Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

    public static final EntityType<SealEntity> HARP_SEAL = add("harp_seal", createEntity(SpawnGroup.WATER_CREATURE, SealEntity::new, false, 1.0F, 0.6F));
    public static final EntityType<PtarmiganEntity> PTARMIGAN = add("ptarmigan", createEntity(SpawnGroup.AMBIENT, PtarmiganEntity::new, false, 0.5F, 0.5F));
    public static final EntityType<MooseEntity> MOOSE = add("moose", createEntity(SpawnGroup.CREATURE, MooseEntity::new, true, 1.5F, 1.3F));
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
        FabricDefaultAttributeRegistry.register(AlaskaEntities.HARP_SEAL, SealEntity.createSealAttributes());
        FabricDefaultAttributeRegistry.register(AlaskaEntities.PTARMIGAN, PtarmiganEntity.createPtarmiganAttributes());
        FabricDefaultAttributeRegistry.register(AlaskaEntities.MOOSE, MooseEntity.createMooseAttributes());
    }

    @SuppressWarnings("deprecation")
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

    private static <T extends Entity> EntityType<T> createEntity(SpawnGroup group, EntityType.EntityFactory<T> factory, boolean changingDimensions, float width, float height) {
        if (changingDimensions) {
            return FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.changing(width, height)).build();
        }
        return FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.fixed(width, height)).build();
    }

    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(WOODEN_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(STONE_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(IRON_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(GOLDEN_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(DIAMOND_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(NETHERITE_HARPOON, HarpoonEntityRenderer::new);

        EntityRendererRegistry.INSTANCE.register(HARP_SEAL, SealEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(PTARMIGAN, PtarmiganEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(MOOSE, MooseEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(DOGSLED, DogsledEntityRenderer::new);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof BipedEntityRenderer || entityRenderer instanceof ArmorStandEntityRenderer) {
                registrationHelper.register(new KuspukSkirtFeatureRenderer<>(entityRenderer, context.getModelLoader()));
            } else if (entityRenderer instanceof PlayerEntityRenderer playerEntityRenderer) {
                registrationHelper.register(new KuspukSkirtFeatureRenderer<>(playerEntityRenderer, context.getModelLoader()));
                registrationHelper.register(new ShoulderPtarmiganFeatureRenderer<>(playerEntityRenderer, context.getModelLoader()));
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerHarpoonPacket() {
        ClientPlayNetworking.registerGlobalReceiver(HarpoonEntity.SPAWN_PACKET, (client, handler, packet, responseSender) -> {
            EntityType<?> type = Registry.ENTITY_TYPE.get(packet.readVarInt());
            UUID entityUUID = packet.readUuid();
            int entityID = packet.readVarInt();
            double x = packet.readDouble();
            double y = packet.readDouble();
            double z = packet.readDouble();
            float pitch = (packet.readByte() * 360) / 256.0F;
            float yaw = (packet.readByte() * 360) / 256.0F;
            ClientWorld world = MinecraftClient.getInstance().world;
            Entity entity = type.create(world);
            client.execute(() -> {
                if (entity != null) {
                    entity.updateTrackedPositionAndAngles(x, y, z, yaw, pitch, 0, false);
                    entity.updateTrackedPosition(x, y, z); // The above does not do the same thing, for some weird reason
                    entity.setId(entityID);
                    entity.setUuid(entityUUID);
                    assert world != null;
                    world.addEntity(entityID, entity);
                }
            });
        });
    }
}
