package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.feature.FallenTrunkPlacer;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.Set;

@Mixin(TreeFeature.class)
public class TreeFeatureMixin {
    @Unique
    private static boolean isDriftwood = false;

    @Inject(method = "generate(Lnet/minecraft/world/ModifiableTestableWorld;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Ljava/util/Set;Ljava/util/Set;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/world/gen/feature/TreeFeatureConfig;)Z",
    at = @At("HEAD"))
    private void setIsDriftwood(ModifiableTestableWorld world, Random random, BlockPos pos, Set<BlockPos> logPositions, Set<BlockPos> leavesPositions, BlockBox box, TreeFeatureConfig config, CallbackInfoReturnable<Boolean> cir) {
        isDriftwood = config.trunkPlacer instanceof FallenTrunkPlacer;
    }

    @Inject(method = "isDirtOrGrass", at = @At("HEAD"), cancellable = true)
    private static void isDriftwoodTreeGen(TestableWorld world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (isDriftwood && world.testBlockState(pos, (blockState -> blockState.isIn(BlockTags.SAND) || blockState.isIn(BlockTags.ICE)))) {
            cir.setReturnValue(true);
        }
        isDriftwood = false;
    }
}
