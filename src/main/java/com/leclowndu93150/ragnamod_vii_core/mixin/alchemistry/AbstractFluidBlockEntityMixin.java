package com.leclowndu93150.ragnamod_vii_core.mixin.alchemistry;

import com.smashingmods.alchemylib.api.blockentity.processing.AbstractFluidBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.AbstractProcessingBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.FluidBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.InventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractFluidBlockEntity.class)
public abstract class AbstractFluidBlockEntityMixin extends AbstractProcessingBlockEntity implements FluidBlockEntity, InventoryBlockEntity {

    private AbstractFluidBlockEntityMixin(String modId, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(modId, type, pos, state);
    }

    /**
     * @author Ragnamod VII Core
     * @reason Skip the entire tick when both input vectors are empty and there is no progress in flight.
     *         Liquifier consumes items, Atomizer consumes fluid — checking both keeps the optimization
     *         safe across either flow direction.
     */
    @Overwrite(remap = false)
    public void tick() {
        boolean fluidEmpty = this.getFluidStorage().isEmpty();
        boolean inputEmpty = this.getInputHandler().isEmpty();

        if (fluidEmpty && inputEmpty && this.getProgress() == 0) {
            return;
        }

        if (!fluidEmpty) {
            this.setCanProcess(this.canProcessRecipe());
        }
        super.tick();
    }
}
