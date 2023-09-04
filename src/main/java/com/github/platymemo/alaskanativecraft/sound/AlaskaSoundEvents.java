package com.github.platymemo.alaskanativecraft.sound;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class AlaskaSoundEvents {
    public static final SoundEvent ENTITY_SEAL_AMBIENT;
    public static final SoundEvent ENTITY_SEAL_AMBIENT_BABY;
    public static final SoundEvent ENTITY_SEAL_HURT;
    public static final SoundEvent ENTITY_MOOSE_AMBIENT;
    public static final SoundEvent ENTITY_MOOSE_HURT;

    static {
        ENTITY_SEAL_AMBIENT = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.seal.ambient"));
        ENTITY_SEAL_AMBIENT_BABY = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.seal.ambient.baby"));
        ENTITY_SEAL_HURT = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.seal.hurt"));
        ENTITY_MOOSE_AMBIENT = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.moose.ambient"));
        ENTITY_MOOSE_HURT = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.moose.hurt"));
    }

    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void register() {
    }
}
