package com.leclowndu93150.ragnamod_vii_core.mixin.alchemistry;

import com.smashingmods.alchemistry.common.block.dissolver.DissolverBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.AbstractInventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DissolverBlockEntity.class)
public abstract class DissolverBlockEntityMixin extends AbstractInventoryBlockEntity {

    private DissolverBlockEntityMixin(String modId, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(modId, type, pos, state);
    }

    /**
     * @author Ragnamod VII Core
     * @reason Skip the buffer-flush pass entirely when the internal buffer is empty.
     *         The vanilla implementation calls setChanged() unconditionally every tick, which marks the chunk
     *         dirty 20×/sec for every placed Dissolver, even when the machine has no work to do.
     */
    @Overwrite(remap = false)
    public void tick() {
        super.tick();
        if (this.isProcessingPaused()) {
            return;
        }
        NonNullList<ItemStack> buffer = ((DissolverBlockEntityAccessor) this).ragnamodCore$getInternalBuffer();
        if (!buffer.isEmpty()) {
            ((DissolverBlockEntityAccessor) this).ragnamodCore$processBuffer();
        }
    }
}
