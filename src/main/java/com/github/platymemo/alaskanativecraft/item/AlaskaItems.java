package com.github.platymemo.alaskanativecraft.item;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import com.github.platymemo.alaskanativecraft.item.material.AlaskaNativeArmorMaterials;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class AlaskaItems {
    public static final Item MUKTUK;
    public static final Item SEAL;
    public static final Item COOKED_SEAL;
    public static final Item PTARMIGAN;
    public static final Item COOKED_PTARMIGAN;
    public static final Item VENISON;
    public static final Item COOKED_VENISON;
    public static final Item DRIFTWOOD_CHUNK;
    public static final Item ANTLER;
    public static final Item IVORY;
    public static final Item FISH_STRIP;
    public static final Item DRY_FISH;
    public static final Item BLUEBERRIES;
    public static final Item CLOUDBERRIES;
    public static final Item RASPBERRIES;
    public static final Item SALMONBERRIES;
    public static final SuspiciousStewItem AKUTAQ;
    public static final UluItem ULU;
    public static final HarpoonItem WOODEN_HARPOON;
    public static final HarpoonItem STONE_HARPOON;
    public static final HarpoonItem IRON_HARPOON;
    public static final HarpoonItem GOLDEN_HARPOON;
    public static final HarpoonItem DIAMOND_HARPOON;
    public static final HarpoonItem NETHERITE_HARPOON;
    public static final DyeableArmorItem KUSPUK_HOOD;
    public static final DyeableArmorItem KUSPUK_BODY;
    public static final ArmorItem SNOW_GOGGLES;
    public static final ArmorItem SNOWSHOES;
    public static final DogsledItem OAK_DOGSLED;
    public static final DogsledItem SPRUCE_DOGSLED;
    public static final DogsledItem BIRCH_DOGSLED;
    public static final DogsledItem JUNGLE_DOGSLED;
    public static final DogsledItem ACACIA_DOGSLED;
    public static final DogsledItem DARK_OAK_DOGSLED;
    public static final SpawnEggItem SEAL_SPAWN_EGG;
    public static final SpawnEggItem PTARMIGAN_SPAWN_EGG;
    public static final SpawnEggItem MOOSE_SPAWN_EGG;

    static {
        MUKTUK = register("muktuk", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(1.0F).build())));
        SEAL = register("seal", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(3).saturationModifier(0.3F).meat().build())));
        COOKED_SEAL = register("cooked_seal", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(1.0F).meat().build())));
        PTARMIGAN = register("ptarmigan", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 400, 0), 0.5F).meat().build())));
        COOKED_PTARMIGAN = register("cooked_ptarmigan", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(5).saturationModifier(0.6F).meat().build())));
        VENISON = register("venison", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(3).saturationModifier(0.3F).meat().build())));
        COOKED_VENISON = register("cooked_venison", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(0.8F).meat().build())));

        DRIFTWOOD_CHUNK = register("driftwood_chunk", new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        ANTLER = register("antler", new Item(new FabricItemSettings().group(ItemGroup.MISC)));
        IVORY = register("ivory", new Item(new FabricItemSettings().group(ItemGroup.MISC)));

        FISH_STRIP = register("fish_strip", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.1F).snack().build())));
        DRY_FISH = register("dry_fish", new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).saturationModifier(1F).snack().build())));

        BLUEBERRIES = register("blueberries", new AliasedBlockItem(AlaskaBlocks.BLUEBERRY_BUSH, new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).snack().build())));
        CLOUDBERRIES = register("cloudberries", new AliasedBlockItem(AlaskaBlocks.CLOUDBERRY_BUSH, new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).snack().build())));
        RASPBERRIES = register("raspberries", new AliasedBlockItem(AlaskaBlocks.RASPBERRY_BUSH, new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).snack().build())));
        SALMONBERRIES = register("salmonberries", new AliasedBlockItem(AlaskaBlocks.SALMONBERRY_BUSH, new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).snack().build())));

        AKUTAQ = register("akutaq", new SuspiciousStewItem(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(1).food(new FoodComponent.Builder().hunger(4).saturationModifier(0.8F).build())));

        ULU = register("ulu", new UluItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(472)));

        WOODEN_HARPOON = register("wooden_harpoon", new HarpoonItem(ToolMaterials.WOOD, 4.0F, -2.2F, () -> AlaskaEntities.WOODEN_HARPOON, new FabricItemSettings().group(ItemGroup.COMBAT)));
        STONE_HARPOON = register("stone_harpoon", new HarpoonItem(ToolMaterials.STONE, 4.0F, -2.3F, () -> AlaskaEntities.STONE_HARPOON, new FabricItemSettings().group(ItemGroup.COMBAT)));
        IRON_HARPOON = register("iron_harpoon", new HarpoonItem(ToolMaterials.IRON, 4.0F, -2.5F, () -> AlaskaEntities.IRON_HARPOON, new FabricItemSettings().group(ItemGroup.COMBAT)));
        GOLDEN_HARPOON = register("golden_harpoon", new HarpoonItem(ToolMaterials.GOLD, 4.0F, -2.7F, () -> AlaskaEntities.GOLDEN_HARPOON, new FabricItemSettings().group(ItemGroup.COMBAT)));
        DIAMOND_HARPOON = register("diamond_harpoon", new HarpoonItem(ToolMaterials.DIAMOND, 4.0F, -2.7F, () -> AlaskaEntities.DIAMOND_HARPOON, new FabricItemSettings().group(ItemGroup.COMBAT)));
        NETHERITE_HARPOON = register("netherite_harpoon", new HarpoonItem(ToolMaterials.NETHERITE, 4.0F, -2.8F, () -> AlaskaEntities.NETHERITE_HARPOON, new FabricItemSettings().group(ItemGroup.COMBAT).fireproof()));

        KUSPUK_HOOD = register("kuspuk_hood", new DyeableArmorItem(AlaskaNativeArmorMaterials.KUSPUK, EquipmentSlot.HEAD, new FabricItemSettings().group(ItemGroup.COMBAT)));
        KUSPUK_BODY = register("kuspuk_body", new DyeableArmorItem(AlaskaNativeArmorMaterials.KUSPUK, EquipmentSlot.CHEST, new FabricItemSettings().group(ItemGroup.COMBAT)));

        SNOW_GOGGLES = register("snow_goggles", new ArmorItem(AlaskaNativeArmorMaterials.SNOW_GEAR, EquipmentSlot.HEAD, new FabricItemSettings().group(ItemGroup.COMBAT)));
        SNOWSHOES = register("snowshoes", new ArmorItem(AlaskaNativeArmorMaterials.SNOW_GEAR, EquipmentSlot.FEET, new FabricItemSettings().group(ItemGroup.COMBAT)));

        OAK_DOGSLED = register("oak_dogsled", new DogsledItem(DogsledEntity.Type.OAK, new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION)));
        SPRUCE_DOGSLED = register("spruce_dogsled", new DogsledItem(DogsledEntity.Type.SPRUCE, new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION)));
        BIRCH_DOGSLED = register("birch_dogsled", new DogsledItem(DogsledEntity.Type.BIRCH, new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION)));
        JUNGLE_DOGSLED = register("jungle_dogsled", new DogsledItem(DogsledEntity.Type.JUNGLE, new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION)));
        ACACIA_DOGSLED = register("acacia_dogsled", new DogsledItem(DogsledEntity.Type.ACACIA, new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION)));
        DARK_OAK_DOGSLED = register("dark_oak_dogsled", new DogsledItem(DogsledEntity.Type.DARK_OAK, new FabricItemSettings().maxCount(1).group(ItemGroup.TRANSPORTATION)));

        SEAL_SPAWN_EGG = register("seal_spawn_egg", new SpawnEggItem(AlaskaEntities.HARP_SEAL, 8355711, 3355443, new FabricItemSettings().group(ItemGroup.MISC)));
        PTARMIGAN_SPAWN_EGG = register("ptarmigan_spawn_egg", new SpawnEggItem(AlaskaEntities.PTARMIGAN, 13750737, 12763849, new FabricItemSettings().group(ItemGroup.MISC)));
        MOOSE_SPAWN_EGG = register("moose_spawn_egg", new SpawnEggItem(AlaskaEntities.MOOSE, 3811094, 14075317, new FabricItemSettings().group(ItemGroup.MISC)));
    }

    private static <I extends Item> I register(String name, I item) {
        Registry.register(Registry.ITEM, new Identifier(AlaskaNativeCraft.MOD_ID, name), item);
        return item;
    }

    public static void register() {
        addFuels();
        addItemGroupEntries();
        addSnowGogglesToLootTable();
    }

    private static void addFuels() {
        FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;
        fuelRegistry.add(WOODEN_HARPOON, 200);
    }

    private static void addItemGroupEntries() {
        FabricItemGroupBuilder.create(new Identifier(AlaskaNativeCraft.MOD_ID, "items")).icon(() -> MUKTUK.asItem().getDefaultStack()).appendItems(stacks -> Registry.ITEM.forEach(item -> {
            if (Registry.ITEM.getId(item).getNamespace().equals(AlaskaNativeCraft.MOD_ID)) {
                item.appendStacks(item.getGroup(), (DefaultedList<ItemStack>) stacks);
            }
        })).build();
    }

    private static void addSnowGogglesToLootTable() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (LootTables.VILLAGE_SNOWY_HOUSE_CHEST.equals(id) || LootTables.VILLAGE_TAIGA_HOUSE_CHEST.equals(id)) {
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
                        .withEntry(ItemEntry.builder(AlaskaItems.SNOW_GOGGLES).build());

                supplier.pool(poolBuilder);
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerItemColors() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack), AlaskaItems.KUSPUK_HOOD, AlaskaItems.KUSPUK_BODY);
    }
}
