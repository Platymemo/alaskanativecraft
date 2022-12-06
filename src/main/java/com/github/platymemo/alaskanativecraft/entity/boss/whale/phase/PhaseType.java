package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat.CapsizeBoatPhase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat.LimpAwayPhase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat.RunAwayPhase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat.StrafePlayerPhase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.passive.RoamPhase;

public class PhaseType<T extends Phase> {
	private static PhaseType<?>[] types = new PhaseType[0];
	public static final PhaseType<RoamPhase> ROAM = register(RoamPhase.class, "RoamPhase");
	public static final PhaseType<StrafePlayerPhase> STRAFE_PLAYER = register(StrafePlayerPhase.class, "StrafePlayer");
	public static final PhaseType<CapsizeBoatPhase> CAPSIZE_BOAT = register(CapsizeBoatPhase.class, "CapsizeBoat");
	public static final PhaseType<LimpAwayPhase> LIMP_AWAY = register(LimpAwayPhase.class, "LimpAway");
	public static final PhaseType<RunAwayPhase> RUN_AWAY = register(RunAwayPhase.class, "RunAway");
	private final Class<? extends Phase> phaseClass;
	private final int id;
	private final String name;

	private PhaseType(int i, Class<? extends Phase> clazz, String string) {
		this.id = i;
		this.phaseClass = clazz;
		this.name = string;
	}

	public Phase create(WhaleEntity whale) {
		try {
			Constructor<? extends Phase> constructor = this.getConstructor();
			return constructor.newInstance(whale);
		} catch (Exception exception) {
			throw new Error(exception);
		}
	}

	protected Constructor<? extends Phase> getConstructor() throws NoSuchMethodException {
		return this.phaseClass.getConstructor(WhaleEntity.class);
	}

	public int getTypeId() {
		return this.id;
	}

	public String toString() {
		return this.name + " (#" + this.id + ")";
	}

	public static PhaseType<?> getFromId(int id) {
		return id >= 0 && id < types.length ? types[id] : ROAM;
	}

	public static int count() {
		return types.length;
	}

	private static <T extends Phase> PhaseType<T> register(Class<T> phaseClass, String name) {
		PhaseType<T> type = new PhaseType<>(types.length, phaseClass, name);
		types = Arrays.copyOf(types, types.length + 1);
		types[type.getTypeId()] = type;
		return type;
	}
}
