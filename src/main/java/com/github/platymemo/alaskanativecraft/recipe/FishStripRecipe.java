package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Random;

public class FishStripRecipe extends SpecialCraftingRecipe {

    public FishStripRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean hasUlu = false;
        boolean hasFish = false;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!itemStack.isEmpty()) {

                // Need at least 1 fish but can have more
                if (itemStack.getItem().isIn(AlaskaTags.SLICEABLE_FISH)) {
                    hasFish = true;
                }
                // Can only have one ulu
                else if (itemStack.getItem().isIn(AlaskaTags.ULUS) && !hasUlu) {
                    hasUlu = true;
                } else {
                    return false;
                }
            }
        }

        return hasUlu && hasFish;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        int numOfFish = 0;
        ItemStack stack;
        for(int i = 0; i < inv.size(); ++i) {
            stack = inv.getStack(i);
            if (stack.getItem().isIn(AlaskaTags.SLICEABLE_FISH)) {
                numOfFish++;
            }
        }
        return new ItemStack(AlaskaItems.FISH_STRIP, numOfFish * 3);
    }

    @Override
    public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inventory) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

        for(int i = 0; i < defaultedList.size(); ++i) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem().isIn(AlaskaTags.ULUS)) {
                stack.damage(5, new Random(), null);
                defaultedList.set(i, stack.copy());
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
