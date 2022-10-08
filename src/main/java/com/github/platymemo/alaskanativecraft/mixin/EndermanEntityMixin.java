package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity implements Angerable {
	protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		throw new AssertionError("Mixin constructor called, something is very wrong!");
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 0),
			method = "isPlayerStaring(Lnet/minecraft/entity/player/PlayerEntity;)Z",
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true)
	private void isPlayerWearingSnowGoggles(PlayerEntity player, CallbackInfoReturnable<Boolean> cir, @NotNull ItemStack stack) {
		if (stack.isOf(AlaskaItems.SNOW_GOGGLES.asItem())) {
			cir.setReturnValue(false);
		}
	}
}
