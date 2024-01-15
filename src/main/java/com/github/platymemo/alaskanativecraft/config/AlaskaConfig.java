package com.github.platymemo.alaskanativecraft.config;

import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.FloatRange;
import org.quiltmc.config.api.values.TrackedValue;

public class AlaskaConfig extends ReflectiveConfig {
	@Comment("Spawning Options")
	public final SpawnOptions spawning = new SpawnOptions();
	@Comment("Generation Options")
	public final GenerationOptions generation = new GenerationOptions();

	@Comment("Do Snowballs Convert Birds")
	public final TrackedValue<Boolean> snowballConversion = this.value(false);
	@Comment("Do Moose Strip Logs")
	public final TrackedValue<Boolean> mooseEatBark = this.value(true);
	@Comment("Seal Fish Hunting Mechanics")
	public final SealFishing sealFishing = new SealFishing();
	@Comment("Enables Snow Overhaul")
	public final TrackedValue<Boolean> snowOverhaul = this.value(true);
	@Comment("How Much Snow Slows Entities")
	@FloatRange(min = 0.0f, max = 1.0f)
	public final TrackedValue<Float> snowSlow = this.value(0.1f);

	public static class SealFishing extends Section {
		@Comment("Do Seals Hunt Fish")
		public final TrackedValue<Boolean> sealsHuntFish = this.value(true);
		@Comment("Do Seals Eat Hunted Fish")
		@Comment("WARNING:")
		@Comment("Disabling this may cause lag from fish drops.")
		public final TrackedValue<Boolean> sealsEatHuntedFish = this.value(true);
		@Comment("Do Seals Breed From Eating Hunted Fish")
		@Comment("WARNING:")
		@Comment("Enabling this may cause seal populations to drastically rise.")
		public final TrackedValue<Boolean> sealsBreedFromHuntedFish = this.value(false);
		@Comment("The chance a seal breeds whenever it hunts fish")
		@FloatRange(min = 0.0f, max = 1.0f)
		public final TrackedValue<Float> sealsBreedChance = this.value(0.1f);
	}

	public static class SpawnOptions extends Section {
		@Comment("Seal Spawning Options")
		public final SpawnSettings sealSettings = new SpawnSettings(5, 1, 4);
		@Comment("Moose Spawning Options")
		public final SpawnSettings mooseSettings = new SpawnSettings(2, 1, 3);
		@Comment("Ptarmigan Spawning Options")
		public final SpawnSettings ptarmiganSettings = new SpawnSettings(5, 2, 5);

		public static class SpawnSettings extends Section {
			@Comment("How Often the Spawned Mob will be This One")
			public final TrackedValue<Integer> weight;
			@Comment("The Minimum Number of This Mob to Spawn at Once")
			public final TrackedValue<Integer> minGroupSize;
			@Comment("The Maximum Number of This Mob to Spawn at Once")
			public final TrackedValue<Integer> maxGroupSize;

			public SpawnSettings(int weight, int minGroupSize, int maxGroupSize) {
				this.weight = this.value(weight);
				this.minGroupSize = this.value(minGroupSize);
				this.maxGroupSize = this.value(maxGroupSize);
			}
		}
	}

	public static class GenerationOptions extends Section {
		@Comment("Generate Driftwood")
		public final TrackedValue<Boolean> genDriftwood = this.value(true);
		@Comment("Generate Blueberries")
		public final TrackedValue<Boolean> genBlueberry = this.value(true);
		@Comment("Generate Low-Bush Salmonberries")
		public final TrackedValue<Boolean> genCloudberry = this.value(true);
		@Comment("Generate Raspberries")
		public final TrackedValue<Boolean> genRaspberry = this.value(true);
		@Comment("Generate Salmonberries")
		public final TrackedValue<Boolean> genSalmonberry = this.value(true);
		@Comment("Generate Labrador Tea Flowers")
		public final TrackedValue<Boolean> genLabradorTea = this.value(true);
	}
}
