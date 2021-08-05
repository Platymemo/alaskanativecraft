package com.github.platymemo.alaskanativecraft.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GrindstoneScreenHandler.class)
public class GrindstoneScreenHandlerMixin {
    @Shadow
    @Final
    Inventory input;

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getMaxDamage()I"))
    private int redirectGetMaxDamage(Item item) {
        return this.input.getStack(0).getMaxDamage();
    }
}
