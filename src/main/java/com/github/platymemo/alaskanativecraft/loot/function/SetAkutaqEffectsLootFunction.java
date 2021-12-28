package com.github.platymemo.alaskanativecraft.loot.function;

import com.github.platymemo.alaskanativecraft.item.AlaskaItems;
import com.github.platymemo.alaskanativecraft.recipe.AkutaqRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SetAkutaqEffectsLootFunction extends ConditionalLootFunction {
    final Map<StatusEffect, LootNumberProvider> effects;
    final LootNumberProvider amountNumberProvider;

    SetAkutaqEffectsLootFunction(LootCondition[] conditions, Map<StatusEffect, LootNumberProvider> effects, LootNumberProvider amountNumberProvider) {
        super(conditions);
        this.effects = ImmutableMap.copyOf(effects);
        this.amountNumberProvider = amountNumberProvider;
    }

    @Override
    public LootFunctionType getType() {
        return AlaskaLootFunctionTypes.SET_AKUTAQ_EFFECT;
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return this.effects
            .values()
            .stream()
            .flatMap(numberProvider -> numberProvider.getRequiredParameters().stream())
            .collect(ImmutableSet.toImmutableSet());
    }

    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        if (stack.isOf(AlaskaItems.AKUTAQ) && !this.effects.isEmpty()) {
            for (int i = 0; i < this.amountNumberProvider.nextInt(context); i++) {
                int selectedInt = context.getRandom().nextInt(this.effects.size());
                Entry<StatusEffect, LootNumberProvider> entry = Iterables.get(this.effects.entrySet(), selectedInt);
                StatusEffect statusEffect = entry.getKey();
                int duration = entry.getValue().nextInt(context);
                if (!statusEffect.isInstant()) {
                    duration *= 20;
                }

                AkutaqRecipe.addEffectToAkutaq(stack, statusEffect, duration);
            }
        }
        return stack;
    }

    public static SetAkutaqEffectsLootFunction.Builder builder() {
        return new SetAkutaqEffectsLootFunction.Builder();
    }

    public static class Builder extends ConditionalLootFunction.Builder<SetAkutaqEffectsLootFunction.Builder> {
        private final Map<StatusEffect, LootNumberProvider> map = Maps.newHashMap();
        private LootNumberProvider amountNumberProvider = ConstantLootNumberProvider.create(0.0F);

        @Override
        protected SetAkutaqEffectsLootFunction.Builder getThisBuilder() {
            return this;
        }

        public SetAkutaqEffectsLootFunction.Builder withAmount(LootNumberProvider amountRange) {
            this.amountNumberProvider = amountRange;
            return this;
        }

        public SetAkutaqEffectsLootFunction.Builder withEffect(StatusEffect effect, LootNumberProvider durationRange) {
            this.map.put(effect, durationRange);
            return this;
        }

        @Override
        public LootFunction build() {
            return new SetAkutaqEffectsLootFunction(this.getConditions(), this.map, amountNumberProvider);
        }
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<SetAkutaqEffectsLootFunction> {
        @Override
        public void toJson(JsonObject jsonObject, SetAkutaqEffectsLootFunction lootFunction, JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, lootFunction, jsonSerializationContext);
            if (!lootFunction.effects.isEmpty()) {
                JsonArray jsonArray = new JsonArray();

                for(StatusEffect statusEffect : lootFunction.effects.keySet()) {
                    JsonObject jsonObject2 = new JsonObject();
                    Identifier identifier = Registry.STATUS_EFFECT.getId(statusEffect);
                    if (identifier == null) {
                        throw new IllegalArgumentException("Don't know how to serialize mob effect " + statusEffect);
                    }

                    jsonObject2.add("type", new JsonPrimitive(identifier.toString()));
                    jsonObject2.add("duration", jsonSerializationContext.serialize(lootFunction.effects.get(statusEffect)));
                    jsonArray.add(jsonObject2);
                }

                jsonObject.add("effects", jsonArray);
            }

        }

        @Override
        public SetAkutaqEffectsLootFunction fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            Map<StatusEffect, LootNumberProvider> map = Maps.newHashMap();
            if (jsonObject.has("effects")) {
                for(JsonElement jsonElement : JsonHelper.getArray(jsonObject, "effects")) {
                    String string = JsonHelper.getString(jsonElement.getAsJsonObject(), "type");
                    StatusEffect statusEffect = Registry.STATUS_EFFECT
                        .getOrEmpty(new Identifier(string))
                        .orElseThrow(() -> new JsonSyntaxException("Unknown mob effect '" + string + "'"));
                    LootNumberProvider lootNumberProvider = JsonHelper.deserialize(
                        jsonElement.getAsJsonObject(), "duration", jsonDeserializationContext, LootNumberProvider.class
                    );

                    map.put(statusEffect, lootNumberProvider);
                }
            }

            LootNumberProvider amountNumberProvider = ConstantLootNumberProvider.create(1.0F);
            if (jsonObject.has("amount")) {
                amountNumberProvider = JsonHelper.deserialize(
                    jsonObject, "amount", jsonDeserializationContext, LootNumberProvider.class
                );
            }

            return new SetAkutaqEffectsLootFunction(lootConditions, map, amountNumberProvider);
        }
    }
}
