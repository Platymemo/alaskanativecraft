package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError("wut");
    }

    @Inject(at=@At("HEAD"), method = "updateTurtleHelmet")
    private void updateSnowGoggles(CallbackInfo ci) {
        ItemStack stack = this.getEquippedStack(EquipmentSlot.HEAD);
        if (stack.getItem() == AlaskaItems.SNOW_GOGGLES) {
            if (this.getStatusEffect(StatusEffects.BLINDNESS) != null) {
                this.removeStatusEffect(StatusEffects.BLINDNESS);
            }
        }
    }
}
