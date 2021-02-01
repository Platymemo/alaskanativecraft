package com.github.platymemo.alaskanativecraft.recipe;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Arrays;
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

                // Need at least 1 berry but can have more
                if (itemStack.getItem().isIn(AlaskaTags.AKUTAQ_BERRIES)) {
                    hasBerries = true;
                }
                // Can only have one piece of meat
                else if (itemStack.getItem().isIn(AlaskaTags.AKUTAQ_MEATS) && !hasMeat) {
                    hasMeat = true;
                } else {

                    // Checks for bowl, if not a bowl or anything but a bowl, returns false
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

        // Probably a better way to do this but it works fine
        Boolean[] effectAlreadyAdded = {false, false, false, false, false, false, false, false};

        ItemStack currentItemstack;
        for(int i = 0; i < inv.size(); ++i) {
            currentItemstack = inv.getStack(i);
            if (!currentItemstack.isEmpty() && currentItemstack.getItem().isIn(AlaskaTags.AKUTAQ_BERRIES)) {
                int j = random.nextInt(8);

                // Wrapped in an if-else in case it's made in a crafting table with >10 crafting slots
                if (Arrays.asList(effectAlreadyAdded).contains(false)) {

                    // Find an effect that hasn't been added already
                    while (effectAlreadyAdded[j]) {
                        j = random.nextInt(8);
                    }
                } else {
                    break;
                }

                // Add effects
                switch (j) {
                    case 0:
                        addEffectToAkutaq(akutaq, StatusEffects.ABSORPTION, 400);
                        effectAlreadyAdded[j] = true;
                        break;
                    case 1:
                        addEffectToAkutaq(akutaq, StatusEffects.REGENERATION, 400);
                        effectAlreadyAdded[j] = true;
                        break;
                    case 2:
                        addEffectToAkutaq(akutaq, StatusEffects.RESISTANCE, 400);
                        effectAlreadyAdded[j] = true;
                        break;
                    case 3:
                        addEffectToAkutaq(akutaq, StatusEffects.FIRE_RESISTANCE, 400);
                        effectAlreadyAdded[j] = true;
                        break;
                    case 4:
                        addEffectToAkutaq(akutaq, StatusEffects.HASTE, 400);
                        effectAlreadyAdded[j] = true;
                        break;
                    case 5:
                        addEffectToAkutaq(akutaq, StatusEffects.STRENGTH, 400);
                        effectAlreadyAdded[j] = true;
                        break;
                    case 6:
                        addEffectToAkutaq(akutaq, StatusEffects.SPEED, 400);
                        effectAlreadyAdded[j] = true;
                        break;
                    case 7:
                        addEffectToAkutaq(akutaq, StatusEffects.JUMP_BOOST, 400);
                        effectAlreadyAdded[j] = true;
                        break;
                }
            }
        }

        return akutaq;
    }

    // Can't use SuspiciousStewItem.addEffectToStew because it overwrites the list tag
    private static void addEffectToAkutaq(ItemStack stew, StatusEffect effect, int duration) {
        CompoundTag compoundTag = stew.getOrCreateTag();
        ListTag listTag = compoundTag.getList("Effects", 10);
        CompoundTag compoundTag2 = new CompoundTag();
        compoundTag2.putByte("EffectId", (byte)StatusEffect.getRawId(effect));
        compoundTag2.putInt("EffectDuration", duration);
        listTag.add(compoundTag2);
        compoundTag.put("Effects", listTag);
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
