package com.github.platymemo.alaskanativecraft.block;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * Base class for berry bushes.
 * Contains common code for berry harvesting and entity collision.
 *
 * @author Platymemo, ix0rai
 */
public class BushBlock extends SweetBerryBushBlock {
	public BushBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(this);
	}

	@Override
	public ActionResult onUse(@NotNull BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int i = state.get(AGE);
		boolean bl = i == 3;
		if (!bl && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
			return ActionResult.PASS;
		} else if (i > 1) {
			int j = 1 + world.random.nextInt(2);
			dropStack(world, pos, new ItemStack(this.getPickStack(world, pos, state).getItem(), j + (bl ? 1 : 0)));
			world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			world.setBlockState(pos, state.with(AGE, 1), 2);
			return ActionResult.success(world.isClient);
		} else {
			return ActionResult.PASS;
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
			entity.setMovementMultiplier(state, new Vec3d(1.0D, 0.90D, 1.0D));
		}
	}
}
