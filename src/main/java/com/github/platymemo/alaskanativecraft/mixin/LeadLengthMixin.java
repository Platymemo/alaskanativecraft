package com.github.platymemo.alaskanativecraft.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PathAwareEntity.class)
public abstract class LeadLengthMixin extends MobEntity {

    protected LeadLengthMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    /*
     * Multiplies the max lead length by 4.8, making it 48 blocks max
     */
    @ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PathAwareEntity;updateForLeashLength(F)V", shift = At.Shift.AFTER), method = "updateLeash")
    public float multiplyLeashLength(float f) {
        f /= 4.8F;
        return f;
    }

    /*
     * Reverts the lead length to its proper size for more reasonable pulling calculations
     */
    @ModifyVariable(at = @At(value = "JUMP", opcode = Opcodes.IFLE, ordinal = 2, shift = At.Shift.BY, by = -3), method = "updateLeash")
    public float revertLeashLength(float f) {
        f *= 4.8F;
        return f;
    }
}