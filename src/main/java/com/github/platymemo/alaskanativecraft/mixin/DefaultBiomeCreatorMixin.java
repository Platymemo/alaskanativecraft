package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.AlaskaNativeEntities;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DefaultBiomeCreator.class)
public class DefaultBiomeCreatorMixin {

    @Inject(at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBatsAndMonsters(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V"),
            method = "createFrozenOcean",
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void addSealsAndPtarmigansOnIcebergs(boolean monument, CallbackInfoReturnable<Biome> ci, SpawnSettings.Builder builder) {
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(AlaskaNativeEntities.HARBOR_SEAL, 2, 1, 5));
        builder.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(AlaskaNativeEntities.PTARMIGAN, 5, 1, 3));
    }

    @Inject(at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/gen/feature/DefaultBiomeFeatures;addBatsAndMonsters(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V"),
            method = "createTaiga",
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void addPtarmigansToTaigas(float depth, float scale, boolean bl, boolean bl2, boolean bl3, boolean bl4, CallbackInfoReturnable<Biome> ci, SpawnSettings.Builder builder) {
        builder.spawn(SpawnGroup.AMBIENT, new SpawnSettings.SpawnEntry(AlaskaNativeEntities.PTARMIGAN, 5, 2, 5));
    }
}