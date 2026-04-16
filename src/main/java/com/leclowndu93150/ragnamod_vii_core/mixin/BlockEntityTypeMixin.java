package com.leclowndu93150.ragnamod_vii_core.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface BlockEntityTypeMixin<T extends BlockEntity> {

    @Accessor("validBlocks")
    Set<Block> getValidBlocks();

    @Mutable
    @Accessor("validBlocks")
    void setValidBlocks(Set<Block> validBlocks);
}
