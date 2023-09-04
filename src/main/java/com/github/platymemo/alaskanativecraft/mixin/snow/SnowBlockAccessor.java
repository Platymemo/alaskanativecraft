package com.github.platymemo.alaskanativecraft.mixin.snow;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.SnowBlock;
import net.minecraft.util.shape.VoxelShape;

@Mixin(SnowBlock.class)
public interface SnowBlockAccessor {
    @Accessor("LAYERS_TO_SHAPE")
    @Final
    static VoxelShape[] getLayers() {
        throw new AssertionError("Mixin injection failed!");
    }
}
