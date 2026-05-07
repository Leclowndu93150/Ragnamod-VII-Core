package com.leclowndu93150.ragnamod_vii_core.mixin.alchemistry;

import com.smashingmods.alchemylib.api.blockentity.processing.AbstractInventoryBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.AbstractProcessingBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.InventoryBlockEntity;
import com.smashingmods.alchemylib.api.storage.ProcessingSlotHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractInventoryBlockEntity.class)
public abstract class AbstractInventoryBlockEntityMixin extends AbstractProcessingBlockEntity implements InventoryBlockEntity {

    private AbstractInventoryBlockEntityMixin(String modId, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(modId, type, pos, state);
    }

    /**
     * @author Ragnamod VII Core
     * @reason Skip the entire tick (including the parent's recipe lookup) when there is no work to do.
     *         An idle inventory machine has an empty input slot and zero progress, so updateRecipe() and
     *         canProcessRecipe() would return nothing actionable. Inventory changes still trigger immediate
     *         recipe updates through the slot handler's onContentsChanged.
     */
    @Overwrite(remap = false)
    public void tick() {
        ProcessingSlotHandler input = this.getInputHandler();
        boolean inputEmpty = input.isEmpty();

        if (inputEmpty && this.getProgress() == 0) {
            return;
        }

        if (!inputEmpty) {
            this.setCanProcess(this.canProcessRecipe());
        }
        super.tick();
    }
}
