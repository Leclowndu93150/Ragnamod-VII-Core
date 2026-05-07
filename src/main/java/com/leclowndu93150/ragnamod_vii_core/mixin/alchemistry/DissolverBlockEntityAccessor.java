package com.leclowndu93150.ragnamod_vii_core.mixin.alchemistry;

import com.smashingmods.alchemistry.common.block.dissolver.DissolverBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DissolverBlockEntity.class)
public interface DissolverBlockEntityAccessor {

    @Accessor("internalBuffer")
    NonNullList<ItemStack> ragnamodCore$getInternalBuffer();

    @Invoker("processBuffer")
    void ragnamodCore$processBuffer();
}
