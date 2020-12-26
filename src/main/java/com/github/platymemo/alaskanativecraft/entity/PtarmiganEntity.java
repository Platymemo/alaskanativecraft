package com.github.platymemo.alaskanativecraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.world.World;

public class PtarmiganEntity extends ParrotEntity {
    public PtarmiganEntity(EntityType<? extends ParrotEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean imitateNearbyMob(World world, Entity parrot) {
        return false;
    }
}
