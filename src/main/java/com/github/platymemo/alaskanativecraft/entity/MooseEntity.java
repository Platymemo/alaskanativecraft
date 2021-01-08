package com.github.platymemo.alaskanativecraft.entity;

import com.github.platymemo.alaskanativecraft.config.AlaskaNativeConfig;
import com.github.platymemo.alaskanativecraft.sound.AlaskaNativeSoundEvents;
import com.github.platymemo.alaskanativecraft.tags.common.CommonBlockTags;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.class_5493;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

public class MooseEntity extends AnimalEntity {
    private static AlaskaNativeConfig config = AlaskaNativeConfig.getConfig();
    public static final EntityDimensions ADULT = EntityDimensions.fixed(3.0F, 2.6F);
    public static final EntityDimensions CALF = EntityDimensions.fixed(1.5F, 1.3F);

    protected MooseEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.calculateDimensions();
        this.stepHeight = this.isBaby() ? 1.0F : 2.0F;
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2.5D));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.WHEAT), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new MooseEntity.EatBarkGoal(this, 2.25D, 0.2D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    protected SoundEvent getAmbientSound() {
        return this.random.nextInt(100) > 75 ? AlaskaNativeSoundEvents.ENTITY_MOOSE_AMBIENT : super.getAmbientSound();
    }

    protected SoundEvent getHurtSound(DamageSource source) { return AlaskaNativeSoundEvents.ENTITY_MOOSE_HURT; }

    protected SoundEvent getDeathSound() {
        return AlaskaNativeSoundEvents.ENTITY_MOOSE_HURT;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return super.interactMob(player, hand);
    }

    public MooseEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return AlaskaNativeEntities.MOOSE.create(serverWorld);
    }

    public static DefaultAttributeContainer.Builder createMooseAttributes() {
        return MooseEntity.createMobAttributes().
                add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D).
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224D);
    }

    protected void onGrowUp() {
        super.onGrowUp();
        this.stepHeight = 2.0F;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.95F;
    }

    public EntityDimensions getDimensions(EntityPose pose) {
        return this.isBaby() ? CALF : ADULT;
    }

    static class EatBarkGoal extends Goal {
        protected MooseEntity moose;
        protected BlockPos logPos;
        protected BlockState logState;
        protected boolean logValid;
        private static final Map<Block, Block> STRIPPED_BLOCKS;
        private double distance;
        private double speed;
        private Vec3d target;

        public EatBarkGoal(MooseEntity moose, double distance, double speed) {
            this.logPos = BlockPos.ORIGIN;
            this.moose = moose;
            this.distance = distance;
            this.speed = speed;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));

            // Makes sure the moose actually can navigate, which should always be true
            if (!class_5493.method_30955(moose)) {
                throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
            }
        }

        protected void setLogStripped() {
            World world = this.moose.world;
            if (this.logValid) {
                this.logState = world.getBlockState(this.logPos);
                if (this.logState.getBlock().isIn(CommonBlockTags.LOGS_WITH_BARK)) {
                    BlockState blockState = world.getBlockState(this.logPos);
                    Block block = STRIPPED_BLOCKS.get(blockState.getBlock());
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
            BlockPos blockPos = this.moose.getBlockPos();
            Iterable<BlockPos> iterable = BlockPos.iterate(MathHelper.floor(this.moose.getX() - 3.0D),
                                                           MathHelper.floor(this.moose.getY() - 6.0D),
                                                           MathHelper.floor(this.moose.getZ() - 3.0D),
                                                           MathHelper.floor(this.moose.getX() + 3.0D),
                                                           MathHelper.floor(this.moose.getY() + 6.0D),
                                                           MathHelper.floor(this.moose.getZ() + 3.0D));
            Iterator<BlockPos> position = iterable.iterator();

            BlockPos blockPos2;
            boolean bl;
            do {
                do {
                    if (!position.hasNext()) {
                        return null;
                    }

                    blockPos2 = position.next();
                } while(blockPos.equals(blockPos2));

                Block block = this.moose.world.getBlockState(blockPos2).getBlock();
                bl = block.isIn(CommonBlockTags.LOGS_WITH_BARK);
            } while(!bl);

            return Vec3d.ofBottomCenter(blockPos2);
        }

        public boolean canStart() {
            if (!config.mooseEatBark) {
                return false;
            }
            if (!this.moose.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                return false;
            } else if (!class_5493.method_30955(this.moose)) {
                return false;
            } else if (this.moose.getTarget() != null) {
                return false;
            } else {
                if (this.moose.isBaby() || this.moose.getRandom().nextInt(200) != 0) {
                    return false;
                } else {
                    return this.targetLogPos();
                }
            }
        }

        public boolean shouldContinue() {
            return !this.moose.getNavigation().isIdle();
        }

        public void start() {
            this.moose.getNavigation().startMovingTo(this.target.x, this.target.y, this.target.z, this.speed);
        }

        public void tick() {
            double h = this.moose.getPos().distanceTo(this.target);
            if (h < this.distance) {
                this.moose.getLookControl().lookAt(this.target);
            }
        }

        public void stop() {
            setLogStripped();
        }

        static {
            STRIPPED_BLOCKS = (new ImmutableMap.Builder()).put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).build();
        }
    }
}
