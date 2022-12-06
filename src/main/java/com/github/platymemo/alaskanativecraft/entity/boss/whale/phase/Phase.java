package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase;

import net.minecraft.entity.damage.DamageSource;

public interface Phase {
	/**
	 * @return {@code true} if this phase is occurring during combat, or {@code false}
	 */
	boolean combat();

	/**
	 * @return {@code true} if this phase is an attack phase, or {@code false}
	 */
	boolean isAttacking();

	void clientTick();

	void serverTick();

	void beginPhase();

	void endPhase();

	PhaseType<? extends Phase> getType();

	default float modifyDamageTaken(DamageSource damageSource, float damage) {
		return damage;
	}
}
