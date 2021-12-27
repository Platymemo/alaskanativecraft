package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    @Shadow
    @Final
    private final Property levelCost;

    @Shadow
    private int repairItemUsage;

    protected AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
        throw new AssertionError("AlaskaNativeCraft's AnvilScreenHandlerMixin constructor called!");
    }

    @Inject(at = @At(value = "JUMP", opcode = Opcodes.IFNE, ordinal = 0), method = "updateResult", cancellable = true)
    private void addLeashedHarpoon(CallbackInfo ci) {
        ItemStack base = this.input.getStack(0);
        ItemStack addition = this.input.getStack(1);
        if (base.isIn(AlaskaTags.HARPOONS) && !base.getOrCreateNbt().getBoolean("leashed") && addition.isOf(Items.LEAD)) {
            ItemStack result = base.copy();
            result.getOrCreateNbt().putInt("leashed", addition.getCount());
            this.output.setStack(0, result);
            this.levelCost.set(1);
            this.repairItemUsage = 1;
            sendContentUpdates();
            ci.cancel();
        }
    }
}
