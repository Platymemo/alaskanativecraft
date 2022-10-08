package com.github.platymemo.alaskanativecraft.recipe;

import java.util.Random;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class AkutaqRecipe extends SpecialCraftingRecipe {
	private static final ImmutableList<Pair<StatusEffect, Integer>> POSSIBLE_EFFECTS = ImmutableList.of(
			new Pair<>(StatusEffects.ABSORPTION, 20),
			new Pair<>(StatusEffects.REGENERATION, 10),
			new Pair<>(StatusEffects.RESISTANCE, 10),
			new Pair<>(StatusEffects.FIRE_RESISTANCE, 10),
			new Pair<>(StatusEffects.HASTE, 10),
			new Pair<>(StatusEffects.STRENGTH, 5),
			new Pair<>(StatusEffects.SPEED, 12),
			new Pair<>(StatusEffects.JUMP_BOOST, 10),
			new Pair<>(StatusEffects.SATURATION, 10)
	);

	public AkutaqRecipe(Identifier id) {
		super(id);
	}

	// Can't use SuspiciousStewItem.addEffectToStew because it overwrites the list tag each time
	public static void addEffectToAkutaq(@NotNull ItemStack stew, StatusEffect effect, int duration) {
		NbtCompound compoundTag = stew.getOrCreateNbt();
		NbtList listTag = compoundTag.getList("Effects", 10);

		boolean effectExists = false;

		byte effectId = (byte) StatusEffect.getRawId(effect);
		int actualDuration = duration;
		for (int i = 0; i < listTag.size(); ++i) {
			NbtCompound previousEffect = listTag.getCompound(i);
			if (previousEffect.contains("EffectDuration", 3) && effectId == previousEffect.getByte("EffectId")) {
				actualDuration += previousEffect.getInt("EffectDuration");
				previousEffect.putInt("EffectDuration", actualDuration);
				effectExists = true;
			}
		}

		if (!effectExists) {
			NbtCompound newEffect = new NbtCompound();
			newEffect.putByte("EffectId", effectId);
			newEffect.putInt("EffectDuration", actualDuration);
			listTag.add(newEffect);
		}

		compoundTag.put("Effects", listTag);
	}

	@Override
	public boolean matches(@NotNull CraftingInventory inv, World world) {
		boolean hasMeat = false;
		boolean hasBerries = false;
		boolean hasBowl = false;

		for (int i = 0; i < inv.size(); ++i) {
			ItemStack itemStack = inv.getStack(i);
			if (!itemStack.isEmpty()) {
				if (itemStack.isIn(AlaskaTags.AKUTAQ_BERRIES)) { // Need at least 1 berry but can have more
					hasBerries = true;
				} else if (itemStack.isIn(AlaskaTags.AKUTAQ_MEATS)) {
					// Can only have one piece of meat
					if (hasMeat) {
						return false;
					}

					hasMeat = true;
				} else {
					// Checks for bowl, if not a bowl, or we've already found a bowl, returns false
					if (itemStack.getItem() != Items.BOWL || hasBowl) {
						return false;
					}

					hasBowl = true;
				}
			}
		}

		return hasMeat && hasBerries && hasBowl;
	}

	@Override
	public ItemStack craft(@NotNull CraftingInventory inv) {
		ItemStack akutaq = new ItemStack(AlaskaItems.AKUTAQ, 1);
		Random random = new Random();

		ItemStack currentItemstack;
		for (int i = 0; i < inv.size(); ++i) {
			currentItemstack = inv.getStack(i);
			if (!currentItemstack.isEmpty() && currentItemstack.isIn(AlaskaTags.AKUTAQ_BERRIES)) {
				Pair<StatusEffect, Integer> pair = POSSIBLE_EFFECTS.get(random.nextInt(POSSIBLE_EFFECTS.size()));
				StatusEffect statusEffect = pair.getLeft();
				int duration = pair.getRight();
				if (!statusEffect.isInstant()) {
					duration *= 20;
				}

				// Add effect
				addEffectToAkutaq(akutaq, statusEffect, duration);
			}
		}

		return akutaq;
	}

	@Override
	public boolean fits(int width, int height) {
		return width >= 2 && height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AlaskaRecipes.AKUTAQ;
	}
}
