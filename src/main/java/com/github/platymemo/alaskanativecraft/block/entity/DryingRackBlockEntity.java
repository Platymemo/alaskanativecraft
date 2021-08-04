package com.github.platymemo.alaskanativecraft.block.entity;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.recipe.AlaskaRecipes;
import com.github.platymemo.alaskanativecraft.recipe.DryingRecipe;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntity implements Clearable, BlockEntityClientSerializable {
    private final DefaultedList<ItemStack> itemsBeingDried;
    private final int[] dryingTimes;
    private final int[] dryingTotalTimes;

    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(AlaskaBlocks.DRYING_RACK_BLOCK_ENTITY, pos, state);
        this.itemsBeingDried = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.dryingTimes = new int[4];
        this.dryingTotalTimes = new int[4];
    }

    public static void possiblyWetTick(@NotNull World world, BlockPos pos, BlockState state, DryingRackBlockEntity dryingRackBlockEntity) {
        if (world.isSkyVisible(pos)) {
            for (int i = 0; i < dryingRackBlockEntity.itemsBeingDried.size(); ++i) {
                if (dryingRackBlockEntity.dryingTimes[i] > 0) {
                    dryingRackBlockEntity.dryingTimes[i] = MathHelper.clamp(dryingRackBlockEntity.dryingTimes[i] - 2, 0, dryingRackBlockEntity.dryingTotalTimes[i]);
                }
            }
        } else {
            updateItemsBeingDried(world, pos, state, dryingRackBlockEntity);
        }
    }

    @SuppressWarnings("unused")
    public static void updateItemsBeingDried(World world, BlockPos pos, BlockState state, @NotNull DryingRackBlockEntity dryingRackBlockEntity) {
        for (int i = 0; i < dryingRackBlockEntity.itemsBeingDried.size(); ++i) {
            ItemStack itemStack = dryingRackBlockEntity.itemsBeingDried.get(i);
            if (!itemStack.isEmpty()) {
                dryingRackBlockEntity.dryingTimes[i]++;
                if (dryingRackBlockEntity.dryingTimes[i] >= dryingRackBlockEntity.dryingTotalTimes[i]) {

                    // Don't want it to keep counting up unnecessarily high
                    dryingRackBlockEntity.dryingTimes[i] = dryingRackBlockEntity.dryingTotalTimes[i];

                    Inventory inventory = new SimpleInventory(itemStack);
                    ItemStack itemStack2 = world.getRecipeManager()
                            .getFirstMatch(AlaskaRecipes.DRYING, inventory, world)
                            .map((dryingRecipe) -> dryingRecipe.craft(inventory))
                            .orElse(itemStack);
                    dryingRackBlockEntity.itemsBeingDried.set(i, itemStack2);
                    dryingRackBlockEntity.updateListeners();
                }
            }
        }
    }

    public DefaultedList<ItemStack> getItemsBeingDried() {
        return this.itemsBeingDried;
    }

    public ItemStack getDriedItem() {
        ItemStack stack;
        for (int i = 0; i < this.itemsBeingDried.size(); ++i) {
            if (this.dryingTimes[i] >= this.dryingTotalTimes[i]) {
                stack = this.itemsBeingDried.get(i);
                if (!stack.isEmpty()) {
                    this.itemsBeingDried.set(i, ItemStack.EMPTY);
                    this.updateListeners();
                    return stack;
                }
            }
        }
        for (int i = 0; i < this.itemsBeingDried.size(); ++i) {
            stack = this.itemsBeingDried.get(i);
            if (!stack.isEmpty()) {
                this.itemsBeingDried.set(i, ItemStack.EMPTY);
                this.updateListeners();
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.itemsBeingDried.clear();
        Inventories.readNbt(tag, this.itemsBeingDried);
        int[] js;
        if (tag.contains("DryingTimes", 11)) {
            js = tag.getIntArray("DryingTimes");
            System.arraycopy(js, 0, this.dryingTimes, 0, Math.min(this.dryingTotalTimes.length, js.length));
        }

        if (tag.contains("DryingTotalTimes", 11)) {
            js = tag.getIntArray("DryingTotalTimes");
            System.arraycopy(js, 0, this.dryingTotalTimes, 0, Math.min(this.dryingTotalTimes.length, js.length));
        }

    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        this.saveInitialChunkData(tag);
        tag.putIntArray("DryingTimes", this.dryingTimes);
        tag.putIntArray("DryingTotalTimes", this.dryingTotalTimes);
        return tag;
    }

    private NbtCompound saveInitialChunkData(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag, this.itemsBeingDried, true);
        return tag;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.saveInitialChunkData(new NbtCompound());
    }

    @SuppressWarnings("ConstantConditions")
    public Optional<DryingRecipe> getRecipeFor(ItemStack item) {
        return this.itemsBeingDried.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.world.getRecipeManager().getFirstMatch(AlaskaRecipes.DRYING, new SimpleInventory(item), this.world);
    }

    public boolean addItem(ItemStack item, int integer) {
        for (int i = 0; i < this.itemsBeingDried.size(); ++i) {
            ItemStack itemStack = this.itemsBeingDried.get(i);
            if (itemStack.isEmpty()) {
                this.dryingTotalTimes[i] = integer;
                this.dryingTimes[i] = 0;
                this.itemsBeingDried.set(i, item.split(1));
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
    }

    public void spawnItemsBeingDried() {
        if (this.world != null) {
            if (!this.world.isClient) {
                ItemScatterer.spawn(this.world, this.getPos(), this.getItemsBeingDried());
            }

            this.updateListeners();
        }

    }

    @Override
    public void fromClientTag(NbtCompound compoundTag) {
        this.readNbt(compoundTag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound compoundTag) {
        return this.writeNbt(compoundTag);
    }
}
