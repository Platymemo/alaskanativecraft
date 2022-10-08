package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import org.jetbrains.annotations.NotNull;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

public class FishStripRecipe extends SpecialCraftingRecipe {
	public FishStripRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(@NotNull CraftingInventory inv, World world) {
		boolean hasUlu = false;
		boolean hasFish = false;

		for (int i = 0; i < inv.size(); ++i) {
			ItemStack itemStack = inv.getStack(i);
			if (!itemStack.isEmpty()) {
				if (itemStack.isIn(AlaskaTags.SLICEABLE_FISH)) { // Need at least 1 fish but can have more
					hasFish = true;
				} else if (itemStack.isIn(AlaskaTags.ULUS) && !hasUlu) { // Can only have one ulu
					hasUlu = true;
				} else {
					return false;
				}
			}
		}

		return hasUlu && hasFish;
	}

	@Override
	public ItemStack craft(@NotNull CraftingInventory inv) {
		int numOfFish = 0;
		ItemStack stack;
		for (int i = 0; i < inv.size(); ++i) {
			stack = inv.getStack(i);
			if (stack.isIn(AlaskaTags.SLICEABLE_FISH)) {
				numOfFish++;
			}
		}

		return new ItemStack(AlaskaItems.FISH_STRIP, numOfFish * 3);
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(@NotNull CraftingInventory inventory) {
		DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

		for (int i = 0; i < defaultedList.size(); ++i) {
			ItemStack stack = inventory.getStack(i);
			if (stack.isIn(AlaskaTags.ULUS)) {
				if (!stack.damage(5, RandomGenerator.createLegacy(), null)) {
					defaultedList.set(i, stack.copy());
				}
			}
		}

		return defaultedList;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return AlaskaRecipes.FISH_STRIP;
	}
}
