package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.Phase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.PhaseType;

import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Makes the whale attempt to flee a fight.
 */
public class RunAwayPhase extends CombatPhase {
	private final Goal fleeGoal;
	private float damageTaken;

	public RunAwayPhase(WhaleEntity whale) {
		super(whale);
		this.fleeGoal = this.getFleeGoal();
		this.damageTaken = 0.0f;
	}

	protected Goal getFleeGoal() {
		return new FleeEntityGoal<>(whale, PlayerEntity.class, 400f, 1.5f, 2.0f);
	}

	/**
	 * Track how much damage is taken.
	 */
	@Override
	public float modifyDamageTaken(DamageSource damageSource, float damage) {
		this.damageTaken += damage;
		return damage;
	}

	@Override
	public void clientTick() {}

	/**
	 * Check if the whale has escaped 160 blocks away.
	 * If so, the whale can despawn.
	 */
	@Override
	public void serverTick() {
		if (this.damageTaken > 15f) {
			this.whale.getPhaseManager().setPhase(PhaseType.STRAFE_PLAYER);
		}

		PlayerEntity maybePlayer = this.whale.world.getClosestPlayer(this.whale, 160f);

		if (maybePlayer == null) {
			this.whale.discard();
		}
	}

	@Override
	public void beginPhase() {
		super.beginPhase();
		this.whale.getGoalSelector().add(3, this.fleeGoal);
	}

	@Override
	public void endPhase() {
		super.endPhase();
		this.whale.getGoalSelector().remove(this.fleeGoal);
	}

	@Override
	public PhaseType<? extends Phase> getType() {
		return PhaseType.RUN_AWAY;
	}
}
