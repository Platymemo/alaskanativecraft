package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
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
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class DogsledEntity extends Entity implements Inventory, NamedScreenHandlerFactory {
    private static final TrackedData<Integer> DAMAGE_WOBBLE_TICKS;
    private static final TrackedData<Integer> DAMAGE_WOBBLE_SIDE;
    private static final TrackedData<Float> DAMAGE_WOBBLE_STRENGTH;
    private static final TrackedData<Integer> DOGSLED_TYPE;
    private float ticksUnderwater;
    private float yawVelocity;
    private float velocityDecay;
    private int clientInterpolationSteps;
    private double x;
    private double y;
    private double z;
    private double dogsledYaw;
    private double dogsledPitch;
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
        this.stepHeight = 1.0F;
        this.inventory = DefaultedList.ofSize(36, ItemStack.EMPTY);
        this.inanimate = true;
    }

    public DogsledEntity(World world, double x, double y, double z) {
        this(AlaskaEntities.DOGSLED, world);
        this.setPosition(x, y, z);
        this.updateTrackedPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height;
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(DAMAGE_WOBBLE_TICKS, 0);
        this.dataTracker.startTracking(DAMAGE_WOBBLE_SIDE, 1);
        this.dataTracker.startTracking(DAMAGE_WOBBLE_STRENGTH, 0.0F);
        this.dataTracker.startTracking(DOGSLED_TYPE, DogsledEntity.Type.OAK.ordinal());
    }

    public boolean collidesWith(Entity other) {
        return method_30959(this, other);
    }

    public static boolean method_30959(Entity entity, Entity entity2) {
        return (entity2.isCollidable() || entity2.isPushable()) && !entity.isConnectedThroughVehicle(entity2);
    }

    public boolean isCollidable() {
        return true;
    }

    public boolean isPushable() {
        return true;
    }

    protected Vec3d positionInPortal(Direction.Axis axis, BlockLocating.Rectangle arg) {
        return LivingEntity.positionInPortal(super.positionInPortal(axis, arg));
    }

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
        switch (this.getDogsledType()) {
            case OAK:
            default:
                return AlaskaItems.OAK_DOGSLED;
            case SPRUCE:
                return AlaskaItems.SPRUCE_DOGSLED;
            case BIRCH:
                return AlaskaItems.BIRCH_DOGSLED;
            case JUNGLE:
                return AlaskaItems.JUNGLE_DOGSLED;
            case ACACIA:
                return AlaskaItems.ACACIA_DOGSLED;
            case DARK_OAK:
                return AlaskaItems.DARK_OAK_DOGSLED;
        }
    }

    @Environment(EnvType.CLIENT)
    public void animateDamage() {
        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() * 11.0F);
    }

    public boolean collides() {
        return !this.isRemoved();
    }

    @Environment(EnvType.CLIENT)
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dogsledYaw = yaw;
        this.dogsledPitch = pitch;
        this.clientInterpolationSteps = 10;
    }

    public Direction getMovementDirection() {
        return this.getHorizontalFacing().rotateYClockwise();
    }

    public void tick() {
        this.location = this.checkLocation();
        if (this.location != Location.UNDER_WATER && this.location != Location.UNDER_FLOWING_WATER) {
            this.ticksUnderwater = 0.0F;
        } else {
            ++this.ticksUnderwater;
        }

        if (!this.world.isClient && this.ticksUnderwater >= 60.0F) {
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
            if (this.world.isClient) {
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
                ((LivingEntity) passenger).updateLimbs(((LivingEntity) passenger), false);
            }
        }

        this.checkBlockCollision();
        List<Entity> list = this.world.getOtherEntities(this, this.getBoundingBox().expand(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntityPredicates.canBePushedBy(this));
        if (!list.isEmpty()) {
            boolean bl = !this.world.isClient;

            for (Entity entity : list) {
                if (!entity.hasPassenger(this)) {
                    if (bl && this.getPassengerList().size() < 2 && !entity.hasVehicle() && entity instanceof WolfEntity && ((WolfEntity) entity).getOwner() != null && !((WolfEntity) entity).isBaby()) {
                        entity.startRiding(this);
                    } else {
                        this.pushAwayFrom(entity);
                    }
                }
            }
        }

    }

    private void clientInterpolation() {
        if (this.isLogicalSideForUpdatingMovement()) {
            this.clientInterpolationSteps = 0;
            this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
        }

        if (this.clientInterpolationSteps > 0) {
            double d = this.getX() + (this.x - this.getX()) / (double) this.clientInterpolationSteps;
            double e = this.getY() + (this.y - this.getY()) / (double) this.clientInterpolationSteps;
            double f = this.getZ() + (this.z - this.getZ()) / (double) this.clientInterpolationSteps;
            double g = MathHelper.wrapDegrees(this.dogsledYaw - (double) this.getYaw());
            float yaw = (float) ((double) this.getYaw() + g / (double) this.clientInterpolationSteps);
            float pitch = (float) ((double) this.getPitch() + (this.dogsledPitch - (double) this.getPitch()) / (double) this.clientInterpolationSteps);
            --this.clientInterpolationSteps;
            this.setPosition(d, e, f);
            this.setRotation(yaw, pitch);
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
                            BlockState blockState = this.world.getBlockState(mutable);
                            BlockState topBlock = this.world.getBlockState(mutable.up());
                            if (blockState.isOf(Blocks.SNOW_BLOCK) || topBlock.isOf(Blocks.SNOW)) {
                                f += 0.95F;
                                ++o;
                            } else if (!(blockState.getBlock() instanceof LilyPadBlock) && VoxelShapes.matchesAnywhere(blockState.getCollisionShape(this.world, mutable).offset(p, s, q), voxelShape, BooleanBiFunction.AND)) {
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
                    FluidState fluidState = this.world.getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER)) {
                        float f = (float) p + fluidState.getHeight(this.world, mutable);
                        this.waterLevel = Math.max(f, this.waterLevel);
                        bl |= box.minY < (double) f;
                        if (d < (double) ((float) mutable.getY() + fluidState.getHeight(this.world, mutable))) {
                            if (!fluidState.isStill()) {
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

            this.setBodyYaw(this.getYaw() + this.yawVelocity);
            if (this.pressingForward) {
                f += 0.04F;
            }

            if (this.pressingBack) {
                f -= 0.005F;
            }

            this.setVelocity(this.getVelocity().add((MathHelper.sin(-this.getYaw() * 0.017453292F) * f), 0.0D, (MathHelper.cos(this.getYaw() * 0.017453292F) * f)));
        }
    }

    private void updateVelocity() {
        double e = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
        double f = 0.0D;
        if (this.location == Location.ON_LAND) {
            velocityDecay = this.getBlockSlipperiness();
        } else {
            if (this.location == Location.UNDER_FLOWING_WATER) {
                e = -7.0E-4D;
            } else if (this.location == Location.UNDER_WATER) {
                f = 0.009999999776482582D;
            }
            velocityDecay *= 0.95F;
        }

        if (this.getPassengerList().size() < 2) {
            velocityDecay /= 1.15F;
        }

        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x * (double) velocityDecay, vec3d.y + e, vec3d.z * (double) velocityDecay);
        this.yawVelocity *= velocityDecay / 1.5F;
        if (f > 0.0D) {
            Vec3d vec3d2 = this.getVelocity();
            this.setVelocity(vec3d2.x, (vec3d2.y + f * 0.06153846016296973D) * 0.75D, vec3d2.z);
        }
    }

    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset((this.getWidth() * MathHelper.SQUARE_ROOT_OF_TWO), passenger.getWidth(), passenger.getYaw());
        double d = this.getX() + vec3d.x;
        double e = this.getZ() + vec3d.z;
        BlockPos blockPos = new BlockPos(d, this.getBoundingBox().maxY, e);
        BlockPos blockPos2 = blockPos.down();
        if (!this.world.isWater(blockPos2)) {
            List<Vec3d> list = Lists.newArrayList();
            double f = this.world.getDismountHeight(blockPos);
            if (Dismounting.canDismountInBlock(f)) {
                list.add(new Vec3d(d, (double)blockPos.getY() + f, e));
            }

            double g = this.world.getDismountHeight(blockPos2);
            if (Dismounting.canDismountInBlock(g)) {
                list.add(new Vec3d(d, (double)blockPos2.getY() + g, e));
            }

            for (EntityPose entityPose : passenger.getPoses()) {
                for (Vec3d vec3d2 : list) {
                    if (Dismounting.canPlaceEntityAt(this.world, vec3d2, passenger, entityPose)) {
                        passenger.setPose(entityPose);
                        return vec3d2;
                    }
                }
            }
        }

        return super.updatePassengerForDismount(passenger);
    }

    protected void copyEntityData(Entity entity) {
        entity.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(entity.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        entity.prevYaw += (g - f);
        entity.setBodyYaw(entity.getYaw() + g - f);
        entity.setHeadYaw(entity.getYaw());
    }

    @Environment(EnvType.CLIENT)
    public void onPassengerLookAround(Entity passenger) {
        this.copyEntityData(passenger);
    }

    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        if (!this.hasVehicle()) {
            if (onGround) {
                if (this.fallDistance > 3.0F) {
                    if (this.location != Location.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.handleFallDamage(this.fallDistance, 1.0F, DamageSource.FALL);
                    if (!this.world.isClient && !this.isRemoved()) {
                        this.kill();
                        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            int j;
                            for (j = 0; j < 3; ++j) {
                                this.dropItem(this.getDogsledType().getBaseBlock());
                            }

                            for (j = 0; j < 2; ++j) {
                                this.dropItem(Items.STICK);
                            }

                            dropItems(null);
                        }
                    }
                }

                this.fallDistance = 0.0F;
            } else if (!this.world.getFluidState(this.getBlockPos().down()).isIn(FluidTags.WATER) && heightDifference < 0.0D) {
                this.fallDistance = (float) ((double) this.fallDistance - heightDifference);
            }

        }
    }

    public void setDamageWobbleStrength(float wobbleStrength) {
        this.dataTracker.set(DAMAGE_WOBBLE_STRENGTH, wobbleStrength);
    }

    public float getDamageWobbleStrength() {
        return this.dataTracker.get(DAMAGE_WOBBLE_STRENGTH);
    }

    public void setDamageWobbleTicks(int wobbleTicks) {
        this.dataTracker.set(DAMAGE_WOBBLE_TICKS, wobbleTicks);
    }

    public int getDamageWobbleTicks() {
        return this.dataTracker.get(DAMAGE_WOBBLE_TICKS);
    }

    public void setDamageWobbleSide(int side) {
        this.dataTracker.set(DAMAGE_WOBBLE_SIDE, side);
    }

    public int getDamageWobbleSide() {
        return this.dataTracker.get(DAMAGE_WOBBLE_SIDE);
    }

    public void setDogsledType(DogsledEntity.Type type) {
        this.dataTracker.set(DOGSLED_TYPE, type.ordinal());
    }

    public DogsledEntity.Type getDogsledType() {
        return DogsledEntity.Type.getType(this.dataTracker.get(DOGSLED_TYPE));
    }

    protected boolean canAddPassenger(Entity passenger) {
        int i = this.getPassengerList().size();
        if (this.isSubmergedIn(FluidTags.WATER)) {
            return false;
        }
        if (i == 0) {
            return passenger instanceof PlayerEntity || passenger instanceof WolfEntity;
        } else if (i == 1) {
            return passenger instanceof PlayerEntity;
        }
        return false;
    }

    @Nullable
    public Entity getPrimaryPassenger() {
        List<Entity> list = this.getPassengerList();
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean isSubmergedInWater() {
        return this.location == Location.UNDER_WATER || this.location == Location.UNDER_FLOWING_WATER;
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Environment(EnvType.CLIENT)
    public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack) {
        this.pressingLeft = pressingLeft;
        this.pressingRight = pressingRight;
        this.pressingForward = pressingForward;
        this.pressingBack = pressingBack;
    }

    public int size() {
        return 27;
    }

    public double getMountedHeightOffset() {
        return 0.2D;
    }

    public boolean damage(DamageSource source, float amount) {
        if (!this.world.isClient && !this.isRemoved()) {
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
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            if (damageSource != null) {
                ItemStack itemStack = this.asItem().getDefaultStack();
                if (this.hasCustomName()) {
                    itemStack.setCustomName(this.getCustomName());
                }
                this.dropStack(itemStack);
            }
            ItemScatterer.spawn(this.world, this, this);
            if (damageSource != null && !this.world.isClient) {
                Entity entity = damageSource.getSource();
                if (entity != null && entity.getType() == EntityType.PLAYER) {
                    PiglinBrain.onGuardedBlockInteracted((PlayerEntity) entity, true);
                }
            }
        }
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        this.updatePassengerPosition(passenger, Entity::setPosition);
    }

    private void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        if (this.hasPassenger(passenger)) {
            if (passenger instanceof PlayerEntity) {
                float g = (float) ((this.isRemoved() ? 0.009999999776482582D : this.getMountedHeightOffset()) + passenger.getHeightOffset());
                double x = MathHelper.cos((this.getYaw() + 90.0F) * 0.0174533F);
                double z = MathHelper.sin((this.getYaw() + 90.0F) * 0.0174533F);
                positionUpdater.accept(passenger, this.getX() - x, this.getY() + (double) g, this.getZ() - z);
            } else if (passenger instanceof WolfEntity) {
                passenger.noClip = true;
                Vec3d vec3d = (new Vec3d(1.5D, 0.0D, 0.0D)).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
                positionUpdater.accept(passenger, this.getX() + vec3d.x, this.getY(), this.getZ() + vec3d.z);
                passenger.setBodyYaw(passenger.getYaw() + this.yawVelocity);
                passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
                this.copyEntityData(passenger);
            }
        }
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
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

    public void readCustomDataFromNbt(NbtCompound tag) {
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

    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.isSneaking()) {
            player.openHandledScreen(this);
            if (!player.world.isClient) {
                PiglinBrain.onGuardedBlockInteracted(player, true);
                return ActionResult.CONSUME;
            } else {
                return ActionResult.SUCCESS;
            }
        } else {
            if (this.ticksUnderwater < 60.0F) {
                if (!this.world.isClient) {
                    return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
                } else {
                    return ActionResult.SUCCESS;
                }
            } else {
                return ActionResult.PASS;
            }
        }
    }

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

    public ItemStack getStack(int slot) {
        this.generateLoot(null);
        return this.inventory.get(slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        this.generateLoot(null);
        return Inventories.splitStack(this.inventory, slot, amount);
    }

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

    public void setStack(int slot, ItemStack stack) {
        this.generateLoot(null);
        this.inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

    }

    public boolean equip(int slot, ItemStack item) {
        if (slot >= 0 && slot < this.size()) {
            this.setStack(slot, item);
            return true;
        } else {
            return false;
        }
    }

    public void markDirty() {
    }

    public boolean canPlayerUse(PlayerEntity player) {
        if (this.isRemoved()) {
            return false;
        } else {
            return player.squaredDistanceTo(this) <= 64.0D;
        }
    }

    public void generateLoot(@Nullable PlayerEntity player) {
        if (this.lootTableId != null && this.world.getServer() != null) {
            LootTable lootTable = this.world.getServer().getLootManager().getTable(this.lootTableId);
            if (player instanceof ServerPlayerEntity) {
                Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger((ServerPlayerEntity) player, this.lootTableId);
            }

            this.lootTableId = null;
            LootContext.Builder builder = (new LootContext.Builder((ServerWorld) this.world)).parameter(LootContextParameters.ORIGIN, this.getPos()).random(this.lootSeed);
            if (player != null) {
                builder.luck(player.getLuck()).parameter(LootContextParameters.THIS_ENTITY, player);
            }

            lootTable.supplyInventory(this, builder.build(LootContextTypes.CHEST));
        }

    }

    public void clear() {
        this.generateLoot(null);
        this.inventory.clear();
    }

    public void setLootTable(Identifier id, long lootSeed) {
        this.lootTableId = id;
        this.lootSeed = lootSeed;
    }

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

    static {
        DAMAGE_WOBBLE_TICKS = DataTracker.registerData(DogsledEntity.class, TrackedDataHandlerRegistry.INTEGER);
        DAMAGE_WOBBLE_SIDE = DataTracker.registerData(DogsledEntity.class, TrackedDataHandlerRegistry.INTEGER);
        DAMAGE_WOBBLE_STRENGTH = DataTracker.registerData(DogsledEntity.class, TrackedDataHandlerRegistry.FLOAT);
        DOGSLED_TYPE = DataTracker.registerData(DogsledEntity.class, TrackedDataHandlerRegistry.INTEGER);
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

        public String getName() {
            return this.name;
        }

        public Block getBaseBlock() {
            return this.baseBlock;
        }

        public String toString() {
            return this.name;
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
    }

    public enum Location {
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR
    }
}
