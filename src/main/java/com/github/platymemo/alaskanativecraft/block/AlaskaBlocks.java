package com.github.platymemo.alaskanativecraft.block;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.entity.DryingRackBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class AlaskaBlocks {
    public static final WhaleMeatBlock WHALE_MEAT_BLOCK;
    public static final BlueberryBushBlock BLUEBERRY_BUSH;
    public static final CloudberryBushBlock CLOUDBERRY_BUSH;
    public static final RaspberryBushBlock RASPBERRY_BUSH;
    public static final SalmonberryBushBlock SALMONBERRY_BUSH;
    public static final FlowerBlock LABRADOR_TEA;
    public static final PillarBlock DRIFTWOOD_LOG;
    public static final DryingRackBlock DRYING_RACK;
    public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK_BLOCK_ENTITY;

    static {
        WHALE_MEAT_BLOCK = register("whale_meat_block", new WhaleMeatBlock(QuiltBlockSettings.of(Material.ORGANIC_PRODUCT).requiresTool().sounds(BlockSoundGroup.HONEY).strength(1.0F, 1.0F)), ItemGroup.BREWING);
        BLUEBERRY_BUSH = register("blueberry_bush", new BlueberryBushBlock(QuiltBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
        CLOUDBERRY_BUSH = register("cloudberry_bush", new CloudberryBushBlock(QuiltBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
        RASPBERRY_BUSH = register("raspberry_bush", new RaspberryBushBlock(QuiltBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
        SALMONBERRY_BUSH = register("salmonberry_bush", new SalmonberryBushBlock(QuiltBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
        LABRADOR_TEA = register("labrador_tea", new LabradorTeaBlock(StatusEffects.REGENERATION, 12, AbstractBlock.Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
        DRIFTWOOD_LOG = register("driftwood_log", new PillarBlock(QuiltBlockSettings.copyOf(Blocks.OAK_LOG)), ItemGroup.BUILDING_BLOCKS);
        DRYING_RACK = register("drying_rack", new DryingRackBlock(QuiltBlockSettings.copyOf(Blocks.OAK_FENCE)), ItemGroup.DECORATIONS);
        DRYING_RACK_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "drying_rack"), QuiltBlockEntityTypeBuilder.create(DryingRackBlockEntity::new, DRYING_RACK).build(null));
    }

    /**
     * registers the {@link Block} along with a {@link BlockItem} in the provided {@link ItemGroup}
     */
    private static <B extends Block> B register(String name, B block, ItemGroup tab) {
        Item.Settings settings = new Item.Settings();
        if (tab != null) {
            settings.group(tab);
        }
        return register(name, block, new BlockItem(block, settings));
    }

    /**
     * registers the {@link Block} along with the provided {@link BlockItem}
     */
    private static <B extends Block> B register(String name, B block, BlockItem item) {
        register(name, block);
        if (item != null) {
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            Registry.register(Registry.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, name), item);
        }
        return block;
    }

    /**
     * registers the {@link Block}
     */
    private static <B extends Block> B register(String name, B block) {
        Registry.register(Registry.BLOCK, new Identifier(AlaskaNativeCraft.MOD_ID, name), block);
        return block;
    }

    public static void register() {}
}
