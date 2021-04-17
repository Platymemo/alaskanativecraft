package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity implements Angerable {

    protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError("Mixin constructor called, something is very wrong!");
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", ordinal = 0),
            method = "isPlayerStaring(Lnet/minecraft/entity/player/PlayerEntity;)Z",
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void isPlayerWearingSnowGoggles(PlayerEntity player, CallbackInfoReturnable<Boolean> cir, ItemStack stack) {
        if (stack.getItem() == AlaskaItems.SNOW_GOGGLES) {
            cir.setReturnValue(false);
        }
    }
}
