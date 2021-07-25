package com.github.platymemo.alaskanativecraft.mixin;

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
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private static final UUID SNOWSHOE_SPEED_BOOST_ID = UUID.fromString("0b824742-8bbc-4043-8caf-f997a543495f");

    @Shadow
    public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute genericMovementSpeed);
    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot feet);
    @Shadow
    public abstract Random getRandom();

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
        throw new AssertionError("wut");
    }

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
        return this.world.getBlockState(this.getVelocityAffectingPos()).isIn(AlaskaTags.SNOWSHOE_SPEED_BLOCKS);
    }
}
