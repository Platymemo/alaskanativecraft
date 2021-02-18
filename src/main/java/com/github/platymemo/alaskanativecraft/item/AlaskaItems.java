package com.github.platymemo.alaskanativecraft.item;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaBlocks;
import com.github.platymemo.alaskanativecraft.entity.AlaskaEntities;
import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import com.github.platymemo.alaskanativecraft.item.material.AlaskaNativeArmorMaterials;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlaskaItems {
    private static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

    public static final Item MUKTUK;
    public static final Item SEAL;
    public static final Item COOKED_SEAL;
    public static final Item PTARMIGAN;
    public static final Item COOKED_PTARMIGAN;
    public static final Item VENISON;
    public static final Item COOKED_VENISON;

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

    public static final DogsledItem OAK_DOGSLED;
    public static final DogsledItem SPRUCE_DOGSLED;
    public static final DogsledItem BIRCH_DOGSLED;
    public static final DogsledItem JUNGLE_DOGSLED;
    public static final DogsledItem ACACIA_DOGSLED;
    public static final DogsledItem DARK_OAK_DOGSLED;

    public static final SpawnEggItem SEAL_SPAWN_EGG;
    public static final SpawnEggItem PTARMIGAN_SPAWN_EGG;
    public static final SpawnEggItem MOOSE_SPAWN_EGG;

    private static <I extends Item> I add(String name, I item) {
        ITEMS.put(new Identifier(AlaskaNativeCraft.MOD_ID, name), item);
        return item;
    }

    public static void register() {
        for (Identifier id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, id, ITEMS.get(id));
        }

        addFuels();
        addLootTableEntries();
    }

    private static void addFuels() {
        FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;
        fuelRegistry.add(WOODEN_HARPOON, 200);
    }

    private static void addLootTableEntries() {
        FabricItemGroupBuilder.create(new Identifier(AlaskaNativeCraft.MOD_ID, "items")).icon(() -> MUKTUK.asItem().getDefaultStack()).appendItems(stacks -> Registry.ITEM.forEach(item -> {
            if (Registry.ITEM.getId(item).getNamespace().equals(AlaskaNativeCraft.MOD_ID)) {
                item.appendStacks(item.getGroup(), (DefaultedList<ItemStack>) stacks);
            }
        })).build();
    }

    static {
        MUKTUK = add("muktuk", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(1.0F).build())));
        SEAL = add("seal", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(3).saturationModifier(0.3F).meat().build())));
        COOKED_SEAL = add("cooked_seal", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(7).saturationModifier(1.0F).meat().build())));
        PTARMIGAN = add("ptarmigan", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 400, 0), 0.5F).meat().build())));
        COOKED_PTARMIGAN = add("cooked_ptarmigan", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(5).saturationModifier(0.6F).meat().build())));
        VENISON = add("venison", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(3).saturationModifier(0.3F).meat().build())));
        COOKED_VENISON = add("cooked_venison", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(0.8F).meat().build())));

        BLUEBERRIES = add("blueberries", new AliasedBlockItem(AlaskaBlocks.BLUEBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).snack().build())));
        CLOUDBERRIES = add("cloudberries", new AliasedBlockItem(AlaskaBlocks.CLOUDBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).snack().build())));
        RASPBERRIES = add("raspberries", new AliasedBlockItem(AlaskaBlocks.RASPBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).snack().build())));
        SALMONBERRIES = add("salmonberries", new AliasedBlockItem(AlaskaBlocks.SALMONBERRY_BUSH, new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).snack().build())));

        AKUTAQ = add("akutaq", new SuspiciousStewItem(new Item.Settings().group(ItemGroup.FOOD).maxCount(1).food(new FoodComponent.Builder().hunger(4).saturationModifier(0.8F).build())));

        ULU = add("ulu", new UluItem(new Item.Settings().group(ItemGroup.TOOLS).maxDamage(472)));

        WOODEN_HARPOON = add("wooden_harpoon", new HarpoonItem(ToolMaterials.WOOD, 4.0F, -2.2F, () -> AlaskaEntities.WOODEN_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
        STONE_HARPOON = add("stone_harpoon", new HarpoonItem(ToolMaterials.STONE, 4.0F, -2.3F, () -> AlaskaEntities.STONE_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
        IRON_HARPOON = add("iron_harpoon", new HarpoonItem(ToolMaterials.IRON, 4.0F, -2.5F, () -> AlaskaEntities.IRON_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
        GOLDEN_HARPOON = add("golden_harpoon", new HarpoonItem(ToolMaterials.GOLD, 4.0F, -2.7F, () -> AlaskaEntities.GOLDEN_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
        DIAMOND_HARPOON = add("diamond_harpoon", new HarpoonItem(ToolMaterials.DIAMOND, 4.0F, -2.7F, () -> AlaskaEntities.DIAMOND_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
        NETHERITE_HARPOON = add("netherite_harpoon", new HarpoonItem(ToolMaterials.NETHERITE, 4.0F, -2.8F, () -> AlaskaEntities.NETHERITE_HARPOON, new Item.Settings().group(ItemGroup.COMBAT).fireproof()));

        KUSPUK_HOOD = add("kuspuk_hood", new DyeableArmorItem(AlaskaNativeArmorMaterials.KUSPUK, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT)));
        KUSPUK_BODY = add("kuspuk_body", new DyeableArmorItem(AlaskaNativeArmorMaterials.KUSPUK, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT)));

        SNOW_GOGGLES = add("snow_goggles", new ArmorItem(AlaskaNativeArmorMaterials.SNOW_GOGGLES, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.COMBAT)));

        OAK_DOGSLED = add("oak_dogsled", new DogsledItem(DogsledEntity.Type.OAK, (new Item.Settings()).maxCount(1).group(ItemGroup.TRANSPORTATION)));
        SPRUCE_DOGSLED = add("spruce_dogsled", new DogsledItem(DogsledEntity.Type.SPRUCE, (new Item.Settings()).maxCount(1).group(ItemGroup.TRANSPORTATION)));
        BIRCH_DOGSLED = add("birch_dogsled", new DogsledItem(DogsledEntity.Type.BIRCH, (new Item.Settings()).maxCount(1).group(ItemGroup.TRANSPORTATION)));
        JUNGLE_DOGSLED = add("jungle_dogsled", new DogsledItem(DogsledEntity.Type.JUNGLE, (new Item.Settings()).maxCount(1).group(ItemGroup.TRANSPORTATION)));
        ACACIA_DOGSLED = add("acacia_dogsled", new DogsledItem(DogsledEntity.Type.ACACIA, (new Item.Settings()).maxCount(1).group(ItemGroup.TRANSPORTATION)));
        DARK_OAK_DOGSLED = add("dark_oak_dogsled", new DogsledItem(DogsledEntity.Type.DARK_OAK, (new Item.Settings()).maxCount(1).group(ItemGroup.TRANSPORTATION)));

        SEAL_SPAWN_EGG = add("seal_spawn_egg", new SpawnEggItem(AlaskaEntities.HARP_SEAL, 8355711, 3355443, (new Item.Settings()).group(ItemGroup.MISC)));
        PTARMIGAN_SPAWN_EGG = add("ptarmigan_spawn_egg", new SpawnEggItem(AlaskaEntities.PTARMIGAN, 13750737, 12763849, (new Item.Settings()).group(ItemGroup.MISC)));
        MOOSE_SPAWN_EGG = add("moose_spawn_egg", new SpawnEggItem(AlaskaEntities.MOOSE, 3811094, 14075317, (new Item.Settings()).group(ItemGroup.MISC)));
    }

}
