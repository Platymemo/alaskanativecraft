/*
 * Copied wholesale, with express permission,
 * from Haven King's TinyTweaks mod, go check it out!
 */

package com.github.platymemo.alaskanativecraft.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;

import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.entity.SealEntity;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.GameRules;

public class GroundFoodMateGoal extends Goal {
	protected final AnimalEntity animal;
	protected ItemEntity foodEntity;

	public GroundFoodMateGoal(AnimalEntity animal) {
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
		this.animal = animal;
	}

	@Override
	public boolean canStart() {
		if (this.animal instanceof SealEntity && !AlaskaConfig.getConfig().sealFishing.sealsEatHuntedFish) {
			return false;
		}

		if (this.animal.getRandom().nextInt(100) != 0) {
			return false;
		}

		if (this.animal.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && this.animal.canEat() && this.animal.getBreedingAge() == 0) {
			this.foodEntity = this.findFood();
		}

		return this.foodEntity != null;
	}

	@Override
	public boolean shouldContinue() {
		return this.foodEntity != null && this.foodEntity.getStack().getCount() > 0 && this.animal.canEat() && this.animal.getBreedingAge() == 0;
	}

	@Override
	public void stop() {
		this.foodEntity = null;
	}

	@Override
	public void tick() {
		if (this.foodEntity != null) {
			this.animal.getLookControl().lookAt(this.foodEntity, 10.0F, this.animal.getLookPitchSpeed());
			this.animal.getNavigation().startMovingTo(this.foodEntity, 1.0f);
			if (this.animal.squaredDistanceTo(this.foodEntity) < 4.0D) {
				this.feed();
			}
		}
	}

	private @Nullable ItemEntity findFood() {
		List<ItemEntity> list = this.animal.getWorld().getEntitiesByClass(ItemEntity.class, this.animal.getBoundingBox().expand(8.0D), (entity) -> true);
		double distance = Double.MAX_VALUE;

		ItemEntity result = null;
		for (ItemEntity itemEntity : list) {
			if (this.animal.isBreedingItem(itemEntity.getStack()) && this.animal.squaredDistanceTo(itemEntity) < distance) {
				result = itemEntity;
				distance = this.animal.squaredDistanceTo(itemEntity);
			}
		}

		return result;
	}

	private void feed() {
		if (this.foodEntity.getStack().getCount() > 0) {
			this.foodEntity.getStack().decrement(1);
			if (!(this.animal instanceof SealEntity) || AlaskaConfig.getConfig().sealFishing.sealsBreedFromHuntedFish) {
				this.animal.lovePlayer(null);
			}
		}

		this.stop();
	}
}
