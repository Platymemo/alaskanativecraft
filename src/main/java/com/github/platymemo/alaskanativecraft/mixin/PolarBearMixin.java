package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.SealEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PolarBearEntity.class)
public abstract class PolarBearMixin extends AnimalEntity implements Angerable {

    protected PolarBearMixin(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("TAIL"), method = "initGoals()V")
    private void addSealTarget(CallbackInfo ci) {
        targetSelector.add(3, new TargetGoal<>(this, SealEntity.class, true));
    }
}
