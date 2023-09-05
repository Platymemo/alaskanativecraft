package com.github.platymemo.alaskanativecraft.entity;

import java.util.EnumSet;

import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.entity.ai.goal.AdultMeleeAttackGoal;
import com.github.platymemo.alaskanativecraft.entity.ai.goal.ChildEscapeDangerGoal;
import com.github.platymemo.alaskanativecraft.mixin.AxeItemAccessor;
import com.github.platymemo.alaskanativecraft.sound.AlaskaSoundEvents;
import com.github.platymemo.alaskanativecraft.tags.CommonTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class MooseEntity extends AnimalEntity {
	protected MooseEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
		this.calculateDimensions();
		this.setStepHeight(this.isBaby() ? 1.0F : 2.0F);
	}

	public static DefaultAttributeContainer.Builder createMooseAttributes() {
		return MooseEntity.createAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new ChildEscapeDangerGoal(this, 2.5D));
		this.goalSelector.add(1, new AdultMeleeAttackGoal(this, 2.5D, true));
		this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.WHEAT), false));
		this.goalSelector.add(4, new FollowParentGoal(this, 1.25D));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(6, new MooseEntity.EatBarkGoal(2.25D, 0.2D));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(7, new LookAroundGoal(this));
		this.targetSelector.add(0, new RevengeGoal(this).setGroupRevenge());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.random.nextInt(100) > 75 ? AlaskaSoundEvents.ENTITY_MOOSE_AMBIENT : super.getAmbientSound();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return AlaskaSoundEvents.ENTITY_MOOSE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return AlaskaSoundEvents.ENTITY_MOOSE_HURT;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
	}

	@Override
	protected float getSoundVolume() {
		return 0.2F;
	}

	@Override
	public MooseEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
		return AlaskaEntities.MOOSE.create(serverWorld);
	}

	@Override
	protected void onGrowUp() {
		super.onGrowUp();
		this.setStepHeight(2.0F);
	}

	@Override
	protected float getActiveEyeHeight(EntityPose pose, @NotNull EntityDimensions dimensions) {
		return dimensions.height * 0.95F;
	}

	class EatBarkGoal extends Goal {
		private final double distance;
		private final double speed;
		protected BlockPos logPos;
		protected BlockState logState;
		protected boolean logValid;
		private Vec3d target;

		EatBarkGoal(double distance, double speed) {
			this.logPos = BlockPos.ORIGIN;
			this.distance = distance;
			this.speed = speed;
			this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
		}

		protected void setLogStripped() {
			World world = MooseEntity.this.getWorld();
			if (this.logValid) {
				this.logState = world.getBlockState(this.logPos);
				if (this.logState.isIn(CommonTags.LOGS_WITH_BARK)) {
					BlockState blockState = world.getBlockState(this.logPos);
					Block block = AxeItemAccessor.getStrippedBlocks().get(blockState.getBlock());
					if (block != null && !world.isClient) {
						world.setBlockState(this.logPos, block.getDefaultState().with(PillarBlock.AXIS, blockState.get(PillarBlock.AXIS)), 11);
					}
				}
			}
		}

		protected boolean targetLogPos() {
			Vec3d vec3d = this.locateLogPos();
			if (vec3d == null) {
				this.logValid = false;
				this.target = Vec3d.ZERO;
				this.logPos = BlockPos.ORIGIN;
				return false;
			} else {
				this.logValid = true;
				this.target = vec3d;
				this.logPos = BlockPos.create(vec3d.x, vec3d.y, vec3d.z);
				return true;
			}
		}

		@Nullable
		protected Vec3d locateLogPos() {
			Iterable<BlockPos> iterable = BlockPos.iterate(
					MooseEntity.this.getBlockX() - 3,
					MooseEntity.this.getBlockY() - 1,
					MooseEntity.this.getBlockZ() - 3,
					MooseEntity.this.getBlockX() + 3,
					MooseEntity.this.getBlockY() + 4,
					MooseEntity.this.getBlockZ() + 3
			);

			for (BlockPos blockPos : iterable) {
				if (MooseEntity.this.getWorld().getBlockState(blockPos).isIn(CommonTags.LOGS_WITH_BARK)) {
					return Vec3d.ofBottomCenter(blockPos);
				}
			}

			return null;
		}

		@Override
		public boolean canStart() {
			if (!AlaskaConfig.getConfig().mooseEatBark) {
				return false;
			}

			if (!MooseEntity.this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
					|| !NavigationConditions.hasMobNavigation(MooseEntity.this)
					|| MooseEntity.this.getTarget() != null
					|| MooseEntity.this.isBaby()
					|| MooseEntity.this.getRandom().nextInt(200) != 0) {
				return false;
			} else {
				return this.targetLogPos();
			}
		}

		@Override
		public boolean shouldContinue() {
			return !MooseEntity.this.getNavigation().isIdle();
		}

		@Override
		public void start() {
			MooseEntity.this.getNavigation().startMovingTo(this.target.x, this.target.y, this.target.z, this.speed);
		}

		@Override
		public void tick() {
			double h = MooseEntity.this.getPos().distanceTo(this.target);
			if (h < this.distance) {
				MooseEntity.this.getLookControl().lookAt(this.target);
			}
		}

		@Override
		public void stop() {
			this.setLogStripped();
			MooseEntity.this.getNavigation().stop();
		}
	}
}
