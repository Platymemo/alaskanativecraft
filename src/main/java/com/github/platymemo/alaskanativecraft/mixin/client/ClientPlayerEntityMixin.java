package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
	@Shadow
	public Input input;
	@Shadow
	private boolean riding;

	ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
		super(world, pos, yaw, gameProfile, publicKey);
		throw new AssertionError("Mixin constructor called, something is very wrong!");
	}

	@Inject(at = @At("TAIL"), method = "tickRiding()V")
	private void rideDogsled(CallbackInfo ci) {
		if (this.getVehicle() instanceof DogsledEntity dogsledEntity) {
			dogsledEntity.setInputs(this.input.pressingLeft, this.input.pressingRight, this.input.pressingForward, this.input.pressingBack);
			this.riding |= this.input.pressingLeft || this.input.pressingRight || this.input.pressingForward || this.input.pressingBack;
		}
	}
}
