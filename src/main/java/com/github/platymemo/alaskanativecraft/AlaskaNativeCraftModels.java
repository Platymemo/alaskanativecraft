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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AlaskaNativeCraftModels {
    public static final EntityModelLayer PTARMIGAN = registerMain("ptarmigan");
    public static final EntityModelLayer DOGSLED = registerMain("dogsled");
    public static final EntityModelLayer HARPOON = registerMain("harpoon");
    public static final EntityModelLayer MOOSE = registerMain("moose");
    public static final EntityModelLayer SEAL = registerMain("seal");
    public static final EntityModelLayer KUSPUK_SKIRT = registerMain("kuspuk_skirt");


    private static EntityModelLayer registerMain(String id){
        return new EntityModelLayer(new Identifier(AlaskaNativeCraft.MOD_ID, id), "main");
    }

    public static void registerEntityModels() {
        EntityModelLayerRegistry.registerModelLayer(PTARMIGAN, PtarmiganEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DOGSLED, DogsledEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HARPOON, HarpoonEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MOOSE, MooseEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SEAL, SealEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(KUSPUK_SKIRT, KuspukSkirtModel::getTexturedModelData);
    }
}
