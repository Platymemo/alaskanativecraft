package com.github.platymemo.alaskanativecraft.mixin.snow;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin(SnowBlock.class)
public abstract class SnowBlockMixin extends AbstractBlockMixin {
	@Shadow
	@Final
	protected static VoxelShape[] LAYERS_TO_SHAPE;

	@Shadow
	@Final
	public static IntProperty LAYERS;

	@Shadow
	@Final
	public static int MAX_LAYERS;

	@Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
	private void anc$fallThroughSnow(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (state.isOf(Blocks.SNOW)) {
			if (context instanceof EntityShapeContext entityShapeContext) {
				Entity entity = entityShapeContext.getEntity();
				if (entityShapeContext == ShapeContext.absent()
						|| (entity != null && PowderSnowBlock.canWalkOnPowderSnow(entity) && context.isAbove(LAYERS_TO_SHAPE[state.get(LAYERS)], pos, false) && !context.isDescending())) {
					cir.setReturnValue(LAYERS_TO_SHAPE[state.get(LAYERS)]);
					return;
				}
			}

			// Smoothly modify collision shape for snow layers on top
			var aboveState = world.getBlockState(pos.up());
			if (aboveState.isOf(Blocks.SNOW)) {
				cir.setReturnValue(LAYERS_TO_SHAPE[Math.min(MAX_LAYERS, 4 + aboveState.get(SnowBlock.LAYERS))]);
				return;
			}

			cir.setReturnValue(LAYERS_TO_SHAPE[Math.max(0, state.get(LAYERS) - 4)]);
		}
	}

	@Override
	protected void anc$onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		if (state.isOf(Blocks.SNOW)) {
			this.anc$snowEffects(state, world, pos, entity, ((float) state.get(LAYERS) / MAX_LAYERS));
		}
	}
}
