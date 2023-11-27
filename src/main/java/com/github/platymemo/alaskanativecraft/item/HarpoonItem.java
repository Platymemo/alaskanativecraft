package com.github.platymemo.alaskanativecraft.item;

import java.util.function.Supplier;

import com.github.platymemo.alaskanativecraft.entity.HarpoonEntity;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.TridentItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HarpoonItem extends TridentItem {
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	private final ToolMaterial material;
	private final float attackDamage;
	private final Supplier<EntityType<HarpoonEntity>> typeSupplier;
	private EntityType<HarpoonEntity> cachedType = null;

	public HarpoonItem(@NotNull ToolMaterial material, float attackDamage, float attackSpeed, Supplier<EntityType<HarpoonEntity>> typeSupplier, @NotNull Item.Settings settings) {
		super(settings.maxDamageIfAbsent(material.getDurability()));
		this.material = material;
		this.attackDamage = attackDamage + material.getAttackDamage();
		this.typeSupplier = typeSupplier;
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", this.attackDamage - 1, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Tool modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	public EntityType<HarpoonEntity> getType() {
		if (this.cachedType == null) {
			this.cachedType = this.typeSupplier.get();
		}

		return this.cachedType;
	}

	@Override
	public int getEnchantability() {
		return this.material.getEnchantability();
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
	}

	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity playerEntity) {
			int i = this.getMaxUseTime(stack) - remainingUseTicks;
			if (i >= 10) {
				int j = EnchantmentHelper.getRiptide(stack);
				if (j <= 0 || playerEntity.isTouchingWaterOrRain()) {
					if (!world.isClient && j == 0) {
						stack.damage(1, playerEntity, entity -> entity.sendToolBreakStatus(user.getActiveHand()));
						HarpoonEntity harpoonEntity = new HarpoonEntity(world, playerEntity, this, stack);
						harpoonEntity.setProperties(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F, 1.0F);
						if (playerEntity.getAbilities().creativeMode) {
							harpoonEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
						}

						world.spawnEntity(harpoonEntity);
						world.playSoundFromEntity(null, harpoonEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
						if (!playerEntity.getAbilities().creativeMode) {
							playerEntity.getInventory().removeOne(stack);
						}
					}

					playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
					if (j > 0) {
						float yaw = playerEntity.getYaw();
						float pitch = playerEntity.getPitch();
						float h = -MathHelper.sin(yaw * MathHelper.RADIANS_PER_DEGREE) * MathHelper.cos(pitch * MathHelper.RADIANS_PER_DEGREE);
						float k = -MathHelper.sin(pitch * MathHelper.RADIANS_PER_DEGREE);
						float l = MathHelper.cos(yaw * MathHelper.RADIANS_PER_DEGREE) * MathHelper.cos(pitch * MathHelper.RADIANS_PER_DEGREE);
						float length = MathHelper.sqrt(h * h + k * k + l * l);
						float multiplier = 3.0F * ((1.0F + (float) j) / 5.5F);
						h *= multiplier / length;
						k *= multiplier / length;
						l *= multiplier / length;
						playerEntity.addVelocity(h, k, l);
						playerEntity.startRiptideAttack(20);
						if (playerEntity.isOnGround()) {
							playerEntity.move(MovementType.SELF, new Vec3d(0.0D, 1.2D, 0.0D));
						}

						SoundEvent soundEvent;
						if (j >= 3) {
							soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
						} else if (j == 2) {
							soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
						} else {
							soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
						}

						world.playSoundFromEntity(null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
					}
				}
			}
		}
	}
}
