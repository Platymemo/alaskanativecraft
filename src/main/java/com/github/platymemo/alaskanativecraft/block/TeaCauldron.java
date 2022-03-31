package com.github.platymemo.alaskanativecraft.block;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

import java.util.Map;
import java.util.function.Predicate;

public class TeaCauldron extends LeveledCauldronBlock {
    public static final Map<Item, CauldronBehavior> BEHAVIOUR = CauldronBehavior.createMap();

    static {
        BEHAVIOUR.put(Items.GLASS_BOTTLE, ((state, world, pos, player, hand, stack) -> decreaseLevel(state, world, pos, player, hand, stack, new ItemStack(AlaskaItems.LABRADOR_TEA), blockState -> state.get(LEVEL) > 0)));
    }

    public TeaCauldron(Settings settings, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, precipitationPredicate, behaviorMap);
    }

    private static ActionResult decreaseLevel(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, ItemStack output, Predicate<BlockState> predicate) {
        if (!predicate.test(state)) {
            return ActionResult.PASS;
        } else {
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, output));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));

                int level = state.get(LEVEL);
                world.setBlockState(pos, level == 0 ? Blocks.CAULDRON.getDefaultState() : state.with(LEVEL, level - 1), 2);

                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return ActionResult.success(world.isClient);
        }
    }
}
