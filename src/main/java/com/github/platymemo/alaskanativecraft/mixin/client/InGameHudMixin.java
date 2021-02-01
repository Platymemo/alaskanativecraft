package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
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
public class InGameHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Inject(at = @At(value = "JUMP", opcode = Opcodes.IFEQ, ordinal = 1), method = "render")
    private void renderSnowGogglesOverlay(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (this.client.options.getPerspective().isFirstPerson() && this.client.player.inventory.getArmorStack(3).getItem() == AlaskaItems.SNOW_GOGGLES) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableAlphaTest();
            this.client.getTextureManager().bindTexture(new Identifier(AlaskaNativeCraft.MOD_ID, "textures/misc/gogglesblur.png"));
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(0.0D, this.scaledHeight, -90.0D).texture(0.0F, 1.0F).next();
            bufferBuilder.vertex(this.scaledWidth, this.scaledHeight, -90.0D).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(this.scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).next();
            bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).next();
            tessellator.draw();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
