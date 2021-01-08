package com.github.platymemo.alaskanativecraft.sound;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlaskaNativeSoundEvents {
    public static final SoundEvent ENTITY_SEAL_AMBIENT;
    public static final SoundEvent ENTITY_SEAL_AMBIENT_BABY;
    public static final SoundEvent ENTITY_SEAL_HURT;
    public static final SoundEvent ENTITY_MOOSE_AMBIENT;
    public static final SoundEvent ENTITY_MOOSE_HURT;

    public static SoundEvent register(Identifier id) {
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void register() {}

    static {
        ENTITY_SEAL_AMBIENT = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.seal.ambient"));
        ENTITY_SEAL_AMBIENT_BABY = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.seal.ambient.baby"));
        ENTITY_SEAL_HURT = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.seal.hurt"));
        ENTITY_MOOSE_AMBIENT = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.moose.ambient"));
        ENTITY_MOOSE_HURT = register(new Identifier(AlaskaNativeCraft.MOD_ID, "entity.moose.hurt"));
    }
}
