package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import com.google.common.collect.Sets;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class SealEntity extends AnimalEntity {
    private static final TrackedData<BlockPos> HOME_POS;
    private static final TrackedData<BlockPos> TRAVEL_POS;
    private static final TrackedData<Boolean> ACTIVELY_TRAVELLING;

    public SealEntity(EntityType<? extends SealEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.moveControl = new SealEntity.SealMoveControl(this);
        this.stepHeight = 1.0F;
    }

    public void setHomePos(BlockPos pos) {
        this.dataTracker.set(HOME_POS, pos);
    }

    private BlockPos getHomePos() {
        return (BlockPos)this.dataTracker.get(HOME_POS);
    }

    private void setTravelPos(BlockPos pos) {
        this.dataTracker.set(TRAVEL_POS, pos);
    }

    private BlockPos getTravelPos() {
        return (BlockPos)this.dataTracker.get(TRAVEL_POS);
    }

    private boolean isActivelyTravelling() {
        return (Boolean)this.dataTracker.get(ACTIVELY_TRAVELLING);
    }

    private void setActivelyTravelling(boolean travelling) {
        this.dataTracker.set(ACTIVELY_TRAVELLING, travelling);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HOME_POS, BlockPos.ORIGIN);
        this.dataTracker.startTracking(TRAVEL_POS, BlockPos.ORIGIN);
        this.dataTracker.startTracking(ACTIVELY_TRAVELLING, false);
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("HomePosX", this.getHomePos().getX());
        tag.putInt("HomePosY", this.getHomePos().getY());
        tag.putInt("HomePosZ", this.getHomePos().getZ());
        tag.putInt("TravelPosX", this.getTravelPos().getX());
        tag.putInt("TravelPosY", this.getTravelPos().getY());
        tag.putInt("TravelPosZ", this.getTravelPos().getZ());
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        int i = tag.getInt("HomePosX");
        int j = tag.getInt("HomePosY");
        int k = tag.getInt("HomePosZ");
        this.setHomePos(new BlockPos(i, j, k));
        super.readCustomDataFromTag(tag);
        int l = tag.getInt("TravelPosX");
        int m = tag.getInt("TravelPosY");
        int n = tag.getInt("TravelPosZ");
        this.setTravelPos(new BlockPos(l, m, n));
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        this.setHomePos(this.getBlockPos());
        this.setTravelPos(BlockPos.ORIGIN);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    public static boolean canSpawn(EntityType<SealEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return pos.getY() < world.getSeaLevel() + 4 && world.getBaseLightLevel(pos, 0) > 8;
    }

    protected void initGoals() {
        this.goalSelector.add(0, new FleeEntityGoal(this, PolarBearEntity.class, 8.0F, 2.0D, 2.0D));
        this.goalSelector.add(0, new SealEntity.SealEscapeDangerGoal(this, 2.0D));
        this.goalSelector.add(1, new SealEntity.MateGoal(this, 1.0D));
        this.goalSelector.add(2, new SealEntity.ApproachFoodHoldingPlayerGoal(this, 1.1D, Items.SALMON.asItem()));
        this.goalSelector.add(3, new FleeEntityGoal(this, PlayerEntity.class, 16.0F, 2.0D, 2.0D));
        this.goalSelector.add(3, new SealEntity.WanderInWaterGoal(this, 1.0D));
        this.goalSelector.add(4, new SealEntity.TravelGoal(this, 1.0D));
        this.goalSelector.add(5, new SealEntity.WanderOnLandGoal(this, 1.0D, 100));
        this.goalSelector.add(6, new MeleeAttackGoal(this, 1.2000000476837158D, true));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.add(0, new FollowTargetGoal(this, SalmonEntity.class, true));
        this.targetSelector.add(0, new FollowTargetGoal(this, CodEntity.class, true));
        this.targetSelector.add(0, new FollowTargetGoal(this, SquidEntity.class, true));
    }

    public boolean canFly() {
        return false;
    }

    public boolean canBreatheInWater() {
        return true;
    }

    public EntityGroup getGroup() {
        return EntityGroup.AQUATIC;
    }

    public int getMinAmbientSoundDelay() {
        return 200;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return !this.isTouchingWater() && this.onGround && !this.isBaby() ? SoundEvents.ENTITY_TURTLE_AMBIENT_LAND : super.getAmbientSound();
    }

    protected void playSwimSound(float volume) {
        super.playSwimSound(volume * 1.5F);
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_TURTLE_SWIM;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isBaby() ? SoundEvents.ENTITY_TURTLE_HURT_BABY : SoundEvents.ENTITY_TURTLE_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? SoundEvents.ENTITY_TURTLE_DEATH_BABY : SoundEvents.ENTITY_TURTLE_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        SoundEvent soundEvent = this.isBaby() ? SoundEvents.ENTITY_TURTLE_SHAMBLE_BABY : SoundEvents.ENTITY_TURTLE_SHAMBLE;
        this.playSound(soundEvent, 0.15F, 1.0F);
    }

    public boolean canEat() {
        return super.canEat();
    }

    protected float calculateNextStepSoundDistance() {
        return this.distanceTraveled + 0.15F;
    }

    public float getScaleFactor() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    protected EntityNavigation createNavigation(World world) {
        return new SealEntity.SealSwimNavigation(this, world);
    }

    @Nullable
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (PassiveEntity)AlaskaNativeEntities.HARBOR_SEAL.create(world);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.SALMON;
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        if (world.getFluidState(pos).isIn(FluidTags.WATER)) {
            return 15.0F;
        } else {
            return world.getBlockState(pos).isOf(Blocks.SAND) ? 10.0F : world.getBrightness(pos) - 0.5F;
        }
    }

    public void tickMovement() {
        super.tickMovement();
    }

    protected void onGrowUp() {
        super.onGrowUp();
        if (!this.isBaby() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            this.dropItem(AlaskaNativeItems.MUKTUK, 1);
        }
    }

    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9D));
            if (this.getTarget() == null || !this.getHomePos().isWithinDistance(this.getPos(), 20.0D)) {
                this.setVelocity(this.getVelocity().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(movementInput);
        }

    }

    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }

    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        this.damage(DamageSource.LIGHTNING_BOLT, Float.MAX_VALUE);
    }

    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(DamageSource.mob(this), (float)((int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
        if (bl) {
            this.dealDamage(this, target);
        }

        return bl;
    }

    static {
        HOME_POS = DataTracker.registerData(SealEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
        TRAVEL_POS = DataTracker.registerData(SealEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
        ACTIVELY_TRAVELLING = DataTracker.registerData(SealEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    static class SealSwimNavigation extends SwimNavigation {
        SealSwimNavigation(SealEntity owner, World world) {
            super(owner, world);
        }

        protected boolean isAtValidPosition() {
            return true;
        }

        protected PathNodeNavigator createPathNodeNavigator(int range) {
            this.nodeMaker = new AmphibiousPathNodeMaker();
            return new PathNodeNavigator(this.nodeMaker, range);
        }

        public boolean isValidPosition(BlockPos pos) {
            if (this.entity instanceof SealEntity) {
                SealEntity sealEntity = (SealEntity)this.entity;
                if (sealEntity.isActivelyTravelling()) {
                    return this.world.getBlockState(pos).isOf(Blocks.WATER);
                }
            }

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
                if (!this.seal.getHomePos().isWithinDistance(this.seal.getPos(), 16.0D)) {
                    this.seal.setMovementSpeed(Math.max(this.seal.getMovementSpeed() / 1.5F, 0.1F));
                }

                if (this.seal.isBaby()) {
                    this.seal.setMovementSpeed(Math.max(this.seal.getMovementSpeed() / 2.0F, 0.08F));
                }
            } else if (this.seal.onGround) {
                this.seal.setMovementSpeed(Math.max(this.seal.getMovementSpeed() / 1.5F, 0.08F));
            }

        }

        public void tick() {
            this.updateVelocity();
            if (this.state == MoveControl.State.MOVE_TO && !this.seal.getNavigation().isIdle()) {
                double d = this.targetX - this.seal.getX();
                double e = this.targetY - this.seal.getY();
                double f = this.targetZ - this.seal.getZ();
                double g = (double)MathHelper.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float)(MathHelper.atan2(f, d) * 57.2957763671875D) - 90.0F;
                this.seal.yaw = this.changeAngle(this.seal.yaw, h, 90.0F);
                this.seal.bodyYaw = this.seal.yaw;
                this.seal.headYaw = this.seal.yaw;
                float i = (float)(this.speed * this.seal.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                this.seal.setMovementSpeed(MathHelper.lerp(0.125F, this.seal.getMovementSpeed(), i));
                this.seal.setVelocity(this.seal.getVelocity().add(0.0D, (double)this.seal.getMovementSpeed() * e * 0.1D, 0.0D));
            } else {
                this.seal.setMovementSpeed(0.0F);
            }
        }
    }

    static class WanderInWaterGoal extends MoveToTargetPosGoal {
        private final SealEntity seal;

        private WanderInWaterGoal(SealEntity seal, double speed) {
            super(seal, seal.isBaby() ? 2.0D : speed, 24);
            this.seal = seal;
            this.lowestY = -1;
        }

        public boolean shouldContinue() {
            return !this.seal.isTouchingWater() && this.tryingTime <= 1200 && this.isTargetPos(this.seal.world, this.targetPos);
        }

        public boolean canStart() {
            if (this.seal.isBaby() && !this.seal.isTouchingWater()) {
                return super.canStart();
            } else {
                return !this.seal.isTouchingWater() && super.canStart();
            }
        }

        public boolean shouldResetPath() {
            return this.tryingTime % 160 == 0;
        }

        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            return world.getBlockState(pos).isOf(Blocks.WATER);
        }
    }

    static class WanderOnLandGoal extends WanderAroundGoal {
        private final SealEntity seal;

        private WanderOnLandGoal(SealEntity seal, double speed, int chance) {
            super(seal, speed, chance);
            this.seal = seal;
        }

        public boolean canStart() {
            return !this.mob.isTouchingWater() && super.canStart();
        }
    }

    static class MateGoal extends AnimalMateGoal {
        private final SealEntity seal;

        MateGoal(SealEntity seal, double speed) {
            super(seal, speed);
            this.seal = seal;
        }

        protected void breed() {
            ServerPlayerEntity serverPlayerEntity = this.animal.getLovingPlayer();
            if (serverPlayerEntity == null && this.mate.getLovingPlayer() != null) {
                serverPlayerEntity = this.mate.getLovingPlayer();
            }

            if (serverPlayerEntity != null) {
                serverPlayerEntity.incrementStat(Stats.ANIMALS_BRED);
                Criteria.BRED_ANIMALS.trigger(serverPlayerEntity, this.animal, this.mate, (PassiveEntity)null);
            }

            this.animal.resetLoveTicks();
            this.mate.resetLoveTicks();
            Random random = this.animal.getRandom();
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
            }

        }
    }

    static class ApproachFoodHoldingPlayerGoal extends Goal {
        private static final TargetPredicate CLOSE_ENTITY_PREDICATE = (new TargetPredicate()).setBaseMaxDistance(10.0D).includeTeammates().includeInvulnerable();
        private final SealEntity seal;
        private final double speed;
        private PlayerEntity targetPlayer;
        private int cooldown;
        private final Set<Item> attractiveItems;

        ApproachFoodHoldingPlayerGoal(SealEntity seal, double speed, Item attractiveItem) {
            this.seal = seal;
            this.speed = speed;
            this.attractiveItems = Sets.newHashSet(new Item[]{attractiveItem});
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        public boolean canStart() {
            if (this.cooldown > 0) {
                --this.cooldown;
                return false;
            } else {
                this.targetPlayer = this.seal.world.getClosestPlayer(CLOSE_ENTITY_PREDICATE, this.seal);
                if (this.targetPlayer == null) {
                    return false;
                } else {
                    return this.isAttractive(this.targetPlayer.getMainHandStack()) || this.isAttractive(this.targetPlayer.getOffHandStack());
                }
            }
        }

        private boolean isAttractive(ItemStack stack) {
            return this.attractiveItems.contains(stack.getItem());
        }

        public boolean shouldContinue() {
            return this.canStart();
        }

        public void stop() {
            this.targetPlayer = null;
            this.seal.getNavigation().stop();
            this.cooldown = 100;
        }

        public void tick() {
            this.seal.getLookControl().lookAt(this.targetPlayer, (float)(this.seal.getBodyYawSpeed() + 20), (float)this.seal.getLookPitchSpeed());
            if (this.seal.squaredDistanceTo(this.targetPlayer) < 6.25D) {
                this.seal.getNavigation().stop();
            } else {
                this.seal.getNavigation().startMovingTo(this.targetPlayer, this.speed);
            }

        }
    }

    static class TravelGoal extends Goal {
        private final SealEntity seal;
        private final double speed;
        private boolean noPath;

        TravelGoal(SealEntity seal, double speed) {
            this.seal = seal;
            this.speed = speed;
        }

        public boolean canStart() {
            return this.seal.isTouchingWater();
        }

        public void start() {
            boolean i = true;
            boolean j = true;
            Random random = this.seal.random;
            int k = random.nextInt(1025) - 512;
            int l = random.nextInt(9) - 4;
            int m = random.nextInt(1025) - 512;
            if ((double)l + this.seal.getY() > (double)(this.seal.world.getSeaLevel() - 1)) {
                l = 0;
            }

            BlockPos blockPos = new BlockPos((double)k + this.seal.getX(), (double)l + this.seal.getY(), (double)m + this.seal.getZ());
            this.seal.setTravelPos(blockPos);
            this.seal.setActivelyTravelling(true);
            this.noPath = false;
        }

        public void tick() {
            if (this.seal.getNavigation().isIdle()) {
                Vec3d vec3d = Vec3d.ofBottomCenter(this.seal.getTravelPos());
                Vec3d vec3d2 = TargetFinder.findTargetTowards(this.seal, 16, 3, vec3d, 0.3141592741012573D);
                if (vec3d2 == null) {
                    vec3d2 = TargetFinder.findTargetTowards(this.seal, 8, 7, vec3d);
                }

                if (vec3d2 != null) {
                    int i = MathHelper.floor(vec3d2.x);
                    int j = MathHelper.floor(vec3d2.z);
                    boolean k = true;
                    if (!this.seal.world.isRegionLoaded(i - 34, 0, j - 34, i + 34, 0, j + 34)) {
                        vec3d2 = null;
                    }
                }

                if (vec3d2 == null) {
                    this.noPath = true;
                    return;
                }

                this.seal.getNavigation().startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, this.speed);
            }

        }

        public boolean shouldContinue() {
            return !this.seal.getNavigation().isIdle() && !this.noPath && !this.seal.isInLove();
        }

        public void stop() {
            this.seal.setActivelyTravelling(false);
            super.stop();
        }
    }

    static class SealEscapeDangerGoal extends EscapeDangerGoal {
        SealEscapeDangerGoal(SealEntity seal, double speed) {
            super(seal, speed);
        }

        public boolean canStart() {
            if (this.mob.getAttacker() == null && !this.mob.isOnFire()) {
                return false;
            } else {
                BlockPos blockPos = this.locateClosestWater(this.mob.world, this.mob, 16, 4);
                if (blockPos != null) {
                    this.targetX = (double)blockPos.getX();
                    this.targetY = (double)blockPos.getY();
                    this.targetZ = (double)blockPos.getZ();
                    return true;
                } else {
                    return this.findTarget();
                }
            }
        }
    }
}
