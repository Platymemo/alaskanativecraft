package com.github.platymemo.alaskanativecraft.block.entity;

import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.recipe.AlaskaRecipes;
import com.github.platymemo.alaskanativecraft.recipe.DryingRecipe;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import com.github.platymemo.alaskanativecraft.block.DryingRackBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntity implements Clearable, Tickable, BlockEntityClientSerializable {
    private final DefaultedList<ItemStack> itemsBeingDried;
    private final int[] dryingTimes;
    private final int[] dryingTotalTimes;

    public DryingRackBlockEntity() {
        super(AlaskaBlocks.DRYING_RACK_BLOCK_ENTITY);
        this.itemsBeingDried = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.dryingTimes = new int[4];
        this.dryingTotalTimes = new int[4];
    }

    @Override
    public void tick() {
        boolean bl = this.getCachedState().get(DryingRackBlock.WATERLOGGED)
                || (this.world != null && this.world.isRaining() && this.world.isSkyVisible(this.pos));
        if (!bl) {
            this.updateItemsBeingDryed();
        } else {
            for (int i = 0; i < this.itemsBeingDried.size(); ++i) {
                if (this.dryingTimes[i] > 0) {
                    this.dryingTimes[i] = MathHelper.clamp(this.dryingTimes[i] - 2, 0, this.dryingTotalTimes[i]);
                }
            }
        }
    }

    private void updateItemsBeingDryed() {
        for(int i = 0; i < this.itemsBeingDried.size(); ++i) {
            ItemStack itemStack = this.itemsBeingDried.get(i);
            if (!itemStack.isEmpty()) {
                this.dryingTimes[i]++;
                if (this.dryingTimes[i] >= this.dryingTotalTimes[i]) {

                    // Don't want it to keep counting up unnecessarily high
                    this.dryingTimes[i] = this.dryingTotalTimes[i];

                    Inventory inventory = new SimpleInventory(itemStack);
                    ItemStack itemStack2 = this.world.getRecipeManager()
                                                     .getFirstMatch(AlaskaRecipes.DRYING,
                                                                    inventory,
                                                                    this.world)
                                                     .map((dryingRecipe) -> dryingRecipe.craft(inventory))
                                                     .orElse(itemStack);
                    this.itemsBeingDried.set(i, itemStack2);
                    this.updateListeners();
                }
            }
        }
    }

    public DefaultedList<ItemStack> getItemsBeingDryed() {
        return this.itemsBeingDried;
    }

    public ItemStack getDriedItem() {
        ItemStack stack;
        for(int i = 0; i < this.itemsBeingDried.size(); ++i) {
            if (this.dryingTimes[i] >= this.dryingTotalTimes[i]) {
                stack = this.itemsBeingDried.get(i);
                if (!stack.isEmpty()) {
                    this.itemsBeingDried.set(i, ItemStack.EMPTY);
                    this.updateListeners();
                    return stack;
                }
            }
        }
        for(int i = 0; i < this.itemsBeingDried.size(); ++i) {
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
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.itemsBeingDried.clear();
        Inventories.fromTag(tag, this.itemsBeingDried);
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
    public CompoundTag toTag(CompoundTag tag) {
        this.saveInitialChunkData(tag);
        tag.putIntArray("DryingTimes", this.dryingTimes);
        tag.putIntArray("DryingTotalTimes", this.dryingTotalTimes);
        return tag;
    }

    private CompoundTag saveInitialChunkData(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.itemsBeingDried, true);
        return tag;
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.saveInitialChunkData(new CompoundTag());
    }

    public Optional<DryingRecipe> getRecipeFor(ItemStack item) {
        return this.itemsBeingDried.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.world.getRecipeManager().getFirstMatch(AlaskaRecipes.DRYING, new SimpleInventory(item), this.world);
    }

    public boolean addItem(ItemStack item, int integer) {
        for(int i = 0; i < this.itemsBeingDried.size(); ++i) {
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

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    public void clear() {
        this.itemsBeingDried.clear();
    }

    public void spawnItemsBeingDryed() {
        if (this.world != null) {
            if (!this.world.isClient) {
                ItemScatterer.spawn(this.world, this.getPos(), this.getItemsBeingDryed());
            }

            this.updateListeners();
        }

    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        this.fromTag(null, compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return this.toTag(compoundTag);
    }
}
