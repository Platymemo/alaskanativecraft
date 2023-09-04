package com.github.platymemo.alaskanativecraft.entity.damage;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class AlaskaDamageTypes {
    public static final RegistryKey<DamageType> HARPOON = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("harpoon"));

    public static void init() {
        // For classloading
    }
}
