package com.github.platymemo.alaskanativecraft.item;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class DogsledItem extends Item {
    private static final Predicate<Entity> RIDERS;
    private final DogsledEntity.Type type;

    public DogsledItem(DogsledEntity.Type type, Item.Settings settings) {
        super(settings);
        this.type = type;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        } else {
            Vec3d vec3d = user.getRotationVec(1.0F);
            List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(5.0D)).expand(1.0D), RIDERS);
            if (!list.isEmpty()) {
                Vec3d vec3d2 = user.getCameraPosVec(1.0F);

                for (Entity entity : list) {
                    Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                    if (box.contains(vec3d2)) {
                        return TypedActionResult.pass(itemStack);
                    }
                }
            }

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                DogsledEntity dogsledEntity = new DogsledEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
                dogsledEntity.setDogsledType(this.type);
                dogsledEntity.setBodyYaw(user.getYaw());
                if (!world.isSpaceEmpty(dogsledEntity, dogsledEntity.getBoundingBox().expand(-0.1D))) {
                    return TypedActionResult.fail(itemStack);
                } else {
                    if (!world.isClient) {
                        world.spawnEntity(dogsledEntity);
                        if (!user.getAbilities().creativeMode) {
                            itemStack.decrement(1);
                        }
                    }

                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    return TypedActionResult.success(itemStack, world.isClient());
                }
            } else {
                return TypedActionResult.pass(itemStack);
            }
        }
    }

    static {
        RIDERS = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::collides);
    }
}
