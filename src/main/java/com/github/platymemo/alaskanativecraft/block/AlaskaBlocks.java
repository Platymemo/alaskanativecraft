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

import java.util.LinkedHashMap;
import java.util.Map;

public class AlaskaBlocks {

    private static final Map<Identifier, BlockItem> ITEMS = new LinkedHashMap<>();
    private static final Map<Identifier, Block> BLOCKS = new LinkedHashMap<>();

    public static final WhaleMeatBlock WHALE_MEAT_BLOCK = add("whale_meat_block", new WhaleMeatBlock(FabricBlockSettings.of(Material.ORGANIC_PRODUCT).breakByTool(AlaskaTags.ULUS).sounds(BlockSoundGroup.HONEY).strength(1.0F, 1.0F)), ItemGroup.BREWING);

    public static final BlueberryBushBlock BLUEBERRY_BUSH = add("blueberry_bush", new BlueberryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
    public static final CloudberryBushBlock CLOUDBERRY_BUSH = add("cloudberry_bush", new CloudberryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
    public static final RaspberryBushBlock RASPBERRY_BUSH = add("raspberry_bush", new RaspberryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));
    public static final SalmonberryBushBlock SALMONBERRY_BUSH = add("salmonberry_bush", new SalmonberryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)));

    public static final DriftwoodBlock DRIFTWOOD_LOG = add("driftwood_log", new DriftwoodBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)), ItemGroup.BUILDING_BLOCKS);
    public static final Block DRIFTWOOD_PLANKS = add("driftwood_planks", new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)), ItemGroup.BUILDING_BLOCKS);

    public static final DryingRackBlock DRYING_RACK = add("drying_rack", new DryingRackBlock(FabricBlockSettings.copyOf(Blocks.OAK_FENCE)), ItemGroup.DECORATIONS);
    public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(AlaskaNativeCraft.MOD_ID, "drying_rack"), FabricBlockEntityTypeBuilder.create(DryingRackBlockEntity::new, DRYING_RACK).build(null));

    private static <B extends Block> B add(String name, B block, ItemGroup tab) {
        Item.Settings settings = new Item.Settings();
        if (tab != null) {
            settings.group(tab);
        }
        return add(name, block, new BlockItem(block, settings));
    }

    private static <B extends Block> B add(String name, B block, BlockItem item) {
        add(name, block);
        if (item != null) {
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            ITEMS.put(new Identifier(AlaskaNativeCraft.MOD_ID, name), item);
        }
        return block;
    }

    private static <B extends Block> B add(String name, B block) {
        BLOCKS.put(new Identifier(AlaskaNativeCraft.MOD_ID, name), block);
        return block;
    }

    private static <I extends BlockItem> I add(String name, I item) {
        item.appendBlocks(Item.BLOCK_ITEMS, item);
        ITEMS.put(new Identifier(AlaskaNativeCraft.MOD_ID, name), item);
        return item;
    }

    public static void register() {
        for (Identifier id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, id, ITEMS.get(id));
        }
        for (Identifier id : BLOCKS.keySet()) {
            Registry.register(Registry.BLOCK, id, BLOCKS.get(id));
        }
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
        flammableRegistry.add(DRIFTWOOD_PLANKS, 5, 20);
        flammableRegistry.add(DRYING_RACK, 5, 5);
        flammableRegistry.add(BLUEBERRY_BUSH, 60, 100);
        flammableRegistry.add(CLOUDBERRY_BUSH, 60, 100);
        flammableRegistry.add(RASPBERRY_BUSH, 60, 100);
        flammableRegistry.add(SALMONBERRY_BUSH, 60, 100);
    }

    public static Map<Identifier, Block> getBlocks() {
        return BLOCKS;
    }
}