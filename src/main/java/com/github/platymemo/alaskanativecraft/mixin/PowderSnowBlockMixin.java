package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    // Also prevents leather boots from letting an entity walk on snow
    @Redirect(method = "canWalkOnPowderSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private static boolean anc$isWearingSnowshoes(ItemStack stack, Item item) {
        return stack.isOf(AlaskaItems.SNOWSHOES);
    }

    @Redirect(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setMovementMultiplier(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)V"))
    private void anc$dontSlowWithMukluks(Entity entity, BlockState state, Vec3d vec3d) {
        if (!(entity instanceof LivingEntity) || (AlaskaConfig.getConfig().snowOverhaul && ((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).isOf(AlaskaItems.MUKLUKS))) {
            return;
        }

        entity.setMovementMultiplier(state, vec3d);
    }
}
