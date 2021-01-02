package com.github.platymemo.alaskanativecraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.world.World;

public class PtarmiganEntity extends ParrotEntity {
    public PtarmiganEntity(EntityType<? extends ParrotEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean imitateNearbyMob(World world, Entity parrot) {
        return false;
    }

    public static DefaultAttributeContainer.Builder createPtarmiganAttributes() {
        return PtarmiganEntity.createMobAttributes().
                add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D).
                add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4000000059604645D).
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224D);
    }
}
