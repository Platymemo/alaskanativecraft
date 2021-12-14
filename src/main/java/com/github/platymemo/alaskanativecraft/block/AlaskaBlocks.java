package com.github.platymemo.alaskanativecraft.block;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.entity.DryingRackBlockEntity;
import com.github.platymemo.alaskanativecraft.tags.AlaskaTags;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlaskaBlocks {
    public static final WhaleMeatBlock WHALE_MEAT_BLOCK;
    public static final BlueberryBushBlock BLUEBERRY_BUSH;
    public static final CloudberryBushBlock CLOUDBERRY_BUSH;
    public static final RaspberryBushBlock RASPBERRY_BUSH;
    public static final SalmonberryBushBlock SALMONBERRY_BUSH;
    public static final PillarBlock DRIFTWOOD_LOG;
    public static final DryingRackBlock DRYING_RACK;
    public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK_BLOCK_ENTITY;

    static {
        WHALE_MEAT_BLOCK = register("whale_meat_block", new WhaleMeatBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT).sounds(BlockSoundGroup.HONEY).strength(1.0F, 1.0F)), ItemGroup.BREWING);
        BLUEBERRY_BUSH = register("blueberry_bush", new BlueberryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
        CLOUDBERRY_BUSH = register("cloudberry_bush", new CloudberryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
        RASPBERRY_BUSH = register("raspberry_bush", new RaspberryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
        SALMONBERRY_BUSH = register("salmonberry_bush", new SalmonberryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
        DRIFTWOOD_LOG = register("driftwood_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)), ItemGroup.BUILDING_BLOCKS);
        DRYING_RACK = register("drying_rack", new DryingRackBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE)), ItemGroup.DECORATIONS);
        DRYING_RACK_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "drying_rack"), FabricBlockEntityTypeBuilder.create(DryingRackBlockEntity::new, DRYING_RACK).build(null));
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

    public static void register() {
        addFuels();
        addFlammables();
    }

    private static void addFuels() {
        FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;
        fuelRegistry.add(WHALE_MEAT_BLOCK, 800);
        fuelRegistry.add(DRYING_RACK, 300);
    }

    private static void addFlammables() {
        FlammableBlockRegistry flammableRegistry = FlammableBlockRegistry.getDefaultInstance();
        flammableRegistry.add(WHALE_MEAT_BLOCK, 60, 100);
        flammableRegistry.add(DRIFTWOOD_LOG, 5, 5);
        flammableRegistry.add(DRYING_RACK, 5, 5);
        flammableRegistry.add(BLUEBERRY_BUSH, 60, 100);
        flammableRegistry.add(CLOUDBERRY_BUSH, 60, 100);
        flammableRegistry.add(RASPBERRY_BUSH, 60, 100);
        flammableRegistry.add(SALMONBERRY_BUSH, 60, 100);
    }

}