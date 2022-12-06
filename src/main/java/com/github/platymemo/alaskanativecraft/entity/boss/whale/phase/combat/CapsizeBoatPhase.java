package com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.combat;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.Phase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.PhaseType;

import net.minecraft.entity.ai.goal.DiveJumpingGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;

public class CapsizeBoatPhase extends CombatPhase {
	private final Goal capsizeGoal;

	public CapsizeBoatPhase(WhaleEntity whale) {
		super(whale);

		this.capsizeGoal = new CapsizeBoatGoal(whale);
	}

	@Override
	public boolean isAttacking() {
		return true;
	}

	@Override
	public void clientTick() {}

	/**
	 * Switches to running away or attacking the player if they have been dismounted from their boat.
	 */
	@Override
	public void serverTick() {
		if (this.whale.getTarget() != null && !(this.whale.getTarget().getVehicle() instanceof BoatEntity)) {
			this.whale.getPhaseManager().setPhase(this.whale.getRandom().nextFloat() < 0.3f ? PhaseType.RUN_AWAY : PhaseType.STRAFE_PLAYER);
		}
	}

	@Override
	public void beginPhase() {
		super.beginPhase();
		this.whale.getGoalSelector().add(4, this.capsizeGoal);
	}

	@Override
	public void endPhase() {
		super.endPhase();
		this.whale.getGoalSelector().remove(this.capsizeGoal);
	}

	@Override
	public PhaseType<? extends Phase> getType() {
		return PhaseType.CAPSIZE_BOAT;
	}

	/**
	 * Allows the whale to jump out of the water and break nearby boats with the impact.
	 */
	static class CapsizeBoatGoal extends DiveJumpingGoal {
		private static final int[] OFFSET_MULTIPLIERS = new int[]{0, 1, 4, 5, 6, 7};
		private final WhaleEntity whale;
		private boolean inWater;

		CapsizeBoatGoal(WhaleEntity whale) {
			this.whale = whale;
		}

		@Override
		public boolean canStart() {
			if (this.whale.getTarget() == null || !(this.whale.getTarget().getVehicle() instanceof BoatEntity)) {
				return false;
			}

			Direction direction = this.whale.getMovementDirection();
			int i = direction.getOffsetX();
			int j = direction.getOffsetZ();
			BlockPos blockPos = this.whale.getBlockPos();

			for (int k : OFFSET_MULTIPLIERS) {
				if (!this.isWater(blockPos, i, j, k) || !this.isAirAbove(blockPos, i, j, k)) {
					return false;
				}
			}

			return true;
		}

		private boolean isWater(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
			BlockPos blockPos = pos.add(offsetX * multiplier, 0, offsetZ * multiplier);
			return this.whale.world.getFluidState(blockPos).isIn(FluidTags.WATER) && !this.whale.world.getBlockState(blockPos).getMaterial().blocksMovement();
		}

		private boolean isAirAbove(BlockPos pos, int offsetX, int offsetZ, int multiplier) {
			return this.whale.world.getBlockState(pos.add(offsetX * multiplier, 1, offsetZ * multiplier)).isAir()
					&& this.whale.world.getBlockState(pos.add(offsetX * multiplier, 2, offsetZ * multiplier)).isAir();
		}

		@Override
		public boolean shouldContinue() {
			double d = this.whale.getVelocity().y;
			return (!(d * d < 0.03F) || this.whale.getPitch() == 0.0F || !(Math.abs(this.whale.getPitch()) < 10.0F) || !this.whale.isTouchingWater())
					&& !this.whale.isOnGround();
		}

		@Override
		public boolean canStop() {
			return false;
		}

		@Override
		public void start() {
			Direction direction = this.whale.getMovementDirection();
			this.whale.setVelocity(this.whale.getVelocity().add(direction.getOffsetX() * 0.6, 0.7, direction.getOffsetZ() * 0.6));
			this.whale.getNavigation().stop();
		}

		@Override
		public void stop() {
			this.whale.setPitch(0.0F);
		}

		@Override
		public void tick() {
			boolean wasInWater = this.inWater;
			if (!wasInWater) {
				FluidState fluidState = this.whale.world.getFluidState(this.whale.getBlockPos());
				this.inWater = fluidState.isIn(FluidTags.WATER);
			}

			if (this.inWater && !wasInWater) {
				for (var boat: this.whale.world.getNonSpectatingEntities(BoatEntity.class, this.whale.getBoundingBox())) {
					if (!boat.world.isClient && !boat.isRemoved()) {
						boat.kill();
						if (boat.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
							for (int i = 0; i < 3; ++i) {
								boat.dropItem(boat.getBoatType().getBaseBlock());
							}

							for (int i = 0; i < 2; ++i) {
								boat.dropItem(Items.STICK);
							}
						}
					}
				}

				this.whale.playSound(SoundEvents.ENTITY_DOLPHIN_JUMP, 1.0F, 1.0F);
			}

			Vec3d velocity = this.whale.getVelocity();
			if (velocity.y * velocity.y < 0.03F && this.whale.getPitch() != 0.0F) {
				this.whale.setPitch(MathHelper.lerpAngleDegrees(0.2F, this.whale.getPitch(), 0.0F));
			} else if (velocity.length() > 1.0E-5F) {
				double horizontalLength = velocity.horizontalLength();
				float pitch = (float) Math.atan2(-velocity.y, horizontalLength) * MathHelper.DEGREES_PER_RADIAN;
				this.whale.setPitch(pitch);
			}
		}
	}
}
