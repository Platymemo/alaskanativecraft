package com.github.platymemo.alaskanativecraft.config;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import static java.lang.Math.min;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.recipe.*;
import net.devtech.arrp.json.tags.JTag;
import static net.devtech.arrp.api.RuntimeResourcePack.id;
import static net.devtech.arrp.json.loot.JLootTable.*;
import static net.devtech.arrp.json.recipe.JIngredient.ingredient;
import static net.devtech.arrp.json.recipe.JIngredients.ingredients;
import static net.devtech.arrp.json.recipe.JKeys.keys;
import static net.devtech.arrp.json.recipe.JPattern.pattern;

public class AlaskaNativeData {

    public static RuntimeResourcePack DATA_PACK = RuntimeResourcePack.create(AlaskaNativeCraft.MOD_ID + ":generated_datapack");
    private static String[] harpoons = {"wooden_harpoon", "stone_harpoon", "iron_harpoon", "golden_harpoon", "diamond_harpoon", "netherite_harpoon"};
    private static String[] harpoonMaterials = {"wood", "cobblestone", "iron_ingot", "gold_ingot", "diamond", "netherite_ingot"};

    public static void init() {
        initLootTables();
        initRecipes();
        initTags();

        RRPCallback.EVENT.register(datapack -> datapack.add(DATA_PACK));
    }

    public static void initLootTables() {
        DATA_PACK.addLootTable(id(AlaskaNativeCraft.MOD_ID, "blocks/whale_meat_block"), blockDrops("whale_meat_block"));
    }

    public static void initRecipes() {
        for (int i = 0; i < min(harpoons.length, harpoonMaterials.length); ++i) {
            if (harpoons[i].contains("netherite")) {
                DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, harpoons[i]), harpoonSmithingRecipe(AlaskaNativeItems.DIAMOND_HARPOON, Items.NETHERITE_INGOT, AlaskaNativeItems.fromString(harpoons[i])));
            } else {
                DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, harpoons[i]), harpoonCraftingRecipe(harpoonMaterials[i], ((HarpoonItem) AlaskaNativeItems.fromString(harpoons[i]))));
            }
        }
    }

    public static void initTags() {
        JTag harpoonTag = new JTag();
        for (int i = 0; i < harpoons.length; ++i) {
            harpoonTag.add(id(AlaskaNativeCraft.MOD_ID, harpoons[i]));
        }
        DATA_PACK.addTag(id(AlaskaNativeCraft.MOD_ID, "items/harpoons"), harpoonTag);
    }

    /*
    * Only works with AlaskaNativeCraft items to drop
    */
    public static JLootTable blockDrops(String itemToDrop) {
        return loot("minecraft:block").pool(pool().rolls(1).entry(entry().type("minecraft:item").name(alaskaNativeModID(itemToDrop))));
    }

    public static JRecipe harpoonCraftingRecipe(String resource, Item result) {
        JPattern pattern = pattern(" S#",
                                   " /S",
                                   "/  ");
        JKeys keys = keys().key("/", ingredient().item(Items.STICK)).key("S", ingredient().item(Items.STRING));

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

    public static String alaskaNativeModID(String path) {
        return AlaskaNativeCraft.MOD_ID + ":" + path;
    }
}
