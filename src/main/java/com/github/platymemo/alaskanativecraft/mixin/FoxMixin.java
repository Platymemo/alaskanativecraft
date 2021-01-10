package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.PtarmiganEntity;
import com.github.platymemo.alaskanativecraft.entity.SealEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxEntity.class)
public abstract class FoxMixin extends AnimalEntity {

	@Shadow
	private Goal followChickenAndRabbitGoal;

	protected FoxMixin(EntityType<? extends AnimalEntity> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("TAIL"), method = "initGoals()V")
	private void addPtarmiganTarget(CallbackInfo info) {
		this.followChickenAndRabbitGoal = new FollowTargetGoal(this, AnimalEntity.class, 10, false, false, (livingEntity) -> {
			return livingEntity instanceof ChickenEntity || livingEntity instanceof RabbitEntity || livingEntity instanceof PtarmiganEntity;
		});
	}
}
