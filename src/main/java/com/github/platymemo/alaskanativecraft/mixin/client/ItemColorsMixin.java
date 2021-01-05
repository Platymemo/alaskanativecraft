package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.DyeableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ItemColors.class)
public class ItemColorsMixin {

    @Inject(at = @At(value = "HEAD", shift = At.Shift.BY, by = 5), method = "create", locals = LocalCapture.PRINT)
    private static void addKuspuks(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir, ItemColors itemColors) {
        itemColors.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem)stack.getItem()).getColor(stack), AlaskaNativeItems.KUSPUK_HOOD, AlaskaNativeItems.KUSPUK_BODY, AlaskaNativeItems.KUSPUK_SKIRT, AlaskaNativeItems.KUSPUK_PANTS);
    }
}
