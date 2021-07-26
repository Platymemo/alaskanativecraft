package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.entity.ai.goal.ChildEscapeDangerGoal;
import com.github.platymemo.alaskanativecraft.mixin.AxeItemAccessor;
import com.github.platymemo.alaskanativecraft.mixin.MeleeAttackGoalAccessor;
import com.github.platymemo.alaskanativecraft.sound.AlaskaSoundEvents;
import com.github.platymemo.alaskanativecraft.tags.common.CommonBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;

public class MooseEntity extends AnimalEntity {
    private static final AlaskaConfig config = AlaskaConfig.getConfig();

    protected MooseEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.calculateDimensions();
        this.stepHeight = this.isBaby() ? 1.0F : 2.0F;
    }

    public static DefaultAttributeContainer.Builder createMooseAttributes() {
        return MooseEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new ChildEscapeDangerGoal(this, 2.5D));
        this.goalSelector.add(1, new AdultMeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.WHEAT), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new MooseEntity.EatBarkGoal(this, 2.25D, 0.2D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(0, new RevengeGoal(this).setGroupRevenge());
    }

    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(100) > 75 ? AlaskaSoundEvents.ENTITY_MOOSE_AMBIENT : super.getAmbientSound();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return AlaskaSoundEvents.ENTITY_MOOSE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return AlaskaSoundEvents.ENTITY_MOOSE_HURT;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    public MooseEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return AlaskaEntities.MOOSE.create(serverWorld);
    }

    protected void onGrowUp() {
        super.onGrowUp();
        this.stepHeight = 2.0F;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.95F;
    }

    class AdultMeleeAttackGoal extends MeleeAttackGoal {

        public AdultMeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public boolean canStart() {
            if (!mob.isBaby()) {
                return super.canStart();
            } else {
                return false;
            }
        }

        @Override
        protected void resetCooldown() {
            ((MeleeAttackGoalAccessor) this).setCooldown(50);
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            float f = MooseEntity.this.getWidth() - 0.1F;
            return (f * 2.0F * f * 2.0F + entity.getWidth());
        }
    }

    class EatBarkGoal extends Goal {
        private final double distance;
        private final double speed;
        protected BlockPos logPos;
        protected BlockState logState;
        protected boolean logValid;
        private Vec3d target;

        public EatBarkGoal(MooseEntity moose, double distance, double speed) {
            this.logPos = BlockPos.ORIGIN;
            this.distance = distance;
            this.speed = speed;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
        }

        protected void setLogStripped() {
            World world = MooseEntity.this.world;
            if (this.logValid) {
                this.logState = world.getBlockState(this.logPos);
                if (this.logState.isIn(CommonBlockTags.LOGS_WITH_BARK)) {
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
                this.logPos = new BlockPos(vec3d);
                return true;
            }
        }

        @Nullable
        protected Vec3d locateLogPos() {
            BlockPos blockPos = MooseEntity.this.getBlockPos();
            Iterable<BlockPos> iterable = BlockPos.iterate(MathHelper.floor(MooseEntity.this.getX() - 3.0D),
                    MathHelper.floor(MooseEntity.this.getY() - 6.0D),
                    MathHelper.floor(MooseEntity.this.getZ() - 3.0D),
                    MathHelper.floor(MooseEntity.this.getX() + 3.0D),
                    MathHelper.floor(MooseEntity.this.getY() + 6.0D),
                    MathHelper.floor(MooseEntity.this.getZ() + 3.0D));
            Iterator<BlockPos> position = iterable.iterator();

            BlockPos blockPos2;
            boolean bl;
            do {
                do {
                    if (!position.hasNext()) {
                        return null;
                    }

                    blockPos2 = position.next();
                } while (blockPos.equals(blockPos2));

                bl = MooseEntity.this.world.getBlockState(blockPos2).isIn(CommonBlockTags.LOGS_WITH_BARK);
            } while (!bl);

            return Vec3d.ofBottomCenter(blockPos2);
        }

        public boolean canStart() {
            if (!config.mooseEatBark) {
                return false;
            }
            if (!MooseEntity.this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                return false;
            } else if (!NavigationConditions.hasMobNavigation(MooseEntity.this)) {
                return false;
            } else if (MooseEntity.this.getTarget() != null) {
                return false;
            } else {
                if (MooseEntity.this.isBaby() || MooseEntity.this.getRandom().nextInt(200) != 0) {
                    return false;
                } else {
                    return this.targetLogPos();
                }
            }
        }

        public boolean shouldContinue() {
            return !MooseEntity.this.getNavigation().isIdle();
        }

        public void start() {
            MooseEntity.this.getNavigation().startMovingTo(this.target.x, this.target.y, this.target.z, this.speed);
        }

        public void tick() {
            double h = MooseEntity.this.getPos().distanceTo(this.target);
            if (h < this.distance) {
                MooseEntity.this.getLookControl().lookAt(this.target);
            }
        }

        public void stop() {
            setLogStripped();
        }
    }
}
