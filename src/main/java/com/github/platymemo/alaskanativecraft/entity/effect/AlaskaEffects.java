package com.github.platymemo.alaskanativecraft.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AlaskaEffects {
    public static StatusEffect MEDICINAL =
            Registry.register(
                    Registries.STATUS_EFFECT,
                    "medicinal",
                    new MedicinalStatusEffect(StatusEffectType.BENEFICIAL, 0x929D2A)
            );
}
