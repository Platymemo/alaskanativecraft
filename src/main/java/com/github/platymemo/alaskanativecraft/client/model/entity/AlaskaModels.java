package com.github.platymemo.alaskanativecraft.client.model.entity;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.client.model.entity.feature.KuspukSkirtModel;
import com.github.platymemo.alaskanativecraft.client.model.entity.feature.SnowshoeModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
	public static final EntityModelLayer SNOWSHOES = registerMain("snowshoes");

	@Contract("_ -> new")
	private static @NotNull EntityModelLayer registerMain(String id) {
		return new EntityModelLayer(new Identifier(AlaskaNativeCraft.MOD_ID, id), "main");
	}

	@Environment(EnvType.CLIENT)
	public static void registerEntityModels() {
		EntityModelLayerRegistry.registerModelLayer(PTARMIGAN, PtarmiganEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(DOGSLED, DogsledEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(HARPOON, HarpoonEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MOOSE, MooseEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SEAL, SealEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(KUSPUK_SKIRT, KuspukSkirtModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SNOWSHOES, SnowshoeModel::getTexturedModelData);
	}
}
