package com.github.platymemo.alaskanativecraft.loot.function;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;

import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AlaskaLootFunctionTypes {
	public static final LootFunctionType SET_AKUTAQ_EFFECT = new LootFunctionType(new SetAkutaqEffectsLootFunction.Serializer());

	public static void register() {
		Registry.register(Registries.LOOK_FUNCTION_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "set_akutaq_effects"), SET_AKUTAQ_EFFECT);
	}
}
