package com.github.platymemo.alaskanativecraft.mixin;

import com.github.platymemo.alaskanativecraft.entity.DogsledEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.structure.*;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(IglooGenerator.Piece.class)
public abstract class IglooGeneratorMixin extends SimpleStructurePiece {
    private static final Identifier DOGSLED_LOOT_TABLE = LootTables.VILLAGE_SNOWY_HOUSE_CHEST;
    private static BlockPos lastPos;

    protected IglooGeneratorMixin(StructurePieceType type, int length, StructureManager structureManager, Identifier identifier, String string, StructurePlacementData structurePlacementData, BlockPos blockPos) {
        super(type, length, structureManager, identifier, string, structurePlacementData, blockPos);
        throw new AssertionError("AlaskaNativeCraft's IglooGeneratorMixin constructor called!");
    }

    @Inject(at = @At(value = "JUMP", opcode = Opcodes.IFNE, ordinal = 0),
            method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V",
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addDogsleds(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pos, CallbackInfo ci, Identifier identifier, StructurePlacementData structurePlacementData) {
        BlockPos chestLocation = this.pos.add(Structure.transform(structurePlacementData, new BlockPos(0, 1, 0)));
        if (lastPos == null || !lastPos.equals(chestLocation)) {
            DogsledEntity dogsled = new DogsledEntity(world.toServerWorld(), (double) chestLocation.getX() + 0.5D, (double) chestLocation.getY() + 0.5D, (double) chestLocation.getZ() + 0.5D);
            dogsled.setDogsledType(DogsledEntity.Type.SPRUCE);
            dogsled.setCustomName(new LiteralText("Abandoned Dogsled"));
            dogsled.setLootTable(DOGSLED_LOOT_TABLE, random.nextLong());
            world.spawnEntity(dogsled);
            lastPos = chestLocation;
        }
    }
}
