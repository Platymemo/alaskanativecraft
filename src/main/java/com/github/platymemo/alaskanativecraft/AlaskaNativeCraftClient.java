package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.client.renderer.block.entity.DryingRackBlockEntityRenderer;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.*;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.KuspukSkirtFeatureRenderer;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.ShoulderPtarmiganFeatureRenderer;
import com.github.platymemo.alaskanativecraft.client.renderer.entity.feature.SnowshoeFeatureRenderer;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeableItem;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class AlaskaNativeCraftClient implements ClientModInitializer {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(AlaskaEntities.WOODEN_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.register(AlaskaEntities.STONE_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.register(AlaskaEntities.IRON_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.register(AlaskaEntities.GOLDEN_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.register(AlaskaEntities.DIAMOND_HARPOON, HarpoonEntityRenderer::new);
        EntityRendererRegistry.register(AlaskaEntities.NETHERITE_HARPOON, HarpoonEntityRenderer::new);

        EntityRendererRegistry.register(AlaskaEntities.HARP_SEAL, SealEntityRenderer::new);
        EntityRendererRegistry.register(AlaskaEntities.PTARMIGAN, PtarmiganEntityRenderer::new);
        EntityRendererRegistry.register(AlaskaEntities.MOOSE, MooseEntityRenderer::new);
        EntityRendererRegistry.register(AlaskaEntities.DOGSLED, DogsledEntityRenderer::new);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof BipedEntityRenderer || entityRenderer instanceof ArmorStandEntityRenderer) {
                registrationHelper.register(new KuspukSkirtFeatureRenderer<>(entityRenderer, context.getModelLoader()));
                registrationHelper.register(new SnowshoeFeatureRenderer(entityRenderer, context.getModelLoader()));
            } else if (entityRenderer instanceof PlayerEntityRenderer playerEntityRenderer) {
                registrationHelper.register(new KuspukSkirtFeatureRenderer<>(playerEntityRenderer, context.getModelLoader()));
                registrationHelper.register(new SnowshoeFeatureRenderer<>(playerEntityRenderer, context.getModelLoader()));
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

    @Environment(EnvType.CLIENT)
    public static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(AlaskaBlocks.DRYING_RACK_BLOCK_ENTITY, DryingRackBlockEntityRenderer::new);
    }

    @Environment(EnvType.CLIENT)
    public static void registerBlockRenderLayers() {
        RenderLayer cutout = RenderLayer.getCutout();
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.BLUEBERRY_BUSH, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.CLOUDBERRY_BUSH, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.RASPBERRY_BUSH, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.SALMONBERRY_BUSH, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(AlaskaBlocks.LABRADOR_TEA_BUSH, cutout);
    }

    @Environment(EnvType.CLIENT)
    public static void registerItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack), AlaskaItems.KUSPUK_HOOD, AlaskaItems.KUSPUK_BODY, AlaskaItems.MUKLUKS);
    }

    @Override
    public void onInitializeClient() {
        registerBlockEntityRenderers();
        registerBlockRenderLayers();
        registerItemColors();
        AlaskaModels.registerEntityModels();
        registerEntityRenderers();
        registerHarpoonPacket();
    }
}
