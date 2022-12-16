package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class PhaseManager {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final WhaleEntity whale;
	private final Phase[] phases = new Phase[PhaseType.count()];
	@Nullable
	private Phase current;

	public PhaseManager(WhaleEntity whale) {
		this.whale = whale;
		this.setPhase(PhaseType.ROAM);
	}

	public void setPhase(PhaseType<?> type) {
		if (this.current != null && type == this.current.getType()) {
			return;
		}

		if (this.current != null) {
			this.current.endPhase();
		}

		this.current = this.create(type);
		if (!this.whale.world.isClient) {
			this.whale.getDataTracker().set(WhaleEntity.PHASE_TYPE, type.getTypeId());
		}

		LOGGER.debug("Whale is now in phase {} on the {}", type, this.whale.world.isClient ? "client" : "server");
		this.current.beginPhase();
	}

	public Phase getCurrent() {
		if (this.current == null) {
			this.setPhase(PhaseType.ROAM);
		}

		return this.current;
	}

	public <T extends Phase> T create(PhaseType<T> type) {
		int i = type.getTypeId();
		if (this.phases[i] == null) {
			this.phases[i] = type.create(this.whale);
		}

		return (T) this.phases[i];
	}

	public void beginCombat() {
		if (this.whale.getRandom().nextFloat() < 01F) {
			this.setPhase(PhaseType.RUN_AWAY);
		} else {
			this.setPhase(PhaseType.STRAFE_PLAYER);
		}
	}
}
