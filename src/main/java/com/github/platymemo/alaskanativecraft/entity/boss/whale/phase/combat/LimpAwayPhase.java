package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.Phase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.PhaseType;

import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Allows the whale to constantly flee a fight, at the cost of taking double damage and a slower fleeing speed.
 */
public class LimpAwayPhase extends RunAwayPhase {
	public LimpAwayPhase(WhaleEntity whale) {
		super(whale);
	}

	@Override
	protected Goal getFleeGoal() {
		return new FleeEntityGoal<>(whale, PlayerEntity.class, 400f, 1.0f, 1.5f);
	}

	@Override
	public float modifyDamageTaken(DamageSource damageSource, float damage) {
		return damage * 2;
	}

	@Override
	public PhaseType<? extends Phase> getType() {
		return PhaseType.LIMP_AWAY;
	}
}
