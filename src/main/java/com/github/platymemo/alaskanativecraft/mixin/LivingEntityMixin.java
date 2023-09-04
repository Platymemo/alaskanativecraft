package com.github.platymemo.alaskanativecraft.mixin;

import java.util.Map;
import java.util.UUID;

import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.entity.effect.AlaskaEffects;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private static final UUID anc$SNOWSHOE_SPEED_BOOST_ID = UUID.fromString("0b824742-8bbc-4043-8caf-f997a543495f");
    @Unique
    private static final float anc$SNOWSHOE_SPEED_BOOST = 0.02f;
    @Shadow
    @Final
    private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute genericMovementSpeed);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot feet);

    @Shadow
    public abstract RandomGenerator getRandom();

    @Inject(method = "removeSoulSpeedBoost", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    protected void anc$removeSnowshoeSpeedBoost(CallbackInfo ci, EntityAttributeInstance entityAttributeInstance) {
        if (entityAttributeInstance != null && entityAttributeInstance.getModifier(anc$SNOWSHOE_SPEED_BOOST_ID) != null) {
            entityAttributeInstance.removeModifier(anc$SNOWSHOE_SPEED_BOOST_ID);
        }
    }

    @Inject(method = "addSoulSpeedBoostIfNeeded", at = @At("TAIL"))
    protected void anc$addSnowshoeSpeedBoostIfNeeded(CallbackInfo ci) {
        if (!this.getLandingBlockStateLegacy().isAir()) {
            if (this.getEquippedStack(EquipmentSlot.FEET).isOf(AlaskaItems.SNOWSHOES) && this.anc$isOnSnowshoeSpeedBlock()) {
                EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (entityAttributeInstance == null) {
                    return;
                }

                entityAttributeInstance.addTemporaryModifier(new EntityAttributeModifier(anc$SNOWSHOE_SPEED_BOOST_ID, "Snowshoe speed boost", anc$SNOWSHOE_SPEED_BOOST, EntityAttributeModifier.Operation.ADDITION));
                if (this.getRandom().nextFloat() < 0.01F) {
                    ItemStack itemStack = this.getEquippedStack(EquipmentSlot.FEET);
                    itemStack.damage(1, (LivingEntity) (Object) this, player -> player.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                }
            }
        }
    }

    @Unique
    protected boolean anc$isOnSnowshoeSpeedBlock() {
        BlockPos pos = this.getVelocityAffectingPos();
        return this.getWorld().getBlockState(pos).isIn(AlaskaTags.SNOWSHOE_SPEED_BLOCKS)
                || this.getWorld().getBlockState(pos.up()).isIn(AlaskaTags.SNOWSHOE_SPEED_BLOCKS);
    }

    @Inject(method = "getVelocityMultiplier", at = @At("HEAD"), cancellable = true)
    private void anc$getVelocityWithSnowBoost(CallbackInfoReturnable<Float> cir) {
        if (this.anc$isOnSnowshoeSpeedBlock()
                && (this.getEquippedStack(EquipmentSlot.FEET).isOf(AlaskaItems.SNOWSHOES)
                || (AlaskaConfig.getConfig().snowOverhaul
                && this.getEquippedStack(EquipmentSlot.FEET).isOf(AlaskaItems.MUKLUKS)))) {
            cir.setReturnValue(1.0f);
        }
    }

    /*
     * We have to tick the medicinal effect before all the others because we modify the list of active effects
     */
    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void anc$tickMedicinalEffect(CallbackInfo ci) {
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
