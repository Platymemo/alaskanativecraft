package com.github.platymemo.alaskanativecraft.mixin.client;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @ModifyVariable(at = @At(value = "JUMP", opcode = Opcodes.IFNE, ordinal = 0),
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            ordinal = 7)
    private float changeLimbDistance(float limbDistance, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (livingEntity instanceof WolfEntity && livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof DogsledEntity) {
            limbDistance = MathHelper.lerp(g, livingEntity.lastLimbDistance, livingEntity.limbDistance);

            if (limbDistance > 1.0F) {
                limbDistance = 1.0F;
            }
        }
        return limbDistance;
    }

    @ModifyVariable(at = @At(value = "JUMP", opcode = Opcodes.IFNE, ordinal = 0),
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            ordinal = 8)
    private float changeLimbAngle(float limbAngle, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (livingEntity instanceof WolfEntity && livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof DogsledEntity) {
            limbAngle = livingEntity.limbAngle - livingEntity.limbDistance * (1.0F - g);
        }
        return limbAngle;
    }
}
