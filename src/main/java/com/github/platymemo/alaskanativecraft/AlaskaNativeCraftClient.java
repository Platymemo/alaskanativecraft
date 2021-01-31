package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.*;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class AlaskaNativeCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
        registerBlockRenderLayers();
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

    private static void registerBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.BLUEBERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.CLOUDBERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.RASPBERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.SALMONBERRY_BUSH, RenderLayer.getCutout());
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.WOODEN_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.STONE_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.IRON_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.GOLDEN_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.DIAMOND_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.NETHERITE_HARPOON, (dispatcher, context) -> new HarpoonEntityRenderer(dispatcher));

        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.HARP_SEAL, (entityRenderDispatcher, context) -> new SealEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.PTARMIGAN, (entityRenderDispatcher, context) -> new PtarmiganEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.MOOSE, (entityRenderDispatcher, context) -> new MooseEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.DOGSLED, (entityRenderDispatcher, context) -> new DogsledEntityRenderer(entityRenderDispatcher));
    }
}
