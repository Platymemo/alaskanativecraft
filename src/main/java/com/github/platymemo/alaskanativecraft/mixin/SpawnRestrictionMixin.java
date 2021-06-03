package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SpawnRestriction.class)
public class SpawnRestrictionMixin {
    @Shadow
    @Final
    private static Map<EntityType<?>, SpawnRestriction.Entry> RESTRICTIONS;

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void registerAlaskaSpawnRestrictions(CallbackInfo cbi)
    {
        AlaskaEntities.registerSpawnRestrictions(RESTRICTIONS);
    }
}
