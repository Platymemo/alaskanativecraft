package com.github.platymemo.alaskanativecraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;

@Mixin(MeleeAttackGoal.class)
public interface MeleeAttackGoalAccessor {
	@Accessor("cooldown")
	void setCooldown(int cooldown);
}
