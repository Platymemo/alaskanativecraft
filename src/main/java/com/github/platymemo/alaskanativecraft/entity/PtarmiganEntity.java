package com.github.platymemo.alaskanativecraft.entity;

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
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class PtarmiganEntity extends ParrotEntity {
    private static final TrackedData<Integer> TYPE;

    public PtarmiganEntity(EntityType<? extends ParrotEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean isValidSpawn(EntityType<PtarmiganEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockState blockState = world.getBlockState(pos.down());
        return (blockState.isIn(BlockTags.LEAVES) || blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isIn(BlockTags.LOGS) || blockState.isOf(Blocks.AIR)) && world.getBaseLightLevel(pos, 0) > 8;
    }

    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityTag) {
        if (world.getBiome(this.getBlockPos()).getTemperature() <= 0.2F) {
            this.setType(0);
        } else {
            this.setType(this.random.nextInt(2) + 1);
        }
        if (entityData == null) {
            entityData = new PassiveEntity.PassiveData(false);
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    public PtarmiganEntity createChild(ServerWorld world, PassiveEntity entity) {
        PtarmiganEntity ptarmiganEntity = AlaskaEntities.PTARMIGAN.create(world);
        if (ptarmiganEntity != null)
            ptarmiganEntity.setType(this.random.nextBoolean() ? this.getPtarmiganType() : ((PtarmiganEntity) entity).getPtarmiganType());
        return ptarmiganEntity;
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, 0);
    }

    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    public static DefaultAttributeContainer.Builder createPtarmiganAttributes() {
        return PtarmiganEntity.createMobAttributes().
                add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D).
                add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4000000059604645D).
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224D);
    }

    public int getPtarmiganType() {
        return this.dataTracker.get(TYPE);
    }

    private void setType(int type) {
        this.dataTracker.set(TYPE, type);
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

    static {
        TYPE = DataTracker.registerData(PtarmiganEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
