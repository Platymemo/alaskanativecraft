package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    private static final Identifier SNOWGOOGLES_BLUR = new Identifier(AlaskaNativeCraft.MOD_ID, "textures/misc/gogglesblur.png");
    
    @Shadow
    @Final
    private MinecraftClient client;
    
    @Shadow protected abstract void renderOverlay(Identifier texture, float opacity);
    
    @Inject(at = @At(value = "JUMP", opcode = Opcodes.IFEQ, ordinal = 1), method = "render")
    private void renderSnowGogglesOverlay(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (this.client.options.getPerspective().isFirstPerson() && this.client.player.getInventory().getArmorStack(3).getItem() == AlaskaItems.SNOW_GOGGLES) {
            this.renderOverlay(SNOWGOOGLES_BLUR, 1.0F);
        }
    }
}
