package com.github.platymemo.alaskanativecraft.config;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = AlaskaNativeCraft.MOD_ID)
public class AlaskaConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    private transient static boolean registered = false;
    public boolean snowballConversion = false;
    public boolean mooseEatBark = true;
    public boolean genDriftwood = true;
    public boolean snowyGen = false;
    @ConfigEntry.Gui.CollapsibleObject
    public SealFishing sealFishing = new SealFishing();
    @ConfigEntry.Gui.CollapsibleObject
    public SpawnOptions spawnOptions = new SpawnOptions();

    public static synchronized AlaskaConfig getConfig() {
        if (!registered) {
            AutoConfig.register(AlaskaConfig.class, GsonConfigSerializer::new);
            registered = true;
        }

        return AutoConfig.getConfigHolder(AlaskaConfig.class).getConfig();
    }

    public static class SpawnOptions {
        @ConfigEntry.Gui.CollapsibleObject
        public SpawnSettings sealOceanSettings = new SpawnSettings(5, 1, 4);
        @ConfigEntry.Gui.CollapsibleObject
        public SpawnSettings sealRiverSettings = new SpawnSettings(5, 1, 2);
        @ConfigEntry.Gui.CollapsibleObject
        public SpawnSettings mooseSettings = new SpawnSettings(2, 1, 3);
        @ConfigEntry.Gui.CollapsibleObject
        public SpawnSettings ptarmiganSettings = new SpawnSettings(5, 2, 5);
    }

    public static class SealFishing {
        public boolean sealsHuntFish = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean sealsEatHuntedFish = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean sealsBreedFromHuntedFish = false;
    }

    public static class SpawnSettings {
        public int weight;
        public int minGroupSize;
        public int maxGroupSize;

        public SpawnSettings(int weight, int minGroupSize, int maxGroupSize) {
            this.weight = weight;
            this.minGroupSize = minGroupSize;
            this.maxGroupSize = maxGroupSize;
        }
    }
}
