package com.github.platymemo.alaskanativecraft.entity.boss.whale.part;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class WhaleLimbEntityPart extends WhaleEntityPart {
	// TODO make configurable
	private static final int SLOW_DURATION_MULTIPLIER = 10;

	public WhaleLimbEntityPart(WhaleEntity owner, float width, float height) {
		super(owner, width, height);
	}

	/**
	 * Slows the whale if damaged.
	 */
	@Override
	public void onDamaged(DamageSource source, float amount) {
		this.getOwner().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 0, ((int) (amount + 0.5)) * SLOW_DURATION_MULTIPLIER));
	}
}
