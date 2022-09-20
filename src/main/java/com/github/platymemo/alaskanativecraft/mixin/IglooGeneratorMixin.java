package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.structure.*;
import net.minecraft.structure.piece.SimpleStructurePiece;
import net.minecraft.structure.piece.StructurePieceType;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(IglooGenerator.Piece.class)
public abstract class IglooGeneratorMixin extends SimpleStructurePiece {
    private static final Identifier DOGSLED_LOOT_TABLE = LootTables.VILLAGE_SNOWY_HOUSE_CHEST;
    private static BlockPos lastPos;

    @Shadow
    private static StructurePlacementData createPlacementData(BlockRotation rotation, Identifier identifier) {
        throw new AssertionError("AlaskaNativeCraft's IglooGeneratorMixin shadowed method called!");
    }

    @Shadow
    private static BlockPos getPosOffset(Identifier identifier, BlockPos pos, int yOffset) {
        throw new AssertionError("AlaskaNativeCraft's IglooGeneratorMixin shadowed method called!");
    }

    protected IglooGeneratorMixin(StructureTemplateManager manager, Identifier identifier, BlockPos pos, BlockRotation rotation, int yOffset) {
        super(StructurePieceType.IGLOO, 0, manager, identifier, identifier.toString(), createPlacementData(rotation, identifier), getPosOffset(identifier, pos, yOffset));
        throw new AssertionError("AlaskaNativeCraft's IglooGeneratorMixin constructor called!");
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/piece/SimpleStructurePiece;generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/structure/StructureManager;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/random/RandomGenerator;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V", shift = At.Shift.AFTER),
            method = "generate",
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addDogsleds(StructureWorldAccess world, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomGenerator random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos pos, CallbackInfo ci) {
        // DanikingRD:
        // Not sure what's the context of this
        // For now i just added a new StructurePlacementData() to make it compile
        // TODO: get placement data at runtime
        BlockPos chestLocation = this.pos.add(Structure.transform(new StructurePlacementData(), new BlockPos(0, 1, 0)));
        if (lastPos == null || !lastPos.equals(chestLocation)) {
            DogsledEntity dogsled = new DogsledEntity(world.toServerWorld(), (double) chestLocation.getX() + 0.5D, (double) chestLocation.getY() + 0.5D, (double) chestLocation.getZ() + 0.5D);
            dogsled.setDogsledType(DogsledEntity.Type.SPRUCE);
            dogsled.setCustomName(Text.literal("Abandoned Dogsled"));
            dogsled.setLootTable(DOGSLED_LOOT_TABLE, random.nextLong());
            world.spawnEntity(dogsled);
            lastPos = chestLocation;
        }
    }
}
