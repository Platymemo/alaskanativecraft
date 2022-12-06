package com.github.platymemo.alaskanativecraft.entity.boss.whale.part;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;

import net.minecraft.entity.damage.DamageSource;

import org.quiltmc.qsl.entity.multipart.api.AbstractEntityPart;

public class WhaleEntityPart extends AbstractEntityPart<WhaleEntity> {
	public WhaleEntityPart(WhaleEntity owner, float width, float height) {
		super(owner, width, height);
	}

	public void onDamaged(DamageSource source, float amount) {}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return this.getOwner().damagePart(this, source, amount);
	}
}
