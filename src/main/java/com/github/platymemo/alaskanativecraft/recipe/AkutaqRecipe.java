package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Random;

public class AkutaqRecipe extends SpecialCraftingRecipe {

    public AkutaqRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean hasMeat = false;
        boolean hasBerries = false;
        boolean hasBowl = false;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem().isIn(AlaskaTags.BERRIES)) {
                    hasBerries = true;
                } else if (itemStack.getItem().isIn(AlaskaTags.AKUTAQ_MEATS) && !hasMeat) {
                    hasMeat = true;
                } else {
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
    public ItemStack craft(CraftingInventory inv) {
        ItemStack akutaq = new ItemStack(AlaskaItems.AKUTAQ, 1);
        Random random = new Random();

        ItemStack currentItemstack;
        for(int i = 0; i < inv.size(); ++i) {
            currentItemstack = inv.getStack(i);
            if (!currentItemstack.isEmpty() && currentItemstack.getItem().isIn(AlaskaTags.BERRIES)) {
                switch (random.nextInt(8)) {
                    case 0:
                        SuspiciousStewItem.addEffectToStew(akutaq, StatusEffects.ABSORPTION, 400);
                        break;
                    case 1:
                        SuspiciousStewItem.addEffectToStew(akutaq, StatusEffects.REGENERATION, 400);
                        break;
                    case 2:
                        SuspiciousStewItem.addEffectToStew(akutaq, StatusEffects.RESISTANCE, 400);
                        break;
                    case 3:
                        SuspiciousStewItem.addEffectToStew(akutaq, StatusEffects.FIRE_RESISTANCE, 400);
                        break;
                    case 4:
                        SuspiciousStewItem.addEffectToStew(akutaq, StatusEffects.HASTE, 400);
                        break;
                    case 5:
                        SuspiciousStewItem.addEffectToStew(akutaq, StatusEffects.STRENGTH, 400);
                        break;
                    case 6:
                        SuspiciousStewItem.addEffectToStew(akutaq, StatusEffects.SPEED, 400);
                        break;
                    case 7:
                        SuspiciousStewItem.addEffectToStew(akutaq, StatusEffects.JUMP_BOOST, 400);
                        break;
                }
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
