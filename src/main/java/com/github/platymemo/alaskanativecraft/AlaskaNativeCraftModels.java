package com.github.platymemo.alaskanativecraft;

import com.github.platymemo.alaskanativecraft.client.model.entity.DogsledEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.HarpoonEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.MooseEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.PtarmiganEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.SealEntityModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.feature.KuspukSkirtModel;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class AlaskaNativeCraftModels {
    public static final EntityModelLayer PTARMIGAN_MODEL = registerMain("ptarmigan");
    public static final EntityModelLayer DOGSLED_MODEL = registerMain("dogsled");
    public static final EntityModelLayer HARPOON_MODEL = registerMain("harpoon");
    public static final EntityModelLayer MOOSE_MODEL = registerMain("moose");
    public static final EntityModelLayer SEAL_MODEL = registerMain("seal");
    public static final EntityModelLayer KUSPUK_SKIRT_MODEL = registerMain("kuspuk_skirt");


    private static EntityModelLayer registerMain(String id){
        return new EntityModelLayer(new Identifier(AlaskaNativeCraft.MOD_ID, id), "main");
    }

    public static void registerEntityModels() {
        EntityModelLayerRegistry.registerModelLayer(PTARMIGAN_MODEL, PtarmiganEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DOGSLED_MODEL, DogsledEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HARPOON_MODEL, HarpoonEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MOOSE_MODEL, MooseEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SEAL_MODEL, SealEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(KUSPUK_SKIRT_MODEL, KuspukSkirtModel::getTexturedModelData);
    }
}
