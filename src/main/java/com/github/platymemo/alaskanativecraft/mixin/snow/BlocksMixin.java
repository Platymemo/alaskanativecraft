package com.github.platymemo.alaskanativecraft.mixin.snow;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;

/**
 * Makes {@link Blocks#SNOW} and {@link Blocks#SNOW_BLOCK} slow entities down.
 */
@Mixin(Blocks.class)
public class BlocksMixin {
	@Unique
	private static final float anc$SNOW_SLOW = 1.0f - AlaskaNativeCraft.CONFIG.snowSlow.value();

	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SnowBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
	private static AbstractBlock.Settings anc$addSlowToSnowSettings(AbstractBlock.Settings oldSettings) {
		return oldSettings.velocityMultiplier(anc$SNOW_SLOW);
	}

	// TODO very brittle selection, can easily break
	// We may want to use a slice
	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V", ordinal = 48))
	private static AbstractBlock.Settings anc$addSlowToSnowBlockSettings(AbstractBlock.Settings oldSettings) {
		return oldSettings.velocityMultiplier(anc$SNOW_SLOW);
	}
}
