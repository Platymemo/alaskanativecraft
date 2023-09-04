package com.github.platymemo.alaskanativecraft.mixin.snow;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    // A half-block in height
    @Unique
    private static final VoxelShape anc$SNOW_BLOCK_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void anc$snowCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (state.isOf(Blocks.SNOW_BLOCK)) {
            // Act like a full cube for falling blocks and entities that can walk on powder snow
            if (context instanceof EntityShapeContext entityShapeContext) {
                Entity entity = entityShapeContext.getEntity();
                if (entity instanceof FallingBlockEntity
                        || (entityShapeContext == ShapeContext.absent())
                        || (entity != null && PowderSnowBlock.canWalkOnPowderSnow(entity) && context.isAbove(VoxelShapes.fullCube(), pos, false) && !context.isDescending())) {
                    cir.setReturnValue(VoxelShapes.fullCube());
                    return;
                }
            }

            // Smoothly modify collision shape for snow layers on top of snow blocks
            var aboveState = world.getBlockState(pos.up());
            if (aboveState.isOf(Blocks.SNOW)) {
                cir.setReturnValue(SnowBlockAccessor.getLayers()[Math.min(SnowBlock.MAX_LAYERS, 4 + aboveState.get(SnowBlock.LAYERS))]);
                return;
            }

            cir.setReturnValue(anc$SNOW_BLOCK_SHAPE);
        }
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    protected void anc$onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (state.isOf(Blocks.SNOW_BLOCK)) {
            this.anc$snowEffects(state, world, pos, entity, 1);
        }
    }

    @Unique
    @SuppressWarnings("ConstantConditions")
    protected void anc$snowEffects(BlockState state, World world, BlockPos pos, Entity entity, double particleHeight) {
        if (!(entity instanceof LivingEntity) || entity.getBlockStateAtPos().getBlock() == (Object) this) {
            if (world.isClient) {
                RandomGenerator randomGenerator = world.getRandom();
                boolean movedHorizontally = entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ();
                if (movedHorizontally && randomGenerator.nextBoolean()) {
                    world.addParticle(
                            ParticleTypes.SNOWFLAKE,
                            entity.getX(),
                            pos.getY() + particleHeight,
                            entity.getZ(),
                            MathHelper.nextBetween(randomGenerator, -1.0F, 1.0F) * 0.083333336F,
                            0.05F,
                            MathHelper.nextBetween(randomGenerator, -1.0F, 1.0F) * 0.083333336F
                    );
                }
            }
        }

        if (!world.isClient) {
            if (entity.isOnFire() && (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) || entity instanceof PlayerEntity) && entity.canModifyAt(world, pos)) {
                world.breakBlock(pos, false);
            }

            entity.setOnFire(false);
        }
    }
}
