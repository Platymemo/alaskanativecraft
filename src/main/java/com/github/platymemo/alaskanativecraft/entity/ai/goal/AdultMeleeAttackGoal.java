package com.github.platymemo.alaskanativecraft.entity.ai.goal;

import com.github.platymemo.alaskanativecraft.mixin.MeleeAttackGoalAccessor;
import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Hand;

public class AdultMeleeAttackGoal extends MeleeAttackGoal {
	public AdultMeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
		super(mob, speed, pauseWhenMobIdle);
	}

	@Override
	public boolean canStart() {
		if (!mob.isBaby()) {
			return super.canStart();
		} else {
			return false;
		}
	}

	@Override
	protected void resetCooldown() {
		((MeleeAttackGoalAccessor) this).setCooldown(50);
	}

	@Override
	protected double getSquaredMaxAttackDistance(@NotNull LivingEntity entity) {
		float f = (mob.getWidth() - 1.0F) * 2.0F;
		return (f * f + entity.getWidth());
	}

	@Override
	protected void attack(LivingEntity target, double squaredDistance) {
		double d = this.getSquaredMaxAttackDistance(target);
		if (squaredDistance <= d && this.isCooledDown()) {
			this.resetCooldown();
			this.mob.swingHand(Hand.MAIN_HAND);
			if (this.mob.tryAttack(target) && this.mob.getRandom().nextFloat() < 0.6F) {
				this.stop();
			}
		}
	}
}
