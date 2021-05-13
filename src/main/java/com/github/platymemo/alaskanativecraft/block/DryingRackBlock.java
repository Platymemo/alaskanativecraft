package com.github.platymemo.alaskanativecraft.block;

import com.github.platymemo.alaskanativecraft.block.entity.DryingRackBlockEntity;
import com.github.platymemo.alaskanativecraft.recipe.DryingRecipe;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DryingRackBlock extends BlockWithEntity implements Waterloggable {
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty CONNECTED_POS;
    public static final BooleanProperty CONNECTED_NEG;
    public static final EnumProperty<Direction.Axis> AXIS;
    private static final VoxelShape X_SHAPE = Block.createCuboidShape(0, 0, 6, 16, 8, 10);
    private static final VoxelShape Z_SHAPE = Block.createCuboidShape(6, 0, 0, 10, 8, 16);

    public DryingRackBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(WATERLOGGED, false)
                .with(CONNECTED_POS, false)
                .with(CONNECTED_NEG, false)
                .with(AXIS, Direction.Axis.Z));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!world.isClient && blockEntity instanceof DryingRackBlockEntity) {
            DryingRackBlockEntity dryingRackBlockEntity = (DryingRackBlockEntity) blockEntity;
            ItemStack itemStack = player.getStackInHand(hand);
            Optional<DryingRecipe> optional = dryingRackBlockEntity.getRecipeFor(itemStack);
            if (optional.isPresent()) {
                if (dryingRackBlockEntity.addItem(player.abilities.creativeMode ? itemStack.copy() : itemStack, (optional.get()).getCookTime())) {
                    // TODO Drying rack interaction stats
                    return ActionResult.SUCCESS;
                }
                return ActionResult.CONSUME;
            } else {
                ItemStack dryingRackItem = dryingRackBlockEntity.getDriedItem();
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), dryingRackItem);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DryingRackBlockEntity) {
                ItemScatterer.spawn(world, pos, ((DryingRackBlockEntity)blockEntity).getItemsBeingDried());
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(AXIS) == Direction.Axis.X) {
            return X_SHAPE;
        } else {
            return Z_SHAPE;
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        boolean bl = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER;
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();

        BlockState state = this.getDefaultState();

        Direction direction = ctx.getPlayerFacing();
        Direction.Axis axis = direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        Direction left = Direction.get(Direction.AxisDirection.NEGATIVE, axis);
        Direction right = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return state.with(AXIS, axis)
                    .with(CONNECTED_NEG, this.canConnect(left, world.getBlockState(blockPos.offset(left))))
                    .with(CONNECTED_POS, this.canConnect(right, world.getBlockState(blockPos.offset(right))))
                    .with(WATERLOGGED, bl);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch(rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch(state.get(AXIS)) {
                    case X:
                        return state.with(AXIS, Direction.Axis.Z);
                    case Z:
                        return state.with(AXIS, Direction.Axis.X);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    private boolean canConnect(Direction direction, BlockState otherState) {
        if (otherState.isIn(BlockTags.LOGS) || otherState.isIn(BlockTags.WALLS) || otherState.isIn(BlockTags.FENCES) || otherState.isIn(BlockTags.FENCE_GATES)) {
            return true;
        } else if (otherState.isOf(this)) {
            return otherState.get(AXIS) == direction.getAxis();
        } else {
            return false;
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return state.get(AXIS) == direction.getAxis() ?
                state.with(direction.getDirection() == Direction.AxisDirection.POSITIVE ? CONNECTED_POS : CONNECTED_NEG, this.canConnect(direction, newState))
                : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!(Boolean)state.get(Properties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DryingRackBlockEntity) {
                ((DryingRackBlockEntity)blockEntity).spawnItemsBeingDried();
            }

            world.setBlockState(pos, state.with(WATERLOGGED, true), 3);
            world.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new DryingRackBlockEntity();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED).add(CONNECTED_POS).add(CONNECTED_NEG).add(AXIS);
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
        CONNECTED_POS = BooleanProperty.of("positive");
        CONNECTED_NEG = BooleanProperty.of("negative");
        AXIS = EnumProperty.of("axis", Direction.Axis.class, Direction.Axis::isHorizontal);
    }
}
