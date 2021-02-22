package com.github.platymemo.alaskanativecraft.config;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;

@Config(name = AlaskaNativeCraft.MOD_ID)
public class AlaskaConfig implements ConfigData {
    public boolean snowballConversion = false;
    public boolean mooseEatBark = true;
    @ConfigEntry.Gui.CollapsibleObject
    public SealFishing sealFishing = new SealFishing();

    public static class SealFishing {
        public boolean sealsHuntFish = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean sealsEatHuntedFish = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean sealsBreedFromHuntedFish = false;
    }

    @ConfigEntry.Gui.Excluded
    private transient static boolean registered = false;

    public static synchronized AlaskaConfig getConfig() {
        if (!registered) {
            AutoConfig.register(AlaskaConfig.class, GsonConfigSerializer::new);
            registered = true;
        }

        return AutoConfig.getConfigHolder(AlaskaConfig.class).getConfig();
    }
}
