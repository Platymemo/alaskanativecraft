package com.github.platymemo.alaskanativecraft.block.entity;

import java.util.Arrays;
import java.util.Optional;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.block.DryingRackBlock;
import com.github.platymemo.alaskanativecraft.recipe.AlaskaRecipes;
import com.github.platymemo.alaskanativecraft.recipe.DryingRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DryingRackBlockEntity extends BlockEntity implements Clearable {
	private final DefaultedList<ItemStack> itemsBeingDried;
	// Contains the current time an item is being dried
	private final int[] dryingTimes;
	// Contains the max time each item can be dried
	private final int[] dryingTotalTimes;

	public DryingRackBlockEntity(BlockPos pos, BlockState state) {
		super(AlaskaBlocks.DRYING_RACK_BLOCK_ENTITY, pos, state);
		this.itemsBeingDried = DefaultedList.ofSize(4, ItemStack.EMPTY);
		this.dryingTimes = new int[4];
		this.dryingTotalTimes = new int[4];
	}

	/**
	 * Check for wet conditions, and reset drying times in the provided {@link DryingRackBlockEntity rack} if so.
	 * Otherwise, dry the {@link ItemStack item}s.
	 */
	public static void updateItemsBeingDried(@NotNull World world, BlockPos pos, BlockState state, DryingRackBlockEntity dryingRack) {
		if (state.get(DryingRackBlock.WATERLOGGED) || (world.isRaining() && world.isSkyVisible(pos))) {
			Arrays.fill(dryingRack.dryingTimes, 0);
		} else {
			dry(world, pos, dryingRack);
		}
	}

	/**
	 * Increase the dry time in all items in the provided {@link DryingRackBlockEntity rack}.
	 */
	public static void dry(World world, BlockPos pos, @NotNull DryingRackBlockEntity dryingRack) {
		boolean smoked = CampfireBlock.isLitCampfireInRange(world, pos);

		for (int slot = 0; slot < dryingRack.itemsBeingDried.size(); slot++) {
			ItemStack itemStack = dryingRack.itemsBeingDried.get(slot);
			if (!itemStack.isEmpty()) {
				// Smoking halves time to dry
				dryingRack.dryingTimes[slot] += smoked ? 2 : 1;
				if (dryingRack.dryingTimes[slot] >= dryingRack.dryingTotalTimes[slot]) {
					// Don't want it to keep counting up unnecessarily high
					dryingRack.dryingTimes[slot] = dryingRack.dryingTotalTimes[slot];

					Inventory inventory = new SimpleInventory(itemStack);
					ItemStack result = world.getRecipeManager().getFirstMatch(AlaskaRecipes.DRYING, inventory, world).map((dryingRecipe) -> dryingRecipe.craft(inventory, world.getRegistryManager())).orElse(itemStack);
					dryingRack.itemsBeingDried.set(slot, result);
					dryingRack.updateListeners();
				}
			}
		}
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	public DefaultedList<ItemStack> getItemsBeingDried() {
		return this.itemsBeingDried;
	}

	/**
	 * Removes an {@link ItemStack item} in the {@link DryingRackBlockEntity rack}, prioritizing dried items.
	 * @return The removed {@link ItemStack item}.
	 */
	public ItemStack takeItem() {
		ItemStack itemStack;
		// Prioritize getting fully dried items first.
		for (int slot = 0; slot < this.itemsBeingDried.size(); slot++) {
			if (this.dryingTimes[slot] >= this.dryingTotalTimes[slot]) {
				itemStack = this.itemsBeingDried.get(slot);
				if (!itemStack.isEmpty()) {
					this.itemsBeingDried.set(slot, ItemStack.EMPTY);
					this.updateListeners();
					return itemStack;
				}
			}
		}

		for (int slot = 0; slot < this.itemsBeingDried.size(); slot++) {
			itemStack = this.itemsBeingDried.get(slot);
			if (!itemStack.isEmpty()) {
				this.itemsBeingDried.set(slot, ItemStack.EMPTY);
				this.updateListeners();
				return itemStack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.itemsBeingDried.clear();
		Inventories.readNbt(nbt, this.itemsBeingDried);
		int[] dryingTimes;
		if (nbt.contains("DryingTimes", NbtElement.INT_ARRAY_TYPE)) {
			dryingTimes = nbt.getIntArray("DryingTimes");
			System.arraycopy(dryingTimes, 0, this.dryingTimes, 0, Math.min(this.dryingTotalTimes.length, dryingTimes.length));
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.itemsBeingDried, true);
		nbt.putIntArray("DryingTimes", this.dryingTimes);
		nbt.putIntArray("DryingTotalTimes", this.dryingTotalTimes);
	}

	@Override
	public NbtCompound toSyncedNbt() {
		NbtCompound nbt = new NbtCompound();
		return Inventories.writeNbt(nbt, this.itemsBeingDried, true);
	}

	@SuppressWarnings("ConstantConditions")
	public Optional<DryingRecipe> getRecipeFor(ItemStack itemStack) {
		return this.itemsBeingDried.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.world.getRecipeManager().getFirstMatch(AlaskaRecipes.DRYING, new SimpleInventory(itemStack), this.world);
	}

	/**
	 * Places a single count of the {@link ItemStack item} into the {@link DryingRackBlockEntity drying rack}.
	 * @param itemStack the {@link ItemStack stack} to take from
	 * @param totalDryingTime the max drying time
	 * @return {@code true} if an {@link ItemStack item} was added to the {@link DryingRackBlockEntity rack}, or {@code false}.
	 */
	public boolean addItem(ItemStack itemStack, int totalDryingTime) {
		return this.addItem(itemStack, totalDryingTime, 1);
	}

	/**
	 * Places the provided count of the {@link ItemStack item} into the {@link DryingRackBlockEntity drying rack}.
	 * @param itemStack the {@link ItemStack stack} to take from
	 * @param totalDryingTime the max drying time
	 * @param count how much from the stack to take
	 * @return {@code true} if an {@link ItemStack item} was added to the {@link DryingRackBlockEntity rack}, or {@code false}.
	 */
	public boolean addItem(ItemStack itemStack, int totalDryingTime, int count) {
		for (int slot = 0; slot < this.itemsBeingDried.size(); slot++) {
			ItemStack dryingItemStack = this.itemsBeingDried.get(slot);
			if (dryingItemStack.isEmpty()) {
				this.dryingTotalTimes[slot] = totalDryingTime;
				this.dryingTimes[slot] = 0;
				this.itemsBeingDried.set(slot, itemStack.split(count));
				this.updateListeners();
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("ConstantConditions")
	private void updateListeners() {
		this.markDirty();
		this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
	}

	@Override
	public void clear() {
		this.itemsBeingDried.clear();
		Arrays.fill(this.dryingTimes, 0);
		Arrays.fill(this.dryingTotalTimes, 0);
	}

	public void spawnItemsBeingDried() {
		if (this.world != null) {
			if (!this.world.isClient) {
				ItemScatterer.spawn(this.world, this.getPos(), this.getItemsBeingDried());
			}

			this.updateListeners();
		}
	}
}
