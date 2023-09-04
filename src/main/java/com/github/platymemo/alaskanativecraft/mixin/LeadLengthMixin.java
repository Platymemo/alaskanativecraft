package com.github.platymemo.alaskanativecraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

@Mixin(PathAwareEntity.class)
public abstract class LeadLengthMixin extends MobEntity {
    protected LeadLengthMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "updateLeash", constant = @Constant(floatValue = 10.0F))
    private float maxLeadLength(float maxLength) {
        return 48.0F;
    }
}
