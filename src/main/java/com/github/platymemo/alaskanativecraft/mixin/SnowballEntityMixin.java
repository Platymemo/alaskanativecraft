package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.AlaskaNativeCraft;
import com.github.platymemo.alaskanativecraft.config.AlaskaConfig;
import com.github.platymemo.alaskanativecraft.entity.PtarmiganEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowballEntity.class)
public class SnowballEntityMixin {
    @Inject(at = @At("TAIL"), method = "onEntityHit")
    private void makeThatBirbAPtarmigan(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (AlaskaConfig.getConfig().snowballConversion) {
            Entity entity = entityHitResult.getEntity();
            if ((entity instanceof ParrotEntity && !(entity instanceof PtarmiganEntity)) || entity instanceof ChickenEntity) {

                Identifier ptarmigan = new Identifier(AlaskaNativeCraft.MOD_ID, "ptarmigan");

                Text entityName = null;
                if (entity.hasCustomName()) {
                    entityName = entity.getCustomName();
                }

                NbtCompound entityTag = new NbtCompound();
                entityTag.putString("id", ptarmigan.toString());

                if (entity.getEntityWorld() instanceof ServerWorld world) {
                    Entity newEntity = EntityType.loadEntityWithPassengers(entityTag, world, (entityMaker) -> {
                        entityMaker.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entityMaker.getYaw(), entityMaker.getPitch());
                        return entityMaker;
                    });
                    if (newEntity != null) {
                        newEntity.setCustomName(entityName);
                    }

                    if (world.spawnNewEntityAndPassengers(newEntity)) {
                        entity.discard();
                    }
                }
            }
        }
    }
}
