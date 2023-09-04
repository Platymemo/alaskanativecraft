package com.github.platymemo.alaskanativecraft.mixin;

import java.util.EnumMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;

@Mixin(ArmorMaterials.class)
public interface ArmorMaterialsAccessor {
	@Accessor("BASE_DURABILITY_VALUES")
	static EnumMap<ArmorItem.ArmorSlot, Integer> getBaseDurabilityValues() {
		throw new AssertionError("Accessor dummy method called somehow!");
	}
}
