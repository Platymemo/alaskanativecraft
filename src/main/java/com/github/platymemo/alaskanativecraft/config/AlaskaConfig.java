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
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("spawning")
    public SpawnOptions spawning = new SpawnOptions();
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("generation")
    public GenerationOptions generation = new GenerationOptions();

    public boolean snowballConversion = false;
    public boolean mooseEatBark = true;
    @ConfigEntry.Gui.CollapsibleObject
    public SealFishing sealFishing = new SealFishing();

    public static synchronized AlaskaConfig getConfig() {
        if (!registered) {
            AutoConfig.register(AlaskaConfig.class, GsonConfigSerializer::new);
            registered = true;
        }

        return AutoConfig.getConfigHolder(AlaskaConfig.class).getConfig();
    }

    public static class SealFishing {
        public boolean sealsHuntFish = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean sealsEatHuntedFish = true;
        @ConfigEntry.Gui.Tooltip(count = 2)
        public boolean sealsBreedFromHuntedFish = false;
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

        public static class SpawnSettings {
            @ConfigEntry.Gui.RequiresRestart
            public int weight;
            @ConfigEntry.Gui.RequiresRestart
            public int minGroupSize;
            @ConfigEntry.Gui.RequiresRestart
            public int maxGroupSize;

            public SpawnSettings(int weight, int minGroupSize, int maxGroupSize) {
                this.weight = weight;
                this.minGroupSize = minGroupSize;
                this.maxGroupSize = maxGroupSize;
            }
        }
    }

    public static class GenerationOptions {
        @ConfigEntry.Gui.RequiresRestart
        public boolean genDriftwood = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean genBlueberry = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean genCloudberry = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean genRaspberry = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean genSalmonberry = true;
        @ConfigEntry.Gui.RequiresRestart
        public boolean genLabradorTea;
        @ConfigEntry.Gui.RequiresRestart
        public boolean snowyGen = false;
    }
}
