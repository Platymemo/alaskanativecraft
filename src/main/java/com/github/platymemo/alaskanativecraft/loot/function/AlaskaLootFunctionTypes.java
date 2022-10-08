package com.github.platymemo.alaskanativecraft.loot.function;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;

import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlaskaLootFunctionTypes {
	public static final LootFunctionType SET_AKUTAQ_EFFECT = new LootFunctionType(new SetAkutaqEffectsLootFunction.Serializer());

	public static void register() {
		Registry.register(Registry.LOOT_FUNCTION_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "set_akutaq_effects"), SET_AKUTAQ_EFFECT);
	}
}
