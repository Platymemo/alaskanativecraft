package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.passive;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.AbstractPhase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.Phase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.PhaseType;

/**
 * Represents a peaceful whale phase.
 */
public class RoamPhase extends AbstractPhase {
	public RoamPhase(WhaleEntity whale) {
		super(whale);
	}

	@Override
	public boolean combat() {
		return false;
	}

	@Override
	public void clientTick() {}

	@Override
	public void serverTick() {}

	@Override
	public void beginPhase() {}

	@Override
	public void endPhase() {}

	@Override
	public PhaseType<? extends Phase> getType() {
		return PhaseType.ROAM;
	}
}
