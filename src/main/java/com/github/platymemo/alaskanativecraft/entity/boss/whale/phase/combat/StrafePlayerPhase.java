package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.Phase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.PhaseType;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;

public class StrafePlayerPhase extends CombatPhase {
	private final Goal meleeGoal;

	public StrafePlayerPhase(WhaleEntity whale) {
		super(whale);
		this.meleeGoal = new MeleeAttackGoal(whale, 1.2F, true);
	}

	@Override
	public void clientTick() {}

	/**
	 * Switch to attempting to capsize the player's boat if they are on one.
	 */
	@Override
	public void serverTick() {
		if (this.whale.getTarget() instanceof PlayerEntity player && player.getVehicle() instanceof BoatEntity boat && this.whale.squaredDistanceTo(boat) < 169f) {
			this.whale.getPhaseManager().setPhase(PhaseType.CAPSIZE_BOAT);
		}
	}

	@Override
	public void beginPhase() {
		super.beginPhase();
		this.whale.getGoalSelector().add(4, this.meleeGoal);
	}

	@Override
	public void endPhase() {
		super.endPhase();
		this.whale.getGoalSelector().remove(this.meleeGoal);
	}

	@Override
	public boolean isAttacking() {
		return true;
	}

	@Override
	public PhaseType<? extends Phase> getType() {
		return PhaseType.STRAFE_PLAYER;
	}
}
