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
	public void render(@NotNull DryingRackBlockEntity dryingRack, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i, int j) {
		Direction.Axis axis = dryingRack.getCachedState().get(DryingRackBlock.AXIS);
		DefaultedList<ItemStack> itemsBeingDried = dryingRack.getItemsBeingDried();
		// Returns 0 if axis=X, 1 if axis=Z
		int axisOrdinal = axis.choose(0, 0, 1);
		int baseSeed = (int) dryingRack.getPos().asLong();

		// Render drying items
		for (int slot = 0; slot < itemsBeingDried.size(); slot++) {
			ItemStack itemStack = itemsBeingDried.get(slot);
			if (itemStack != ItemStack.EMPTY) {
				matrices.push();
				matrices.translate(0.5D, 0.45D, 0.5D);
				Direction itemDirection = Direction.fromHorizontal((slot % 2) * 2 + axisOrdinal);
				float rotation = -itemDirection.asRotation();
				matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(rotation));
				matrices.multiply(Axis.X_POSITIVE.rotationDegrees(180.0F));
				matrices.translate(-0.25D, 0.1D, 0.075D * (slot < 1 || slot > 2 ? 1 : -1));
				matrices.scale(0.375F, 0.375F, 0.375F);
				MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.FIXED, i, j, matrices, vertexConsumers, dryingRack.getWorld(), baseSeed + slot);
				matrices.pop();
			}
		}
	}
}
