package com.github.platymemo.alaskanativecraft.item;

import com.github.platymemo.alaskanativecraft.entity.effect.AlaskaEffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AlaskaPotions {
    public static final Potion TUNDRA_TEA = Registry.register(Registries.POTION, "tundra_tea", new Potion(new StatusEffectInstance(AlaskaEffects.MEDICINAL, 1200)));
    public static final Potion LONG_TUNDRA_TEA = Registry.register(Registries.POTION, "long_tundra_tea", new Potion(new StatusEffectInstance(AlaskaEffects.MEDICINAL, 2400)));
    public static final Potion STRONG_TUNDRA_TEA = Registry.register(Registries.POTION, "strong_tundra_tea", new Potion(new StatusEffectInstance(AlaskaEffects.MEDICINAL, 600, 1)));
}
