package com.github.platymemo.alaskanativecraft.entity.ai.goal;

import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class ChildEscapeDangerGoal extends EscapeDangerGoal {
	public ChildEscapeDangerGoal(PathAwareEntity mob, double speed) {
		super(mob, speed);
	}

	@Override
	public boolean canStart() {
		if (mob.isBaby()) {
			return super.canStart();
		} else {
			return false;
		}
	}
}
