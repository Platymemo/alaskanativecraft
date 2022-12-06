package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;

public abstract class AbstractPhase implements Phase {
	protected final WhaleEntity whale;

	protected AbstractPhase(WhaleEntity whale) {
		this.whale = whale;
	}

	@Override
	public boolean combat() {
		return this.whale.getHealth() < this.whale.getMaxHealth();
	}

	@Override
	public boolean isAttacking() {
		return false;
	}
}
