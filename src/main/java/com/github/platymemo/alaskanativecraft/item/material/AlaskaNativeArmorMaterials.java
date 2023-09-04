package com.github.platymemo.alaskanativecraft.item.material;

import java.util.EnumMap;

import com.github.platymemo.alaskanativecraft.mixin.ArmorMaterialsAccessor;
import org.jetbrains.annotations.NotNull;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import org.quiltmc.loader.api.minecraft.ClientOnly;

public enum AlaskaNativeArmorMaterials implements ArmorMaterial {
    KUSPUK("kuspuk", 5, new int[] {1, 0, 3, 1}, 15, SoundEvents.BLOCK_WOOL_STEP, 0.0F, 0.0F, Ingredient.ofTag(ItemTags.WOOL)),
    SNOW_GEAR("snow_goggles", 5, new int[] {1, 0, 0, 1}, 25, SoundEvents.BLOCK_WOOD_STEP, 0.0F, 0.0F, Ingredient.ofTag(ItemTags.PLANKS));

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.ArmorSlot, Integer> slotProtections;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredient;

    AlaskaNativeArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Ingredient repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = Util.make(new EnumMap<>(ArmorItem.ArmorSlot.class), map -> {
            map.put(ArmorItem.ArmorSlot.BOOTS, protectionAmounts[0]);
            map.put(ArmorItem.ArmorSlot.LEGGINGS, protectionAmounts[1]);
            map.put(ArmorItem.ArmorSlot.CHESTPLATE, protectionAmounts[2]);
            map.put(ArmorItem.ArmorSlot.HELMET, protectionAmounts[3]);
        });
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability(@NotNull ArmorItem.ArmorSlot slot) {
        return ArmorMaterialsAccessor.getBaseDurabilityValues().get(slot) * this.durabilityMultiplier;
    }

    @Override
    public int getProtection(@NotNull ArmorItem.ArmorSlot slot) {
        return this.slotProtections.get(slot);
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
    @ClientOnly
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
