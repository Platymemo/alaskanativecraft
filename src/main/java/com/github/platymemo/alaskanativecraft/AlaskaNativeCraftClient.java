package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.client.renderer.entity.*;
import com.github.platymemo.alaskanativecraft.entity.AlaskaNativeEntities;
import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class AlaskaNativeCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
        registerHarpoonPacket();
    }

    private void registerHarpoonPacket() {
        ClientSidePacketRegistry.INSTANCE.register(HarpoonEntity.SPAWN_PACKET, (context, packet) -> {
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
            context.getTaskQueue().execute(() -> {
                if (entity != null) {
                    entity.updatePosition(x, y, z);
                    entity.updateTrackedPosition(x, y, z);
                    entity.pitch = pitch;
                    entity.yaw = yaw;
                    entity.setEntityId(entityID);
                    entity.setUuid(entityUUID);
                    assert world != null;
                    world.addEntity(entityID, entity);
                }
            });
        });
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.WOODEN_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.STONE_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.IRON_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.GOLDEN_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.DIAMOND_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.NETHERITE_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));

        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.HARBOR_SEAL, (entityRenderDispatcher, context) -> new SealEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.PTARMIGAN, (entityRenderDispatcher, context) -> new PtarmiganEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.MOOSE, (entityRenderDispatcher, context) -> new MooseEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaNativeEntities.DOGSLED, (entityRenderDispatcher, context) -> new DogsledEntityRenderer(entityRenderDispatcher));
    }
}
