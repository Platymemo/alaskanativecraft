package com.github.platymemo.alaskanativecraft.entity.boss.whale.part;

import com.github.platymemo.alaskanativecraft.entity.boss.whale.WhaleEntity;

import net.minecraft.entity.damage.DamageSource;

public class WhaleHeadEntityPart extends WhaleEntityPart {
	// TODO turn into config options
	private static final float STUN_THRESHOLD = 5f;
	private static final int STUN_DURATION_MULTIPLIER = 10;

	public WhaleHeadEntityPart(WhaleEntity owner, float width, float height) {
		super(owner, width, height);
	}

	/**
	 * Stuns the whale if hit with a hard enough attack.
	 */
	@Override
	public void onDamaged(DamageSource source, float amount) {
		if (amount > STUN_THRESHOLD) {
			this.getOwner().stun(((int) (amount + 0.5)) * STUN_DURATION_MULTIPLIER);
		}
	}
}
