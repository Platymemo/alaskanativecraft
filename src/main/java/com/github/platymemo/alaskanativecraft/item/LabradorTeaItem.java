package com.github.platymemo.alaskanativecraft.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

/**
 * original code from bodacious berries by ix0rai
 */
public class LabradorTeaItem extends Item {
    private final List<StatusEffect> negativeEffects = List.of(
            StatusEffects.POISON,
            StatusEffects.BLINDNESS,
            StatusEffects.NAUSEA,
            StatusEffects.HUNGER,
            StatusEffects.WEAKNESS,
            StatusEffects.SLOWNESS,
            StatusEffects.MINING_FATIGUE,
            StatusEffects.BAD_OMEN,
            StatusEffects.WITHER,
            StatusEffects.UNLUCK
    );

    public LabradorTeaItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        //we handle changing the stack
        ItemStack stack1 = stack.copy();
        super.finishUsing(stack, world, user);
        stack = stack1;

        //remove random negative status effect
        boolean cleared = false;
        for (StatusEffectInstance effect : user.getStatusEffects()) {
            StatusEffect type = effect.getEffectType();

            if (!cleared && negativeEffects.contains(type)) {
                user.removeStatusEffect(type);
                cleared = true;
            }
        }

        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
        }

        //return empty bottle, or throw it away if it does not fit
        //also decrement the stack size if it will not be entirely consumed
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (user instanceof PlayerEntity playerEntity && !playerEntity.getAbilities().creativeMode) {
                ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!playerEntity.getInventory().insertStack(glassBottle)) {
                    playerEntity.dropItem(glassBottle, false);
                }

                if (stack.getCount() == 1) {
                    return glassBottle;
                } else {
                    stack.decrement(1);
                    return stack;
                }
            }

            return stack;
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
