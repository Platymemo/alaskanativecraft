package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.AbstractPhase;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Represents a whale phase after it has been attacked.
 */
public abstract class CombatPhase extends AbstractPhase {
	private final Goal targetGoal;

	protected CombatPhase(WhaleEntity whale) {
		super(whale);
		this.targetGoal = new TargetGoal<>(whale, PlayerEntity.class, false, true);
	}

	@Override
	public void beginPhase() {
		this.whale.getTargetSelector().add(0, this.targetGoal);
	}

	@Override
	public void endPhase() {
		this.whale.getTargetSelector().remove(this.targetGoal);
	}

	@Override
	public boolean combat() {
		return true;
	}
}
