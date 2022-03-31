package com.github.platymemo.alaskanativecraft.block;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class CloudberryBushBlock extends BushBlock {
    private static final VoxelShape MID_SHAPE;
    private static final VoxelShape FULL_SHAPE;

    static {
        MID_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 9.0D, 15.0D);
        FULL_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
    }

    public CloudberryBushBlock(Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return AlaskaItems.CLOUDBERRIES.getDefaultStack();
    }

    @Override
    public VoxelShape getOutlineShape(@NotNull BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(AGE) > 0 && state.get(AGE) < 3) {
            return MID_SHAPE;
        } else if (state.get(AGE) == 3) {
            return FULL_SHAPE;
        } else {
            return super.getOutlineShape(state, world, pos, context);
        }
    }
}
