package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.entity.ai.goal.GroundFoodMateGoal;
import com.github.platymemo.alaskanativecraft.sound.AlaskaSoundEvents;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.AmphibiousPathNodeMaker;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class SealEntity extends AnimalEntity {
	public SealEntity(EntityType<? extends SealEntity> entityType, World world) {
		super(entityType, world);
		this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
		this.moveControl = new SealEntity.SealMoveControl(this);
		this.stepHeight = 1.0F;
	}

	@SuppressWarnings({"deprecation", "unused"})
	public static <T extends Entity> boolean canSpawn(EntityType<T> entityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, RandomGenerator randomGenerator) {
		return blockPos.getY() < serverWorldAccess.getSeaLevel() + 2 && blockPos.getY() > serverWorldAccess.getSeaLevel() - 10 && serverWorldAccess.getBaseLightLevel(blockPos, 0) > 8;
	}

	public static DefaultAttributeContainer.Builder createSealAttributes() {
		return SealEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.75D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5D);
	}

	@Override
	public boolean canSpawn(WorldView world) {
		return world.doesNotIntersectEntities(this);
	}

	@Override
	@Nullable
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new FleeEntityGoal<>(this, PolarBearEntity.class, 8.0F, 1.0D, 1.5D));
		this.goalSelector.add(0, new SealEntity.SealEscapeDangerGoal(this, 1.5D));
		this.goalSelector.add(1, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(2, new TemptGoal(this, 1.1D, Ingredient.ofTag(AlaskaTags.SEAL_FOOD), true));
		this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 16.0F, 1.0D, 1.5D));
		this.goalSelector.add(3, new SwimAroundGoal(this, this.isBaby() ? 2.0D : 1.0D, 40));
		this.goalSelector.add(5, new SealEntity.WanderOnLandGoal(this, 1.0D, 100));
		this.goalSelector.add(5, new GroundFoodMateGoal(this));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(7, new SealEntity.HuntFishGoal(this, 1.2D, true));
		this.targetSelector.add(0, new TargetGoal<>(this, SalmonEntity.class, true));
		this.targetSelector.add(0, new TargetGoal<>(this, CodEntity.class, true));
		this.targetSelector.add(1, new TargetGoal<>(this, SquidEntity.class, true));
	}

	@Override
	public boolean isPushedByFluids() {
		return false;
	}

	@Override
	public boolean canBreatheInWater() {
		return true;
	}

	@Override
	public EntityGroup getGroup() {
		return EntityGroup.AQUATIC;
	}

	@Override
	public int getMinAmbientSoundDelay() {
		return 200;
	}

	@Override
	@Nullable
	protected SoundEvent getAmbientSound() {
		if (!this.isTouchingWater() && this.onGround) {
			return this.isBaby() ? AlaskaSoundEvents.ENTITY_SEAL_AMBIENT_BABY : AlaskaSoundEvents.ENTITY_SEAL_AMBIENT;
		}

		return super.getAmbientSound();
	}

	@Override
	protected void playSwimSound(float volume) {
		super.playSwimSound(volume * 1.5F);
	}

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_TURTLE_SWIM;
	}

	@Override
	@Nullable
	protected SoundEvent getHurtSound(DamageSource source) {
		return AlaskaSoundEvents.ENTITY_SEAL_HURT;
	}

	@Override
	@Nullable
	protected SoundEvent getDeathSound() {
		return AlaskaSoundEvents.ENTITY_SEAL_HURT;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		SoundEvent soundEvent = this.isBaby() ? SoundEvents.ENTITY_TURTLE_SHAMBLE_BABY : SoundEvents.ENTITY_TURTLE_SHAMBLE;
		this.playSound(soundEvent, 0.15F, 1.0F);
	}

	@Override
	protected float calculateNextStepSoundDistance() {
		return this.distanceTraveled + 0.15F;
	}

	@Override
	public float getScaleFactor() {
		return this.isBaby() ? 0.3F : 1.0F;
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return new SealEntity.SealSwimNavigation(this, world);
	}

	@Override
	@Nullable
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return AlaskaEntities.HARP_SEAL.create(world);
	}

	@Override
	public boolean isBreedingItem(@NotNull ItemStack stack) {
		return stack.isIn(AlaskaTags.SEAL_FOOD);
	}

	@Override
	public float getPathfindingFavor(BlockPos pos, @NotNull WorldView world) {
		if (world.getFluidState(pos).isIn(FluidTags.WATER)) {
			return 15.0F;
		} else {
			return world.getBlockState(pos).isOf(Blocks.SAND) ? 10.0F : world.getLightLevel(pos) - 0.5F;
		}
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (this.canMoveVoluntarily() && this.isTouchingWater()) {
			this.updateVelocity(0.1F, movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9D));
			if (this.getTarget() == null) {
				this.setVelocity(this.getVelocity().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public boolean tryAttack(@NotNull Entity target) {
		boolean bl = target.damage(DamageSource.mob(this), (float) ((int) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
		if (bl) {
			this.applyDamageEffects(this, target);
		}

		return bl;
	}

	static class SealSwimNavigation extends SwimNavigation {
		SealSwimNavigation(SealEntity owner, World world) {
			super(owner, world);
		}

		@Override
		protected boolean isAtValidPosition() {
			return true;
		}

		@Override
		protected PathNodeNavigator createPathNodeNavigator(int range) {
			this.nodeMaker = new AmphibiousPathNodeMaker(false);
			return new PathNodeNavigator(this.nodeMaker, range);
		}

		@Override
		public boolean isValidPosition(BlockPos pos) {
			return !this.world.getBlockState(pos.down()).isAir();
		}
	}

	static class SealMoveControl extends MoveControl {
		private final SealEntity seal;

		SealMoveControl(SealEntity seal) {
			super(seal);
			this.seal = seal;
		}

		private void updateVelocity() {
			if (this.seal.isTouchingWater()) {
				this.seal.setVelocity(this.seal.getVelocity().add(0.0D, 0.005D, 0.0D));
				this.seal.setMovementSpeed(Math.max(this.seal.getMovementSpeed() / 1.5F, 0.1F));

				if (this.seal.isBaby()) {
					this.seal.setMovementSpeed(Math.max(this.seal.getMovementSpeed() / 2.0F, 0.08F));
				}
			} else if (this.seal.onGround) {
				this.seal.setMovementSpeed(Math.max(this.seal.getMovementSpeed() / 1.5F, 0.08F));
			}
		}

		@Override
		public void tick() {
			this.updateVelocity();
			if (this.state == MoveControl.State.MOVE_TO && !this.seal.getNavigation().isIdle()) {
				double d = this.targetX - this.seal.getX();
				double e = this.targetY - this.seal.getY();
				double f = this.targetZ - this.seal.getZ();
				double g = Math.sqrt(d * d + e * e + f * f);
				e /= g;
				float h = (float) (MathHelper.atan2(f, d) * 57.2957763671875D) - 90.0F;
				this.seal.setYaw(this.wrapDegrees(this.seal.getYaw(), h, 90.0F));
				this.seal.bodyYaw = this.seal.getYaw();
				this.seal.headYaw = this.seal.getYaw();
				float i = (float) (this.speed * this.seal.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
				this.seal.setMovementSpeed(MathHelper.lerp(0.125F, this.seal.getMovementSpeed(), i));
				this.seal.setVelocity(this.seal.getVelocity().add(0.0D, (double) this.seal.getMovementSpeed() * e * 0.1D, 0.0D));
			} else {
				this.seal.setMovementSpeed(0.0F);
			}
		}
	}

	static class WanderOnLandGoal extends WanderAroundGoal {
		private WanderOnLandGoal(SealEntity seal, double speed, int chance) {
			super(seal, speed, chance);
		}

		@Override
		public boolean canStart() {
			return !this.mob.isTouchingWater() && super.canStart();
		}
	}

	static class HuntFishGoal extends MeleeAttackGoal {
		private final AnimalEntity animal;

		HuntFishGoal(SealEntity sealEntity, double speed, boolean pauseWhenMobIdle) {
			super(sealEntity, speed, pauseWhenMobIdle);
			this.animal = sealEntity;
		}

		@Override
		public boolean canStart() {
			if (!AlaskaConfig.getConfig().sealFishing.sealsHuntFish) {
				return false;
			}

			if (this.animal.getRandom().nextInt(1000) != 0 || !this.animal.canEat()) {
				return false;
			}

			return super.canStart();
		}
	}

	static class SealEscapeDangerGoal extends EscapeDangerGoal {
		SealEscapeDangerGoal(SealEntity seal, double speed) {
			super(seal, speed);
		}

		@Override
		public boolean canStart() {
			if (this.mob.getAttacker() == null && !this.mob.isOnFire()) {
				return false;
			} else {
				BlockPos blockPos = this.locateClosestWater(this.mob.world, this.mob, 5);
				if (blockPos != null) {
					this.targetX = blockPos.getX();
					this.targetY = blockPos.getY();
					this.targetZ = blockPos.getZ();
					return true;
				} else {
					return this.findTarget();
				}
			}
		}
	}
}
