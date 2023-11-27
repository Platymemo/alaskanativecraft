package com.github.platymemo.alaskanativecraft.entity;

import java.util.Iterator;
import java.util.List;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import org.quiltmc.loader.api.minecraft.ClientOnly;

public class DogsledEntity extends Entity implements Inventory, NamedScreenHandlerFactory {
	private static final TrackedData<Integer> DAMAGE_WOBBLE_TICKS;
	private static final TrackedData<Integer> DAMAGE_WOBBLE_SIDE;
	private static final TrackedData<Float> DAMAGE_WOBBLE_STRENGTH;
	private static final TrackedData<Integer> DOGSLED_TYPE;

	static {
		DAMAGE_WOBBLE_TICKS = DataTracker.registerData(DogsledEntity.class, TrackedDataHandlerRegistry.INTEGER);
		DAMAGE_WOBBLE_SIDE = DataTracker.registerData(DogsledEntity.class, TrackedDataHandlerRegistry.INTEGER);
		DAMAGE_WOBBLE_STRENGTH = DataTracker.registerData(DogsledEntity.class, TrackedDataHandlerRegistry.FLOAT);
		DOGSLED_TYPE = DataTracker.registerData(DogsledEntity.class, TrackedDataHandlerRegistry.INTEGER);
	}

	private float ticksUnderwater;
	private float yawVelocity;
	private float velocityDecay;
	private int clientInterpolationSteps;
	private double x;
	private double y;
	private double z;
	private double predictedYaw;
	private double predictedPitch;
	private boolean pressingLeft;
	private boolean pressingRight;
	private boolean pressingForward;
	private boolean pressingBack;
	private double waterLevel;
	private Location location;
	private DefaultedList<ItemStack> inventory;
	@Nullable
	private Identifier lootTableId;
	private long lootSeed;

	public DogsledEntity(EntityType<?> entityType, World world) {
		super(entityType, world);
		this.setStepHeight(1.0F);
		this.inventory = DefaultedList.ofSize(36, ItemStack.EMPTY);
		this.setOnGround(true);
	}

	public DogsledEntity(World world, double x, double y, double z) {
		this(AlaskaEntities.DOGSLED, world);
		this.setPosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}

	@Override
	protected float getEyeHeight(EntityPose pose, @NotNull EntityDimensions dimensions) {
		return dimensions.height;
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(DAMAGE_WOBBLE_TICKS, 0);
		this.dataTracker.startTracking(DAMAGE_WOBBLE_SIDE, 1);
		this.dataTracker.startTracking(DAMAGE_WOBBLE_STRENGTH, 0.0F);
		this.dataTracker.startTracking(DOGSLED_TYPE, DogsledEntity.Type.OAK.ordinal());
	}

	@Override
	public boolean collidesWith(Entity other) {
		return BoatEntity.canCollide(this, other);
	}

	@Override
	public boolean isCollidable() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	protected Vec3d positionInPortal(Direction.Axis axis, BlockLocating.Rectangle arg) {
		return LivingEntity.positionInPortal(super.positionInPortal(axis, arg));
	}

	@Override
	public void pushAwayFrom(Entity entity) {
		if (entity instanceof BoatEntity || entity instanceof DogsledEntity) {
			if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.pushAwayFrom(entity);
			}
		} else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
			super.pushAwayFrom(entity);
		}
	}

	public Item asItem() {
		return switch (this.getDogsledType()) {
			case SPRUCE -> AlaskaItems.SPRUCE_DOGSLED;
			case BIRCH -> AlaskaItems.BIRCH_DOGSLED;
			case JUNGLE -> AlaskaItems.JUNGLE_DOGSLED;
			case ACACIA -> AlaskaItems.ACACIA_DOGSLED;
			case DARK_OAK -> AlaskaItems.DARK_OAK_DOGSLED;
			default -> AlaskaItems.OAK_DOGSLED;
		};
	}

	@Override
	@ClientOnly
	public void animateDamage(float yaw) {
		this.setDamageWobbleSide(-this.getDamageWobbleSide());
		this.setDamageWobbleTicks(10);
		this.setDamageWobbleStrength(this.getDamageWobbleStrength() * 11.0F);
	}

	@Override
	public boolean collides() {
		return !this.isRemoved();
	}

	@Override
	@ClientOnly
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.predictedYaw = yaw;
		this.predictedPitch = pitch;
		this.clientInterpolationSteps = 10;
	}

	@Override
	public Direction getMovementDirection() {
		return this.getHorizontalFacing().rotateYClockwise();
	}

	@Override
	public void tick() {
		this.location = this.checkLocation();
		if (this.location != Location.UNDER_WATER && this.location != Location.UNDER_FLOWING_WATER) {
			this.ticksUnderwater = 0.0F;
		} else {
			this.ticksUnderwater++;
		}

		if (!this.getWorld().isClient && this.ticksUnderwater >= 60.0F) {
			this.removeAllPassengers();
		}

		if (this.getDamageWobbleTicks() > 0) {
			this.setDamageWobbleTicks(this.getDamageWobbleTicks() - 1);
		}

		if (this.getDamageWobbleStrength() > 0.0F) {
			this.setDamageWobbleStrength(this.getDamageWobbleStrength() - 1.0F);
		}

		super.tick();
		this.clientInterpolation();
		if (this.isLogicalSideForUpdatingMovement()) {
			this.updateVelocity();
			if (this.getWorld().isClient) {
				this.updateMovement();
			}

			this.move(MovementType.SELF, this.getVelocity());
		} else {
			this.setVelocity(Vec3d.ZERO);
		}

		for (Entity passenger : this.getPassengerList()) {
			if (passenger instanceof WolfEntity) {
				if (!MathHelper.approximatelyEquals(this.getVelocity().length(), 0.0D)) {
					((TameableEntity) passenger).setInSittingPose(false);
				}

				((LivingEntity) passenger).updateLimbs(false);
			}
		}

		this.checkBlockCollision();
		List<Entity> list = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.2D, -0.01D, 0.2D), EntityPredicates.canBePushedBy(this));
		if (!list.isEmpty()) {
			for (Entity entity : list) {
				if (!entity.hasVehicle()) {
					if (!this.getWorld().isClient && this.getPassengerList().size() < 2 && !entity.hasVehicle() && entity instanceof WolfEntity wolf && wolf.getOwner() != null && !wolf.isBaby()) {
						entity.startRiding(this);
					} else {
						this.pushAwayFrom(entity);
					}
				}
			}
		}
	}

	/**
	 * Handles the logic of client predictive movement.
	 */
	private void clientInterpolation() {
		if (this.isLogicalSideForUpdatingMovement()) {
			this.clientInterpolationSteps = 0;
			this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
		}

		if (this.clientInterpolationSteps > 0) {
			double interpolatedX = this.getX() + (this.x - this.getX()) / (double) this.clientInterpolationSteps;
			double interpolatedY = this.getY() + (this.y - this.getY()) / (double) this.clientInterpolationSteps;
			double interpolatedZ = this.getZ() + (this.z - this.getZ()) / (double) this.clientInterpolationSteps;
			float interpolatedYaw = (float) ((double) this.getYaw() + MathHelper.wrapDegrees(this.predictedYaw - (double) this.getYaw()) / (double) this.clientInterpolationSteps);
			float interpolatedPitch = (float) ((double) this.getPitch() + (this.predictedPitch - (double) this.getPitch()) / (double) this.clientInterpolationSteps);
			--this.clientInterpolationSteps;
			this.setPosition(interpolatedX, interpolatedY, interpolatedZ);
			this.setRotation(interpolatedYaw, interpolatedPitch);
		}
	}

	private Location checkLocation() {
		Location location = this.getUnderWaterLocation();
		if (location != null) {
			this.waterLevel = this.getBoundingBox().maxY;
			return location;
		} else {
			float f = this.getBlockSlipperiness();
			if (f > 0.0F) {
				return Location.ON_LAND;
			} else {
				return Location.IN_AIR;
			}
		}
	}

	public float getBlockSlipperiness() {
		Box box = this.getBoundingBox();
		Box box2 = new Box(box.minX, box.minY - 0.001D, box.minZ, box.maxX, box.minY, box.maxZ);
		int i = MathHelper.floor(box2.minX) - 1;
		int j = MathHelper.ceil(box2.maxX) + 1;
		int k = MathHelper.floor(box2.minY) - 1;
		int l = MathHelper.ceil(box2.maxY) + 1;
		int m = MathHelper.floor(box2.minZ) - 1;
		int n = MathHelper.ceil(box2.maxZ) + 1;
		VoxelShape voxelShape = VoxelShapes.cuboid(box2);
		float f = 0.0F;
		int o = 0;
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int p = i; p < j; ++p) {
			for (int q = m; q < n; ++q) {
				int r = (p != i && p != j - 1 ? 0 : 1) + (q != m && q != n - 1 ? 0 : 1);
				if (r != 2) {
					for (int s = k; s < l; ++s) {
						if (r <= 0 || s != k && s != l - 1) {
							mutable.set(p, s, q);
							BlockState blockState = this.getWorld().getBlockState(mutable);
							BlockState topBlock = this.getWorld().getBlockState(mutable.up());
							if (blockState.isOf(Blocks.SNOW_BLOCK) || topBlock.isOf(Blocks.SNOW)) {
								f += 0.95F;
								++o;
							} else if (!(blockState.getBlock() instanceof LilyPadBlock) && VoxelShapes.matchesAnywhere(blockState.getCollisionShape(this.getWorld(), mutable).offset(p, s, q), voxelShape, BooleanBiFunction.AND)) {
								f += blockState.getBlock().getSlipperiness();
								++o;
							}
						}
					}
				}
			}
		}

		return f / (float) o;
	}

	@Nullable
	private Location getUnderWaterLocation() {
		Box box = this.getBoundingBox();
		double d = box.maxY + 0.001D;
		int i = MathHelper.floor(box.minX);
		int j = MathHelper.ceil(box.maxX);
		int k = MathHelper.floor(box.maxY);
		int l = MathHelper.ceil(d);
		int m = MathHelper.floor(box.minZ);
		int n = MathHelper.ceil(box.maxZ);
		boolean bl = false;
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int o = i; o < j; ++o) {
			for (int p = k; p < l; ++p) {
				for (int q = m; q < n; ++q) {
					mutable.set(o, p, q);
					FluidState fluidState = this.getWorld().getFluidState(mutable);
					if (fluidState.isIn(FluidTags.WATER)) {
						float f = (float) p + fluidState.getHeight(this.getWorld(), mutable);
						this.waterLevel = Math.max(f, this.waterLevel);
						bl |= box.minY < (double) f;
						if (d < (double) ((float) mutable.getY() + fluidState.getHeight(this.getWorld(), mutable))) {
							if (!fluidState.isSource()) {
								return Location.UNDER_FLOWING_WATER;
							}

							bl = true;
						}
					}
				}
			}
		}

		return bl ? Location.UNDER_WATER : null;
	}

	private void updateMovement() {
		if (this.hasPassengers()) {
			float f = 0.0F;
			if (this.pressingLeft) {
				--this.yawVelocity;
			}

			if (this.pressingRight) {
				++this.yawVelocity;
			}

			if (this.pressingRight != this.pressingLeft && !this.pressingForward && !this.pressingBack) {
				f += 0.005F;
			}

			this.setYaw(this.getYaw() + this.yawVelocity);
			if (this.pressingForward) {
				f += 0.04F;
			}

			if (this.pressingBack) {
				f -= 0.005F;
			}

			this.setVelocity(this.getVelocity().add((MathHelper.sin(-this.getYaw() * MathHelper.RADIANS_PER_DEGREE) * f), 0.0D, (MathHelper.cos(this.getYaw() * MathHelper.RADIANS_PER_DEGREE) * f)));
		}
	}

	private void updateVelocity() {
		double gravity = this.hasNoGravity() ? 0.0D : -0.04D;
		boolean verticalDrag = false;
		if (this.location == Location.ON_LAND) {
			this.velocityDecay = this.getBlockSlipperiness();
		} else {
			if (this.location == Location.UNDER_FLOWING_WATER) {
				gravity = -7.0E-4D;
			} else if (this.location == Location.UNDER_WATER) {
				verticalDrag = true;
			}

			this.velocityDecay *= 0.95F;
		}

		if (this.getPassengerList().size() < 2) {
			this.velocityDecay /= 1.15F;
		}

		Vec3d velocity = this.getVelocity();
		this.setVelocity(velocity.x * (double) this.velocityDecay, velocity.y + gravity, velocity.z * (double) this.velocityDecay);
		this.yawVelocity *= this.velocityDecay / 1.5F;
		if (verticalDrag) {
			Vec3d newVelocity = this.getVelocity();
			this.setVelocity(newVelocity.x, (newVelocity.y + 0.00615D) * 0.75D, newVelocity.z);
		}
	}

	@Override
	public Vec3d updatePassengerForDismount(@NotNull LivingEntity passenger) {
		Vec3d dismountOffset = getPassengerDismountOffset((this.getWidth() * MathHelper.SQUARE_ROOT_OF_TWO), passenger.getWidth(), passenger.getYaw());
		double dismountX = this.getX() + dismountOffset.x;
		double dismountZ = this.getZ() + dismountOffset.z;
		BlockPos dismountPos = BlockPos.create(dismountX, this.getBoundingBox().maxY, dismountZ);
		BlockPos altDismountPos = dismountPos.down();
		if (!this.getWorld().isWater(altDismountPos)) {
			List<Vec3d> possibleDismountPositions = Lists.newArrayList();
			double dismountHeight = this.getWorld().getDismountHeight(dismountPos);
			if (Dismounting.canDismountInBlock(dismountHeight)) {
				possibleDismountPositions.add(new Vec3d(dismountX, (double) dismountPos.getY() + dismountHeight, dismountZ));
			}

			double altDismountHeight = this.getWorld().getDismountHeight(altDismountPos);
			if (Dismounting.canDismountInBlock(altDismountHeight)) {
				possibleDismountPositions.add(new Vec3d(dismountX, (double) altDismountPos.getY() + altDismountHeight, dismountZ));
			}

			for (EntityPose pose : passenger.getPoses()) {
				for (Vec3d dismountPosition : possibleDismountPositions) {
					if (Dismounting.canPlaceEntityAt(this.getWorld(), dismountPosition, passenger, pose)) {
						passenger.setPose(pose);
						return dismountPosition;
					}
				}
			}
		}

		return super.updatePassengerForDismount(passenger);
	}

	/**
	 * Limits the difference in yaw between this dogsled and the provided {@link Entity}.
	 */
	protected void limitYaw(@NotNull Entity entity) {
		entity.setBodyYaw(this.getYaw());
		float yaw = MathHelper.wrapDegrees(entity.getYaw() - this.getYaw());
		float clampedYaw = MathHelper.clamp(yaw, -105.0F, 105.0F);
		entity.prevYaw += (clampedYaw - yaw);
		entity.setYaw(entity.getYaw() + clampedYaw - yaw);
		entity.setHeadYaw(entity.getYaw());
	}

	@Override
	@ClientOnly
	public void onPassengerLookAround(Entity passenger) {
		this.limitYaw(passenger);
	}

	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
		if (!this.hasVehicle()) {
			if (onGround) {
				if (this.fallDistance > 3.0F) {
					if (this.location != Location.ON_LAND) {
						this.fallDistance = 0.0F;
						return;
					}

					this.handleFallDamage(this.fallDistance, 1.0F, this.getDamageSources().fall());
					if (!this.getWorld().isClient && !this.isRemoved()) {
						this.kill();
						if (this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
							int j;
							for (j = 0; j < 3; ++j) {
								this.dropItem(this.getDogsledType().getBaseBlock());
							}

							for (j = 0; j < 2; ++j) {
								this.dropItem(Items.STICK);
							}

							this.dropItems(null);
						}
					}
				}

				this.fallDistance = 0.0F;
			} else if (!this.getWorld().getFluidState(this.getBlockPos().down()).isIn(FluidTags.WATER) && heightDifference < 0.0D) {
				this.fallDistance = (float) ((double) this.fallDistance - heightDifference);
			}
		}
	}

	public float getDamageWobbleStrength() {
		return this.dataTracker.get(DAMAGE_WOBBLE_STRENGTH);
	}

	public void setDamageWobbleStrength(float wobbleStrength) {
		this.dataTracker.set(DAMAGE_WOBBLE_STRENGTH, wobbleStrength);
	}

	public int getDamageWobbleTicks() {
		return this.dataTracker.get(DAMAGE_WOBBLE_TICKS);
	}

	public void setDamageWobbleTicks(int wobbleTicks) {
		this.dataTracker.set(DAMAGE_WOBBLE_TICKS, wobbleTicks);
	}

	public int getDamageWobbleSide() {
		return this.dataTracker.get(DAMAGE_WOBBLE_SIDE);
	}

	public void setDamageWobbleSide(int side) {
		this.dataTracker.set(DAMAGE_WOBBLE_SIDE, side);
	}

	public DogsledEntity.Type getDogsledType() {
		return DogsledEntity.Type.getType(this.dataTracker.get(DOGSLED_TYPE));
	}

	public void setDogsledType(@NotNull DogsledEntity.Type type) {
		this.dataTracker.set(DOGSLED_TYPE, type.ordinal());
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		int size = this.getPassengerList().size();
		if (this.isSubmergedIn(FluidTags.WATER)) {
			return false;
		}

		if (size == 0) {
			return passenger instanceof PlayerEntity || passenger instanceof WolfEntity;
		} else if (size == 1) {
			return passenger instanceof PlayerEntity;
		}

		return false;
	}

	@Nullable
	@Override
	public LivingEntity getPrimaryPassenger() {
		Entity entity = this.getFirstPassenger();
		return entity instanceof LivingEntity livingEntity ? livingEntity : null;
	}

	@Override
	public boolean isSubmergedInWater() {
		return this.location == Location.UNDER_WATER || this.location == Location.UNDER_FLOWING_WATER;
	}

	@ClientOnly
	public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack) {
		this.pressingLeft = pressingLeft;
		this.pressingRight = pressingRight;
		this.pressingForward = pressingForward;
		this.pressingBack = pressingBack;
	}

	@Override
	public int size() {
		return 27;
	}

	@Override
	public double getMountedHeightOffset() {
		return 0.2D;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (!this.getWorld().isClient && !this.isRemoved()) {
			if (this.isInvulnerableTo(source)) {
				return false;
			} else {
				this.setDamageWobbleSide(-this.getDamageWobbleSide());
				this.setDamageWobbleTicks(10);
				this.scheduleVelocityUpdate();
				this.setDamageWobbleStrength(this.getDamageWobbleStrength() + amount * 10.0F);
				boolean bl = source.getAttacker() instanceof PlayerEntity && ((PlayerEntity) source.getAttacker()).getAbilities().creativeMode;
				if (bl || this.getDamageWobbleStrength() > 40.0F) {
					this.removeAllPassengers();
					if (bl && !this.hasCustomName()) {
						this.discard();
					} else {
						this.dropItems(source);
					}
				}

				return true;
			}
		} else {
			return true;
		}
	}

	@Override
	public void removeAllPassengers() {
		for (int i = this.getPassengerList().size() - 1; i >= 0; --i) {
			Entity entity = this.getPassengerList().get(i);
			if (entity instanceof WolfEntity) {
				entity.noClip = false;
			}

			entity.stopRiding();
		}
	}

	public void dropItems(DamageSource damageSource) {
		this.remove(RemovalReason.KILLED);
		if (this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			if (damageSource != null) {
				ItemStack itemStack = this.asItem().getDefaultStack();
				if (this.hasCustomName()) {
					itemStack.setCustomName(this.getCustomName());
				}

				this.dropStack(itemStack);
			}

			ItemScatterer.spawn(this.getWorld(), this, this);
			if (damageSource != null && !this.getWorld().isClient) {
				Entity entity = damageSource.getSource();
				if (entity != null && entity.getType() == EntityType.PLAYER) {
					PiglinBrain.onGuardedBlockInteracted((PlayerEntity) entity, true);
				}
			}
		}
	}

	@Override
	protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
		if (this.hasPassenger(passenger)) {
			if (passenger instanceof PlayerEntity) {
				float g = (float) ((this.isRemoved() ? 0.01D : this.getMountedHeightOffset()) + passenger.getHeightOffset());
				double x = MathHelper.cos((this.getYaw() + 90.0F) * MathHelper.RADIANS_PER_DEGREE);
				double z = MathHelper.sin((this.getYaw() + 90.0F) * MathHelper.RADIANS_PER_DEGREE);
				positionUpdater.accept(passenger, this.getX() - x, this.getY() + (double) g, this.getZ() - z);
			} else if (passenger instanceof WolfEntity) {
				passenger.noClip = true;
				Vec3d vec3d = (new Vec3d(1.5D, 0.0D, 0.0D)).rotateY(-this.getYaw() * MathHelper.RADIANS_PER_DEGREE - MathHelper.HALF_PI);
				positionUpdater.accept(passenger, this.getX() + vec3d.x, this.getY(), this.getZ() + vec3d.z);
				passenger.setYaw(passenger.getYaw() + this.yawVelocity);
				passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
				this.limitYaw(passenger);
			}
		}
	}

	@Override
	public void writeCustomDataToNbt(@NotNull NbtCompound tag) {
		tag.putString("Type", this.getDogsledType().getName());
		if (this.lootTableId != null) {
			tag.putString("LootTable", this.lootTableId.toString());
			if (this.lootSeed != 0L) {
				tag.putLong("LootTableSeed", this.lootSeed);
			}
		} else {
			Inventories.writeNbt(tag, this.inventory);
		}
	}

	@Override
	public void readCustomDataFromNbt(@NotNull NbtCompound tag) {
		if (tag.contains("Type", 8)) {
			this.setDogsledType(DogsledEntity.Type.getType(tag.getString("Type")));
		}

		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (tag.contains("LootTable", 8)) {
			this.lootTableId = new Identifier(tag.getString("LootTable"));
			this.lootSeed = tag.getLong("LootTableSeed");
		} else {
			Inventories.readNbt(tag, this.inventory);
		}
	}

	@Override
	public ActionResult interact(@NotNull PlayerEntity player, Hand hand) {
		if (player.isSneaking()) {
			player.openHandledScreen(this);
			if (!player.getWorld().isClient) {
				PiglinBrain.onGuardedBlockInteracted(player, true);
				return ActionResult.CONSUME;
			} else {
				return ActionResult.SUCCESS;
			}
		} else {
			if (this.ticksUnderwater < 60.0F) {
				if (!this.getWorld().isClient) {
					return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
				} else {
					return ActionResult.SUCCESS;
				}
			} else {
				return ActionResult.PASS;
			}
		}
	}

	@Override
	public boolean isEmpty() {
		Iterator<ItemStack> var1 = this.inventory.iterator();

		ItemStack itemStack;
		do {
			if (!var1.hasNext()) {
				return true;
			}

			itemStack = var1.next();
		} while (itemStack.isEmpty());

		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		this.generateLoot(null);
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		this.generateLoot(null);
		return Inventories.splitStack(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		this.generateLoot(null);
		ItemStack itemStack = this.inventory.get(slot);
		if (itemStack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			this.inventory.set(slot, ItemStack.EMPTY);
			return itemStack;
		}
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.generateLoot(null);
		this.inventory.set(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (this.isRemoved()) {
			return false;
		} else {
			return player.squaredDistanceTo(this) <= 64.0D;
		}
	}

	public void generateLoot(@Nullable PlayerEntity player) {
		if (this.lootTableId != null && this.getWorld().getServer() != null) {
			LootTable lootTable = this.getWorld().getServer().getLootManager().getLootTable(this.lootTableId);
			if (player instanceof ServerPlayerEntity) {
				Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger((ServerPlayerEntity) player, this.lootTableId);
			}

			this.lootTableId = null;
			LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld) this.getWorld())
					.add(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.getBlockPos()));
			if (player != null) {
				builder.withLuck(player.getLuck()).add(LootContextParameters.THIS_ENTITY, player);
			}

			lootTable.supplyInventory(this, builder.build(LootContextTypes.CHEST), this.lootSeed);
		}
	}

	@Override
	public void clear() {
		this.generateLoot(null);
		this.inventory.clear();
	}

	public void setLootTable(Identifier id, long lootSeed) {
		this.lootTableId = id;
		this.lootSeed = lootSeed;
	}

	@Override
	@Nullable
	public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		if (this.lootTableId != null && playerEntity.isSpectator()) {
			return null;
		} else {
			this.generateLoot(playerInventory.player);
			return this.getScreenHandler(i, playerInventory);
		}
	}

	public ScreenHandler getScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
	}

	public enum Type {
		OAK(Blocks.OAK_PLANKS, "oak"),
		SPRUCE(Blocks.SPRUCE_PLANKS, "spruce"),
		BIRCH(Blocks.BIRCH_PLANKS, "birch"),
		JUNGLE(Blocks.JUNGLE_PLANKS, "jungle"),
		ACACIA(Blocks.ACACIA_PLANKS, "acacia"),
		DARK_OAK(Blocks.DARK_OAK_PLANKS, "dark_oak");

		private final String name;
		private final Block baseBlock;

		Type(Block baseBlock, String name) {
			this.name = name;
			this.baseBlock = baseBlock;
		}

		public static DogsledEntity.Type getType(int i) {
			DogsledEntity.Type[] types = values();
			if (i < 0 || i >= types.length) {
				i = 0;
			}

			return types[i];
		}

		public static DogsledEntity.Type getType(String string) {
			DogsledEntity.Type[] types = values();

			for (Type type : types) {
				if (type.getName().equals(string)) {
					return type;
				}
			}

			return types[0];
		}

		public String getName() {
			return this.name;
		}

		public Block getBaseBlock() {
			return this.baseBlock;
		}

		public String toString() {
			return this.name;
		}
	}

	public enum Location {
		UNDER_WATER,
		UNDER_FLOWING_WATER,
		ON_LAND,
		IN_AIR
	}
}
