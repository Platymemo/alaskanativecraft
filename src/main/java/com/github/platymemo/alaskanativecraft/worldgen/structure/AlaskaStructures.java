package com.github.platymemo.alaskanativecraft.worldgen.structure;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import net.minecraft.structure.StructureType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlaskaStructures {
    public static StructureType<FishCampStructure> FISH_CAMP = Registry.register(Registry.STRUCTURE_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "fish_camp"), () -> FishCampStructure.CODEC);
}
