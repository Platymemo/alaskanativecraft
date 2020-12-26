package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.AlaskaNativeEntities;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {

    @Inject(at = @At("HEAD"), method = "addOceanMobs")
    private static void addSeals(SpawnSettings.Builder builder, int squidWeight, int squidMaxGroupSize, int codWeight, CallbackInfo ci) {
        builder.spawn(SpawnGroup.WATER_CREATURE, new SpawnSettings.SpawnEntry(AlaskaNativeEntities.HARBOR_SEAL, 3, 1, 4));
    }

    @Inject(at = @At("HEAD"), method = "addSnowyMobs")
    private static void addSealsAndPtarmigans(SpawnSettings.Builder builder, CallbackInfo ci) {
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(AlaskaNativeEntities.HARBOR_SEAL, 5, 1, 4));
        builder.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(AlaskaNativeEntities.PTARMIGAN, 25, 2, 6));
    }

}