package com.github.platymemo.alaskanativecraft.item.material;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.ItemTags;

public enum AlaskaNativeArmorMaterials implements ArmorMaterial {
	KUSPUK("kuspuk", 5, new int[] {1, 0, 3, 1}, 15, SoundEvents.BLOCK_WOOL_STEP, 0.0F, 0.0F, Ingredient.ofTag(ItemTags.WOOL)),
	SNOW_GEAR("snow_goggles", 5, new int[] {1, 0, 0, 1}, 25, SoundEvents.BLOCK_WOOD_STEP, 0.0F, 0.0F, Ingredient.ofTag(ItemTags.PLANKS));

	private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] protectionAmounts;
	private final int enchantability;
	private final SoundEvent equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final Ingredient repairIngredient;

	AlaskaNativeArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Ingredient repairIngredient) {
		this.name = name;
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionAmounts = protectionAmounts;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairIngredient = repairIngredient;
	}

	@Override
	public int getDurability(@NotNull EquipmentSlot slot) {
		return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
	}

	@Override
	public int getProtectionAmount(@NotNull EquipmentSlot slot) {
		return this.protectionAmounts[slot.getEntitySlotId()];
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return this.equipSound;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}
