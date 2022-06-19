package com.github.platymemo.alaskanativecraft.mixin;

import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructureFeature.class)
public interface StructureFeatureAccessor {
    @Invoker("register")
    static <F extends StructureFeature<?>> F callRegister(String name, F structureFeature, GenerationStep.Feature step) {
        throw new AssertionError("Accessor dummy method called somehow!");
    }
}
