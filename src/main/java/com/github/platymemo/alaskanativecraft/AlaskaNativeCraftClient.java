package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaNativeCraftModels;
import com.github.platymemo.alaskanativecraft.client.model.entity.DogsledEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.HarpoonEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.MooseEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.PtarmiganEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.SealEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.feature.KuspukSkirtModel;
import com.github.platymemo.alaskanativecraft.client.renderer.block.entity.DryingRackBlockEntityRenderer;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.*;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeableItem;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class AlaskaNativeCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
        registerBlockEntityRenderers();
        registerItemColors();
        registerBlockRenderLayers();
        registerHarpoonPacket();
        registerEntityModels();
    }
    
    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    private void registerEntityModels() {
        EntityModelLayerRegistry.registerModelLayer(AlaskaNativeCraftModels.PTARMIGAN, PtarmiganEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaNativeCraftModels.DOGSLED, DogsledEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaNativeCraftModels.HARPOON, HarpoonEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaNativeCraftModels.MOOSE, MooseEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaNativeCraftModels.SEAL, SealEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaNativeCraftModels.KUSPUK_SKIRT, KuspukSkirtModel::getTexturedModelData);
    }

    private void registerHarpoonPacket() {
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

    private static void registerItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack), AlaskaItems.KUSPUK_HOOD, AlaskaItems.KUSPUK_BODY);
    }

    private static void registerBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.BLUEBERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.CLOUDBERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.RASPBERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.SALMONBERRY_BUSH, RenderLayer.getCutout());
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.INSTANCE.register(AlaskaBlocks.DRYING_RACK_BLOCK_ENTITY, DryingRackBlockEntityRenderer::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.WOODEN_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.STONE_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.IRON_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.GOLDEN_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.DIAMOND_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.NETHERITE_HARPOON, HarpoonEntityRenderer::new);

        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.HARP_SEAL, SealEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.PTARMIGAN, PtarmiganEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.MOOSE, MooseEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(AlaskaEntities.DOGSLED, DogsledEntityRenderer::new);
    }
}
