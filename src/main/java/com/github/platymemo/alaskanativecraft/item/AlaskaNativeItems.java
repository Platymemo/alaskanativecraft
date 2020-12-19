package com.github.platymemo.alaskanativecraft.item;
import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.entity.AlaskaNativeEntities;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlaskaNativeItems {
    private static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

    public static final Item MUKTUK = add("muktuk", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(2).build())));

    public static final HarpoonItem WOODEN_HARPOON = add("wooden_harpoon", new HarpoonItem(ToolMaterials.WOOD, 2.5F, -2.2F, () -> AlaskaNativeEntities.WOODEN_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final HarpoonItem STONE_HARPOON = add("stone_harpoon", new HarpoonItem(ToolMaterials.STONE, 2.5F, -2.3F, () -> AlaskaNativeEntities.STONE_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final HarpoonItem IRON_HARPOON = add("iron_harpoon", new HarpoonItem(ToolMaterials.IRON, 2.5F, -2.5F, () -> AlaskaNativeEntities.IRON_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final HarpoonItem GOLDEN_HARPOON = add("golden_harpoon", new HarpoonItem(ToolMaterials.GOLD, 2.5F, -2.7F, () -> AlaskaNativeEntities.GOLDEN_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final HarpoonItem DIAMOND_HARPOON = add("diamond_harpoon", new HarpoonItem(ToolMaterials.DIAMOND, 2.5F, -2.7F, () -> AlaskaNativeEntities.DIAMOND_HARPOON, new Item.Settings().group(ItemGroup.COMBAT)));
    public static final HarpoonItem NETHERITE_HARPOON = add("netherite_harpoon", new HarpoonItem(ToolMaterials.NETHERITE, 2.5F, -2.8F, () -> AlaskaNativeEntities.NETHERITE_HARPOON, new Item.Settings().group(ItemGroup.COMBAT).fireproof()));

    private static <I extends Item> I add(String name, I item) {
        ITEMS.put(new Identifier(AlaskaNativeCraft.MOD_ID, name), item);
        return item;
    }

    public static void register() {
        for (Identifier id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, id, ITEMS.get(id));
        }
    }

}
