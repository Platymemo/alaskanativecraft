package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.effect.AlaskaEffects;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private static final UUID SNOWSHOE_SPEED_BOOST_ID = UUID.fromString("0b824742-8bbc-4043-8caf-f997a543495f");

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
        throw new AssertionError("wut");
    }

    @Shadow
    public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute genericMovementSpeed);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot feet);

    @Shadow
    public abstract Random getRandom();

    @Shadow @Final private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;

    @Inject(method = "removeSoulSpeedBoost", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    protected void removeSnowshoeSpeedBoost(CallbackInfo ci, EntityAttributeInstance entityAttributeInstance) {
        if (entityAttributeInstance != null && entityAttributeInstance.getModifier(SNOWSHOE_SPEED_BOOST_ID) != null) {
            entityAttributeInstance.removeModifier(SNOWSHOE_SPEED_BOOST_ID);
        }
    }

    @Inject(method = "addSoulSpeedBoostIfNeeded", at = @At("TAIL"))
    protected void addSnowshoeSpeedBoostIfNeeded(CallbackInfo ci) {
        if (!this.getLandingBlockState().isAir()) {
            if (this.getEquippedStack(EquipmentSlot.FEET).isOf(AlaskaItems.SNOWSHOES) && this.isOnSnowshoeSpeedBlock()) {
                EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (entityAttributeInstance == null) {
                    return;
                }

                entityAttributeInstance.addTemporaryModifier(new EntityAttributeModifier(SNOWSHOE_SPEED_BOOST_ID, "Snowshoe speed boost", 0.025F, EntityAttributeModifier.Operation.ADDITION));
                if (this.getRandom().nextFloat() < 0.01F) {
                    ItemStack itemStack = this.getEquippedStack(EquipmentSlot.FEET);
                    itemStack.damage(1, (LivingEntity) (Object) this, player -> player.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                }
            }
        }
    }

    @Unique
    protected boolean isOnSnowshoeSpeedBlock() {
        BlockPos pos = this.getVelocityAffectingPos();
        return this.world.getBlockState(pos).isIn(AlaskaTags.SNOWSHOE_SPEED_BLOCKS) || this.world.getBlockState(pos.up()).isIn(AlaskaTags.SNOWSHOE_SPEED_BLOCKS);
    }

    /*
     * We have to tick the medicinal effect before all the others because we modify the list of active effects
     */
    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void tickMedicinalEffect(CallbackInfo ci) {
        if (this.activeStatusEffects.containsKey(AlaskaEffects.MEDICINAL)) {
            var statusEffect = this.activeStatusEffects.get(AlaskaEffects.MEDICINAL);
            var duration = statusEffect.getDuration();
            var amplifier = statusEffect.getAmplifier();
            if (duration % (80 >> amplifier) == 0) {
                statusEffect.getEffectType().applyUpdateEffect((LivingEntity) (Object) this, amplifier);
            }
        }
    }
}
