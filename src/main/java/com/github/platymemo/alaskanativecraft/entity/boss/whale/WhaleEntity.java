package com.github.platymemo.alaskanativecraft.entity.boss.whale;

import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.part.WhaleEntityPart;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.part.WhaleHeadEntityPart;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.part.WhaleLimbEntityPart;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.Phase;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.PhaseManager;
import com.github.platymemo.alaskanativecraft.entity.boss.whale.phase.PhaseType;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.AquaticLookControl;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.goal.BreatheAirGoal;
import net.minecraft.entity.ai.goal.ChaseBoatGoal;
import net.minecraft.entity.ai.goal.DiveJumpingGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MoveIntoWaterGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import org.quiltmc.qsl.entity.multipart.api.EntityPart;
import org.quiltmc.qsl.entity.multipart.api.MultipartEntity;

public class WhaleEntity extends WaterCreatureEntity implements MultipartEntity {
	private static final TrackedData<Integer> MOISTNESS = DataTracker.registerData(WhaleEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Integer> PHASE_TYPE = DataTracker.registerData(WhaleEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final int TOTAL_AIR_SUPPLY = 4800;
	private static final int MAX_MOISTNESS = 4800;
	private final ServerBossBar bossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS);
	private final PhaseManager phaseManager;
	private float damageTakenWhileAttacking;
	private int stunTicks;
	private final WhaleEntityPart[] parts;
	private final WhaleEntityPart head;
	private final WhaleEntityPart chest;
	private final WhaleEntityPart torso;
	private final WhaleEntityPart rightFlipper;
	private final WhaleEntityPart leftFlipper;
	private final WhaleEntityPart tail;

	public WhaleEntity(EntityType<? extends WhaleEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new AquaticMoveControl(this, 35, 10, 0.02F, 0.1F, true);
		this.lookControl = new AquaticLookControl(this, 5);
		this.setHealth(this.getMaxHealth());

		// The whale is so big it should always render, so it doesn't get culled while still in view.
		this.ignoreCameraFrustum = true;

		// The whale is so large that no clip helps it maneuver reasonably.
		this.noClip = true;

		this.parts = new WhaleEntityPart[] {
				this.head = new WhaleHeadEntityPart(this, 5.0f, 5.0f),
				this.chest = new WhaleEntityPart(this, 7.5f, 5.5f),
				this.torso = new WhaleEntityPart(this, 5.0f, 5.5f),
				this.rightFlipper = new WhaleLimbEntityPart(this, 2.5f, 1.5f),
				this.leftFlipper = new WhaleLimbEntityPart(this, 2.5f, 1.5f),
				this.tail = new WhaleLimbEntityPart(this, 4.0f, 2.0f),
		};
		this.setPartPositions();

		this.phaseManager = new PhaseManager(this);
		this.bossBar.setVisible(false);
	}

	private void setPartPositions() {
		this.head.setRelativePosition(new Vec3d(0.0, 0.0, 9.0));
		this.chest.setRelativePosition(new Vec3d(0.0, 0.0, 2.5));
		this.rightFlipper.setRelativePosition(new Vec3d(-4.5, -0.5, 5.0));
		this.leftFlipper.setRelativePosition(new Vec3d(4.5, -0.5, 5.0));
		this.torso.setRelativePosition(new Vec3d(0.0, 0.0, -6.0));
		this.tail.setRelativePosition(new Vec3d(0.0, 3.5, -11.5));
	}

	/**
	 * Stun the whale, halting all movement, for the provided amount of ticks.
	 * @param duration the duration of the stun, in ticks.
	 */
	public void stun(int duration) {
		if (this.world.isClient) {
			return;
		}

		this.stunTicks = duration;
	}

	/**
	 * Determines whether this entity pushes other entities out of its bounding box.
	 * We do not want to collide as other entities should collide with the {@link WhaleEntityPart}s.
	 */
	@Override
	public boolean collides() {
		return false;
	}

	/**
	 * The conditions in which a whale can naturally spawn.
	 */
	@SuppressWarnings({"deprecation", "unused"})
	public static <T extends Entity> boolean canSpawn(EntityType<T> entityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, RandomGenerator randomGenerator) {
		return blockPos.getY() < serverWorldAccess.getSeaLevel();
	}

	/**
	 * Whales are only damaged by natural causes or harpoons.
	 */
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		if (!(source instanceof EntityDamageSource entityDamageSource) || entityDamageSource.getSource() instanceof HarpoonEntity || entityDamageSource.getAttacker() instanceof LivingEntity livingEntity && livingEntity.getMainHandStack().isIn(AlaskaTags.HARPOONS)) {
			return super.isInvulnerableTo(source);
		}

		return true;
	}

	/**
	 * We handle damage with context of entity parts.
	 * By default, we handle damage using the whale's chest.
	 */
	@Override
	public boolean damage(DamageSource source, float amount) {
		return !this.world.isClient && this.damagePart(this.chest, source, amount);
	}

	/**
	 * Handles damage received by the whale.
	 */
	public boolean damagePart(WhaleEntityPart part, DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		}

		if (!this.world.isClient && !this.phaseManager.getCurrent().combat()) {
			this.phaseManager.beginCombat();
			this.bossBar.setVisible(true);
		}

		amount = this.phaseManager.getCurrent().modifyDamageTaken(source, amount);

		if (amount < 0.01F) {
			return false;
		} else {
			if (source.getAttacker() instanceof PlayerEntity || source.isExplosive()) {
				float f = this.getHealth();
				super.damage(source, amount);

				if (this.phaseManager.getCurrent().isAttacking()) {
					this.damageTakenWhileAttacking = this.damageTakenWhileAttacking + f - this.getHealth();
					if (this.damageTakenWhileAttacking > 0.25F * this.getMaxHealth()) {
						this.damageTakenWhileAttacking = 0.0F;
						this.phaseManager.setPhase(PhaseType.RUN_AWAY);
					}
				}

				if (this.getHealth() < 0.25f * this.getMaxHealth()) {
					this.phaseManager.setPhase(PhaseType.LIMP_AWAY);
				}
			}

			part.onDamaged(source, amount);

			return true;
		}
	}

	/**
	 * Kills the whale.
	 * Overridden to bypass post-death effects.
	 */
	@Override
	public void kill() {
		this.remove(Entity.RemovalReason.KILLED);
		this.emitGameEvent(GameEvent.ENTITY_DIE);
	}

	/**
	 * Floats the dead whale to the surface, and spawns a carcass.
	 */
	@Override
	protected void updatePostDeath() {
		++this.deathTime;
		this.move(MovementType.SELF, new Vec3d(0.0, 0.1F, 0.0));

		BlockPos pos = new BlockPos(this.getPos().add(0.0, this.getDimensions(EntityPose.DYING).height / 2.0f, 0.0));
		if (!this.world.isClient() && (this.world.getBlockState(pos).isAir() || this.deathTime > 200)) {
			this.world.sendEntityStatus(this, (byte) 60);
			this.remove(Entity.RemovalReason.KILLED);
			this.spawnCarcass();
		}
	}

	/**
	 * Spawns the whale carcass that can be harvested for whale meat.
	 */
	private void spawnCarcass() {
		// TODO
	}

	/**
	 * Initializes a new entity.
	 */
	@Nullable
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		this.setAir(this.getMaxAir());
		this.setPitch(0.0F);
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}

	/**
	 * Adds the boss bar to nearby players.
	 */
	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		this.bossBar.addPlayer(player);
	}

	/**
	 * Removes the boss bar from players that leave the area.
	 */
	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		this.bossBar.removePlayer(player);
	}

	/**
	 * Mammals cannot breathe underwater.
	 */
	@Override
	public boolean canBreatheInWater() {
		return false;
	}

	/**
	 * Parent method applies the logic for fish to suffocate in air.
	 * Overridden to do nothing.
	 */
	@Override
	protected void tickWaterBreathingAir(int air) {}

	public GoalSelector getGoalSelector() {
		return this.goalSelector;
	}

	public GoalSelector getTargetSelector() {
		return this.targetSelector;
	}

	public int getMoistness() {
		return this.dataTracker.get(MOISTNESS);
	}

	public void setMoistness(int moistness) {
		this.dataTracker.set(MOISTNESS, moistness);
	}

	public int getPhaseTypeId() {
		return this.dataTracker.get(PHASE_TYPE);
	}

	public void setPhaseType(int id) {
		this.dataTracker.set(PHASE_TYPE, id);
	}

	/**
	 * Starts tracking server-client synced data.
	 */
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(MOISTNESS, MAX_MOISTNESS);
		this.dataTracker.startTracking(PHASE_TYPE, PhaseType.ROAM.getTypeId());
	}

	/**
	 * Sets the whale phase when the phase type id is changed.
	 */
	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (PHASE_TYPE.equals(data) && this.world.isClient) {
			this.phaseManager.setPhase(PhaseType.getFromId(this.getDataTracker().get(PHASE_TYPE)));
		}

		super.onTrackedDataSet(data);
	}

	/**
	 * Serializes custom data for unloading the entity.
	 */
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("Moistness", this.getMoistness());
		nbt.putInt("PhaseType", this.getPhaseTypeId());
	}

	/**
	 * Reads custom data from NBT.
	 */
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setMoistness(nbt.getInt("Moistness"));
		this.setPhaseType(nbt.getInt("PhaseType"));
	}

	/**
	 * Adds the initial goals of whales.
	 */
	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new BreatheAirGoal(this));
		this.goalSelector.add(0, new MoveIntoWaterGoal(this));
		this.goalSelector.add(4, new SwimAroundGoal(this, 1.0, 10));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(5, new BreachGoal(this, 10));
		this.goalSelector.add(8, new ChaseBoatGoal(this));
	}

	/**
	 * Creates the attributes for whales.
	 */
	public static DefaultAttributeContainer.Builder createWhaleAttributes() {
		return MobEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.2F)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0)
			.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0);
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return new SwimNavigation(this, world);
	}

	/**
	 * Attacks the target entity.
	 */
	@Override
	public boolean tryAttack(Entity target) {
		boolean bl = super.tryAttack(target);
		if (bl) {
			// TODO whale sounds
			this.playSound(SoundEvents.ENTITY_DOLPHIN_ATTACK, 1.0F, 1.0F);
		}

		return bl;
	}

	@Override
	public int getMaxAir() {
		return TOTAL_AIR_SUPPLY;
	}

	/**
	 * Handles refilling the air of an entity when it is on land.
	 */
	@Override
	protected int getNextAirOnLand(int air) {
		return this.getMaxAir();
	}

	/**
	 * @return How high up an entity's eyes are on the bounding box.
	 */
	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 3.8F;
	}

	/**
	 * How many degrees an entity can turn per tick.
	 */
	@Override
	public int getLookPitchSpeed() {
		return 1;
	}

	/**
	 * How many degrees an entity can turn per tick.
	 */
	@Override
	public int getBodyYawSpeed() {
		return 1;
	}

	/**
	 * @return {@code true} if this entity can ride the provided entity, or {@code false}
	 */
	@Override
	protected boolean canStartRiding(Entity entity) {
		return false;
	}

	/**
	 * @return {@code true} if this entity can equip the provided item, or {@code false}
	 */
	@Override
	public boolean canEquip(ItemStack stack) {
		// TODO maybe rideable with a saddle in the future?
		return false;
	}

	/**
	 * Handles the logic of this entity interacting with an item on the ground.
	 */
	@Override
	protected void loot(ItemEntity item) {}

	/**
	 * The primary logic of an {@link Entity}.
	 */
	@Override
	public void tick() {
		super.tick();

		if (this.world.isClient) {
			this.phaseManager.getCurrent().clientTick();
		} else {
			Phase phase = this.phaseManager.getCurrent();
			phase.serverTick();
			if (this.phaseManager.getCurrent() != phase) {
				this.phaseManager.getCurrent().serverTick();
			}
		}

		if (this.isAiDisabled()) {
			this.setAir(this.getMaxAir());
		} else {
			if (this.isWet()) {
				this.setMoistness(MAX_MOISTNESS);
			} else {
				this.setMoistness(this.getMoistness() - 1);
				if (this.getMoistness() <= 0) {
					this.damage(DamageSource.DRYOUT, 5.0F);
				}

				// Flop around slightly if the whale is drying out in the air
				if (this.onGround) {
					this.setVelocity(this.getVelocity().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F, 0.5, (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F));
					this.setYaw(this.random.nextFloat() * 360.0F);
					this.onGround = false;
					this.velocityDirty = true;
				}
			}

			if (this.world.isClient && this.isTouchingWater() && this.getVelocity().lengthSquared() > 0.03) {
				Vec3d rotationVec = this.getRotationVec(0.0F);
				float f = MathHelper.cos(this.getYaw() * MathHelper.RADIANS_PER_DEGREE) * 0.3F;
				float g = MathHelper.sin(this.getYaw() * MathHelper.RADIANS_PER_DEGREE) * 0.3F;
				float h = 6F - this.random.nextFloat() * 0.7F;

				for (int i = 0; i < 2; ++i) {
					this.world.addParticle(
							ParticleTypes.DOLPHIN,
							this.getX() - rotationVec.x * (double) h + (double) f,
							this.getY() - rotationVec.y,
							this.getZ() - rotationVec.z * (double) h + (double) g,
							0.0,
							0.0,
							0.0
					);
					this.world.addParticle(
							ParticleTypes.DOLPHIN,
							this.getX() - rotationVec.x * (double) h - (double) f,
							this.getY() - rotationVec.y,
							this.getZ() - rotationVec.z * (double) h - (double) g,
							0.0,
							0.0,
							0.0
					);
				}
			}
		}
	}

	/**
	 * The movement logic of an entity.
	 */
	@Override
	public void tickMovement() {
		this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

		if (this.stunTicks > 0) {
			this.stunTicks--;

			if (this.stunTicks != 0) {
				return;
			}
		}

		super.tickMovement();

		for (var part : this.parts) {
			part.rotate(this.getPitch(), this.getHeadYaw(), 0.0f);
		}
	}

	// TODO whale sounds
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_DOLPHIN_HURT;
	}

	// TODO whale sounds
	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_DOLPHIN_DEATH;
	}

	// TODO whale sounds
	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTouchingWater() ? SoundEvents.ENTITY_DOLPHIN_AMBIENT_WATER : SoundEvents.ENTITY_DOLPHIN_AMBIENT;
	}

	// TODO whale sounds
	@Override
	protected SoundEvent getSplashSound() {
		return SoundEvents.ENTITY_DOLPHIN_SPLASH;
	}

	// TODO whale sounds
	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_DOLPHIN_SWIM;
	}

	/**
	 * Allows you to do certain speed and velocity calculations. This is useful for custom vehicle behavior, or custom entity movement. This is not to be confused with AI.
	 * @param movementInput represents the sidewaysSpeed, upwardSpeed, and forwardSpeed of the entity in that order
	 */
	@Override
	public void travel(Vec3d movementInput) {
		if (this.canMoveVoluntarily() && this.isTouchingWater()) {
			this.updateVelocity(this.getMovementSpeed(), movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9));
			if (this.getTarget() == null) {
				this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	/**
	 * Called on the client when instantiating an entity.
	 */
	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		WhaleEntityPart[] parts = (WhaleEntityPart[]) this.getEntityParts();

		for (int i = 0; i < parts.length; ++i) {
			parts[i].setId(i + packet.getId());
			parts[i].setRelativePosition(Vec3d.ZERO);
		}

		this.setPartPositions();
	}

	@Override
	public EntityPart<?>[] getEntityParts() {
		return this.parts;
	}

	public PhaseManager getPhaseManager() {
		return this.phaseManager;
	}

	/**
	 * Allows whales to occasionally breach, jumping out of the water.
	 */
	static class BreachGoal extends DiveJumpingGoal {
		private static final int[] OFFSET_MULTIPLIERS = new int[]{0, 1, 4, 5, 6, 7};
		private final WhaleEntity whale;
		private final int chance;
		private boolean inWater;

		BreachGoal(WhaleEntity whale, int i) {
			this.whale = whale;
			this.chance = toGoalTicks(i);
		}

		@Override
		public boolean canStart() {
			if (this.whale.getRandom().nextInt(this.chance) != 0) {
				return false;
			} else {
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
			boolean inWater = this.inWater;
			if (!inWater) {
				FluidState fluidState = this.whale.world.getFluidState(this.whale.getBlockPos());
				this.inWater = fluidState.isIn(FluidTags.WATER);
			}

			if (this.inWater && !inWater) {
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
