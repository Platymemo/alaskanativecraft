package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.util.DugoutHelper;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    @Inject(at = @At("HEAD"), method = "onBlockAdded")
    private void dugoutCanoeCheck(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        Direction.Axis axis = DugoutHelper.getAxisOrNull(world, pos.down());
        if (axis != null) {
            DugoutHelper.createDugoutCanoe(axis, world, pos.down());
        }
    }
}
