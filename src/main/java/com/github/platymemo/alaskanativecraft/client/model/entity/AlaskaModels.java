package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.feature.KuspukSkirtModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AlaskaModels {
    public static final EntityModelLayer PTARMIGAN = registerMain("ptarmigan");
    public static final EntityModelLayer DOGSLED = registerMain("dogsled");
    public static final EntityModelLayer HARPOON = registerMain("harpoon");
    public static final EntityModelLayer MOOSE = registerMain("moose");
    public static final EntityModelLayer SEAL = registerMain("seal");
    public static final EntityModelLayer KUSPUK_SKIRT = registerMain("kuspuk_skirt");

    private static EntityModelLayer registerMain(String id) {
        return new EntityModelLayer(new Identifier(AlaskaNativeCraft.MOD_ID, id), "main");
    }

    @Environment(EnvType.CLIENT)
    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    public static void registerEntityModels() {
        EntityModelLayerRegistry.registerModelLayer(AlaskaModels.PTARMIGAN, PtarmiganEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaModels.DOGSLED, DogsledEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaModels.HARPOON, HarpoonEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaModels.MOOSE, MooseEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaModels.SEAL, SealEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AlaskaModels.KUSPUK_SKIRT, KuspukSkirtModel::getTexturedModelData);
    }
}
