package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.client.model.entity.AlaskaModels;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AlaskaNativeCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AlaskaEntities.registerEntityRenderers();
        AlaskaBlocks.registerBlockEntityRenderers();
        AlaskaBlocks.registerBlockRenderLayers();
        AlaskaItems.registerItemColors();
        AlaskaEntities.registerHarpoonPacket();
        AlaskaModels.registerEntityModels();
    }
}
