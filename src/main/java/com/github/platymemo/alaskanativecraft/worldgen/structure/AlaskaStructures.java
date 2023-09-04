package com.github.platymemo.alaskanativecraft.worldgen.structure;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructureType;
import net.minecraft.util.Identifier;

public class AlaskaStructures {
	public static StructureType<FishCampStructure> FISH_CAMP = Registry.register(Registries.STRUCTURE_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "fish_camp"), () -> FishCampStructure.CODEC);
}
