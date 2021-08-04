package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
    @Shadow
    @Final
    ItemStack output;

    @Inject(at = @At("RETURN"), method = "craft", cancellable = true)
    private void addDurability(@NotNull CraftingInventory craftingInventory, CallbackInfoReturnable<ItemStack> cir) {
        if (craftingInventory.containsAny(Set.of(AlaskaItems.DRIFTWOOD_CHUNK, AlaskaItems.ANTLER, AlaskaItems.IVORY)) && this.output.getItem().isDamageable()) {
            ItemStack protectedItem = this.output.copy();
            NbtCompound tag = protectedItem.getOrCreateNbt();
            float durabilityMultiplier = 1;
            for (int i = 0; i < craftingInventory.size(); ++i) {
                ItemStack stack = craftingInventory.getStack(i);
                if (stack.isOf(AlaskaItems.DRIFTWOOD_CHUNK)) {
                    durabilityMultiplier *= 1.05;
                } else if (stack.isOf(AlaskaItems.ANTLER)) {
                    durabilityMultiplier *= 1.2;
                } else if (stack.isOf(AlaskaItems.IVORY)) {
                    durabilityMultiplier *= 1.4;
                }
            }
            tag.putFloat("DurabilityMultiplier", durabilityMultiplier);
            cir.setReturnValue(protectedItem);
        }
    }
}
