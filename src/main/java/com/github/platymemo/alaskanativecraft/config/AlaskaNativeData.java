package com.github.platymemo.alaskanativecraft.config;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.block.AlaskaNativeBlocks;
import com.github.platymemo.alaskanativecraft.item.AlaskaNativeItems;
import com.github.platymemo.alaskanativecraft.item.HarpoonItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import static java.lang.Math.min;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.recipe.*;
import net.devtech.arrp.json.tags.JTag;
import static net.devtech.arrp.api.RuntimeResourcePack.id;
import static net.devtech.arrp.json.recipe.JIngredient.ingredient;
import static net.devtech.arrp.json.recipe.JKeys.keys;
import static net.devtech.arrp.json.recipe.JPattern.pattern;
import static net.devtech.arrp.json.recipe.JRecipe.campfire;
import static net.devtech.arrp.json.recipe.JRecipe.smelting;
import static net.devtech.arrp.json.recipe.JRecipe.smithing;
import static net.devtech.arrp.json.recipe.JRecipe.shaped;
import static net.devtech.arrp.json.recipe.JRecipe.shapeless;
import static net.devtech.arrp.json.recipe.JRecipe.smoking;
import static net.devtech.arrp.json.recipe.JResult.result;
import static net.devtech.arrp.json.recipe.JResult.item;
import static net.devtech.arrp.json.tags.JTag.tag;

public class AlaskaNativeData {

    public static RuntimeResourcePack DATA_PACK = RuntimeResourcePack.create(AlaskaNativeCraft.MOD_ID + ":generated_datapack");
    private static final String[] HARPOONS = {"wooden_harpoon", "stone_harpoon", "iron_harpoon", "golden_harpoon", "diamond_harpoon", "netherite_harpoon"};
    private static final String[] HARPOON_MATERIALS = {"wood", "cobblestone", "iron_ingot", "gold_ingot", "diamond", "netherite_ingot"};
    private static final String[] LOGS_WITH_BARK = {"oak_log", "spruce_log", "birch_log", "jungle_log", "acacia_log", "dark_oak_log", "warped_stem", "crimson_stem",
                                              "oak_wood", "spruce_wood", "birch_wood", "jungle_wood", "acacia_wood", "dark_oak_wood", "warped_hyphae", "crimson_hyphae"};
    private static final String[] CHESTS = {"chest", "trapped_chest", "ender_chest"};
    private static final String[] DOGSLED_TYPES = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"};

    public static void init() {
        initTags();
        initRecipes();

        RRPCallback.EVENT.register(datapack -> datapack.add(DATA_PACK));
    }

    public static void initTags() {
        DATA_PACK.addTag(id("c", "blocks/logs_with_bark"), logsWithBarkTag());
        DATA_PACK.addTag(id("c", "items/logs_with_bark"), logsWithBarkTag());
        DATA_PACK.addTag(id("c", "blocks/chests"), chestTag());
        DATA_PACK.addTag(id("c", "items/chests"), chestTag());
        DATA_PACK.addTag(id("c", "items/venison"), tag().add(id(AlaskaNativeCraft.MOD_ID, "venison")));
        DATA_PACK.addTag(id("c", "items/cooked_venison"), tag().add(id(AlaskaNativeCraft.MOD_ID, "cooked_venison")));
        DATA_PACK.addTag(id(AlaskaNativeCraft.MOD_ID, "items/harpoons"), harpoonTag());
    }

    public static void initRecipes() {
        for (int i = 0; i < HARPOONS.length; ++i) {
            if (HARPOONS[i].contains("netherite")) {
                DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, HARPOONS[i]), harpoonSmithingRecipe(AlaskaNativeItems.DIAMOND_HARPOON, Items.NETHERITE_INGOT, HARPOONS[i]));
            } else {
                DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, HARPOONS[i]), harpoonCraftingRecipe(HARPOON_MATERIALS[i], HARPOONS[i]));
            }
        }
        for (String dogsledType : DOGSLED_TYPES) {
            DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, dogsledType + "_dogsled"), dogsledRecipe(dogsledType));
        }
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, "whale_meat_block"), shaped(pattern("###", "###", "###"), keys().key("#", ingredient().item(AlaskaNativeItems.MUKTUK)), item(AlaskaNativeBlocks.WHALE_MEAT_BLOCK.asItem())));
        foodRecipes("cooked_seal", ingredient().item(alaskaNativeModID("seal")));
        foodRecipes("cooked_ptarmigan", ingredient().item(alaskaNativeModID("ptarmigan")));
        foodRecipes("cooked_venison", ingredient().tag("c:venison"));
    }

    public static void foodRecipes(String id, JIngredient ingredient) {
        foodRecipes(id, ingredient, 200);
    }

    public static void foodRecipes(String id, JIngredient ingredient, int cookTime) {
        JResult result = result(alaskaNativeModID(id));
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, id), smelting(ingredient, result).experience(0.35F).cookingTime(cookTime));
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, id + "_from_campfire_cooking"), campfire(ingredient, result).experience(0.35F).cookingTime(cookTime * 3));
        DATA_PACK.addRecipe(id(AlaskaNativeCraft.MOD_ID, id + "_from_smoking"), smoking(ingredient, result).experience(0.35F).cookingTime(cookTime / 2));
    }

    public static JRecipe dogsledRecipe(String resource) {
        JPattern pattern =  pattern("/C ",
                                    "/##",
                                    "///");
        JKeys keys = keys().key("/", ingredient().item(Items.STICK)).key("C", ingredient().tag("c:chests")).key("#", ingredient().item("minecraft:" + resource + "_planks"));

        return shaped(pattern, keys, result(alaskaNativeModID(resource + "_dogsled")));
    }

    public static JRecipe harpoonCraftingRecipe(String resource, String result) {
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

        return shaped(pattern, keys, result(alaskaNativeModID(result)));
    }

    public static JRecipe harpoonSmithingRecipe(Item base, Item addition, String result) {
        return smithing(ingredient().item(base), ingredient().item(addition), result(alaskaNativeModID(result)));
    }

    public static JTag logsWithBarkTag() {
        JTag logsWithBark = new JTag();
        for (String logWithBark : LOGS_WITH_BARK) {
            logsWithBark.add(id("minecraft", logWithBark));
        }
        return logsWithBark;
    }

    public static JTag chestTag() {
        JTag chests = new JTag();
        for (String chest : CHESTS) {
            chests.add(id("minecraft", chest));
        }
        return chests;
    }

    public static JTag harpoonTag() {
        JTag harpoonTag = new JTag();
        for (String harpoon : HARPOONS) {
            harpoonTag.add(id(AlaskaNativeCraft.MOD_ID, harpoon));
        }
        return harpoonTag;
    }

    public static String alaskaNativeModID(String path) {
        return AlaskaNativeCraft.MOD_ID + ":" + path;
    }
}
