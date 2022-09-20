package com.github.platymemo.alaskanativecraft.entity;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PtarmiganEntity extends ParrotEntity {
    private static final TrackedData<Integer> TYPE;

    static {
        TYPE = DataTracker.registerData(PtarmiganEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    public PtarmiganEntity(EntityType<? extends ParrotEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean isValidSpawn(EntityType<PtarmiganEntity> entityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, RandomGenerator randomGenerator) {
        BlockState blockState = serverWorldAccess.getBlockState(blockPos.down());
        return (blockState.isIn(BlockTags.LEAVES) || blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isIn(BlockTags.LOGS) || blockState.isOf(Blocks.AIR)) && serverWorldAccess.getBaseLightLevel(blockPos, 0) > 8;
    }

    public static DefaultAttributeContainer.Builder createPtarmiganAttributes() {
        return PtarmiganEntity.createMobAttributes().
                add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D).
                add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4000000059604645D).
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224D);
    }

    @Override
    public EntityData initialize(@NotNull ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityTag) {
        if (world.getBiome(getBlockPos()).hasTag(ConventionalBiomeTags.SNOWY) || world.getBiome(getBlockPos()).hasTag(ConventionalBiomeTags.CLIMATE_COLD)) {
            this.setType(0);
        } else {
            this.setType(this.random.nextInt(2) + 1);
        }
        if (entityData == null) {
            entityData = new PassiveEntity.PassiveData(false);
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    public PtarmiganEntity createChild(ServerWorld world, PassiveEntity entity) {
        PtarmiganEntity ptarmiganEntity = AlaskaEntities.PTARMIGAN.create(world);
        if (ptarmiganEntity != null)
            ptarmiganEntity.setType(this.random.nextBoolean() ? this.getPtarmiganType() : ((PtarmiganEntity) entity).getPtarmiganType());
        return ptarmiganEntity;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, 0);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    /**
     * This is so the Ptarmigan doesn't imitate mobs as parrots do
     */
    @Override
    public void tickMovement() {
        boolean silent = this.isSilent();
        this.setSilent(true);
        super.tickMovement();
        this.setSilent(silent);
    }

    public int getPtarmiganType() {
        return this.dataTracker.get(TYPE);
    }

    /*
     * Somehow a player spawned a ptarmigan with type = 3, so this is a safety check
     */
    private void setType(int type) {
        if (type >= 0 && type < 3) {
            this.dataTracker.set(TYPE, type);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("Type", this.getPtarmiganType());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setType(tag.getInt("Type"));
    }
}
