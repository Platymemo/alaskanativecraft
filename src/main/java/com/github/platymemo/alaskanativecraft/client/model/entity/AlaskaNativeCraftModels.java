package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AlaskaNativeCraftModels {
    public static final EntityModelLayer PTARMIGAN = registerMain("ptarmigan");
    public static final EntityModelLayer DOGSLED = registerMain("dogsled");
    public static final EntityModelLayer HARPOON = registerMain("harpoon");
    public static final EntityModelLayer MOOSE = registerMain("moose");
    public static final EntityModelLayer SEAL = registerMain("seal");
    public static final EntityModelLayer KUSPUK_SKIRT = registerMain("kuspuk_skirt");

    private static EntityModelLayer registerMain(String id) {
        return new EntityModelLayer(new Identifier(AlaskaNativeCraft.MOD_ID, id), "main");
    }
}
