package com.github.platymemo.alaskanativecraft.config;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import static net.devtech.arrp.api.RuntimeResourcePack.id;
import static net.devtech.arrp.json.loot.JLootTable.*;
import static net.devtech.arrp.json.recipe.JIngredient.ingredient;
import static net.devtech.arrp.json.recipe.JKeys.keys;
import static net.devtech.arrp.json.recipe.JPattern.pattern;

public class AlaskaNativeRecipes {

    public static RuntimeResourcePack DATA_PACK = RuntimeResourcePack.create(AlaskaNativeCraft.MOD_ID + ":generated_datapack");

    public static void init() {
        initLootTables();
        initRecipes();

        RRPCallback.EVENT.register(a -> a.add(DATA_PACK));
    }

    public static void initLootTables() {
        DATA_PACK.addLootTable(id(AlaskaNativeCraft.MOD_ID + ":blocks/whale_meat_block"), blockDrops("whale_meat_block"));
    }

    public static void initRecipes() {
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID + ":wooden_harpoon"), harpoonCraftingRecipe("wood", AlaskaNativeItems.WOODEN_HARPOON));
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID + ":stone_harpoon"), harpoonCraftingRecipe("cobblestone", AlaskaNativeItems.STONE_HARPOON));
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID + ":iron_harpoon"), harpoonCraftingRecipe("iron_ingot", AlaskaNativeItems.IRON_HARPOON));
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID + ":golden_harpoon"), harpoonCraftingRecipe("gold_ingot", AlaskaNativeItems.GOLDEN_HARPOON));
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID + ":diamond_harpoon"), harpoonCraftingRecipe("diamond", AlaskaNativeItems.DIAMOND_HARPOON));
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID + ":netherite_harpoon"), harpoonSmithingRecipe(AlaskaNativeItems.DIAMOND_HARPOON, Items.NETHERITE_INGOT, AlaskaNativeItems.NETHERITE_HARPOON));
    }

    public static JLootTable blockDrops(String itemToDrop) {
        return loot("minecraft:block").pool(pool().rolls(1).entry(entry().type("minecraft:item").name(AlaskaNativeCraft.MOD_ID + ":" + itemToDrop)));
    }

    public static JRecipe harpoonCraftingRecipe(String resource, Item result) {
        JPattern pattern = pattern(" S#", " / ", "/  ");
        JKeys keys = keys().key("S", ingredient().item(Items.LEAD)).key("/", ingredient().item(Items.STICK));

        JIngredient material;
        if (resource.equals("wood")) {
            material = ingredient().tag("minecraft:planks");
        } else {
            material = ingredient().item("minecraft:" + resource);
        }
        keys.key("#", material);

        return JRecipe.shaped(pattern, keys, JResult.item(result));
    }

    public static JRecipe harpoonSmithingRecipe(Item base, Item addition, Item result) {
        return JRecipe.smithing(ingredient().item(base), ingredient().item(addition), JResult.item(result));
    }

}
