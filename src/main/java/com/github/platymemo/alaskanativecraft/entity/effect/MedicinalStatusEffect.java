package com.github.platymemo.alaskanativecraft.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class MedicinalStatusEffect extends StatusEffect {
    protected MedicinalStatusEffect(StatusEffectType category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // The medicinal effect removes other effects,
        // It cannot do that at the regular time otherwise it will result in a CME
        // Therefore applyUpdateEffect will not run in the regular loop
        return false;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // remove first negative status effect found
        for (StatusEffect type : entity.getActiveStatusEffects().keySet()) {
            if (type.getType() == StatusEffectType.HARMFUL) {
                entity.removeStatusEffect(type);
                break;
            }
        }
    }
}
