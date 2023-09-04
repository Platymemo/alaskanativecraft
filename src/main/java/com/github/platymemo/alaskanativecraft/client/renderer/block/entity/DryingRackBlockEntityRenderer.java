package com.github.platymemo.alaskanativecraft.client.renderer.block.entity;

import com.github.platymemo.alaskanativecraft.block.DryingRackBlock;
import com.github.platymemo.alaskanativecraft.block.entity.DryingRackBlockEntity;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;

import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {
    public DryingRackBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(@NotNull DryingRackBlockEntity dryingRackBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Direction.Axis axis = dryingRackBlockEntity.getCachedState().get(DryingRackBlock.AXIS);
        DefaultedList<ItemStack> defaultedList = dryingRackBlockEntity.getItemsBeingDried();
        // Returns 0 if axis=X, 1 if axis=Z
        int XOrZ = axis.choose(0, 0, 1);
        int k = (int) dryingRackBlockEntity.getPos().asLong();

        for (int l = 0; l < defaultedList.size(); ++l) {
            ItemStack itemStack = defaultedList.get(l);
            if (itemStack != ItemStack.EMPTY) {
                matrixStack.push();
                matrixStack.translate(0.5D, 0.45D, 0.5D);
                Direction direction2 = Direction.fromHorizontal((l % 2) * 2 + XOrZ);
                float g = -direction2.asRotation();
                matrixStack.multiply(Axis.Y_POSITIVE.rotationDegrees(g));
                matrixStack.multiply(Axis.X_POSITIVE.rotationDegrees(90.0F));
                matrixStack.multiply(Axis.X_POSITIVE.rotationDegrees(90.0F));
                matrixStack.translate(-0.25D, 0.1D, 0.075D * (l < 1 || l > 2 ? 1 : -1));
                matrixStack.scale(0.375F, 0.375F, 0.375F);
                MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.FIXED, i, j, matrixStack, vertexConsumerProvider, dryingRackBlockEntity.getWorld(), k + l);
                matrixStack.pop();
            }
        }
    }
}
