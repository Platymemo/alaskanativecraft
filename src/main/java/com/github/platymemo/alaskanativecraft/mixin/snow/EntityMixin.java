package com.github.platymemo.alaskanativecraft.mixin.snow;

import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Unique
    private static final float anc$SNOW_SLOW = 1.0f - AlaskaConfig.getConfig().snowSlow;

    @Shadow
    private World world;

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    protected abstract BlockPos getVelocityAffectingPos();

    @ModifyVariable(method = "getVelocityMultiplier", at = @At(value = "STORE", ordinal = 0))
    private float anc$ignoreSmallSnowLayers(float oldFloat) {
        BlockState blockState = this.world.getBlockState(this.getBlockPos());
        if (blockState.isOf(Blocks.SNOW) && blockState.get(SnowBlock.LAYERS) < 5) {
            return MathHelper.lerp((blockState.get(SnowBlock.LAYERS) - 1.0f) / 3.0f, 1.0f, anc$SNOW_SLOW);
        }

        return oldFloat;
    }

    @Inject(method = "getVelocityMultiplier", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void anc$ignoreSmallSnowLayers2ElectricBoogaloo(CallbackInfoReturnable<Float> cir) {
        BlockState blockState = this.world.getBlockState(this.getVelocityAffectingPos());
        if (blockState.isOf(Blocks.SNOW) && blockState.get(SnowBlock.LAYERS) < 5) {
            cir.setReturnValue(MathHelper.lerp((blockState.get(SnowBlock.LAYERS) - 1.0f) / 3.0f, 1.0f, anc$SNOW_SLOW));
        }
    }
}
