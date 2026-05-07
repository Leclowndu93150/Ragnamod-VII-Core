package com.leclowndu93150.ragnamod_vii_core.mixin.alchemistry;

import com.smashingmods.alchemylib.api.blockentity.processing.AbstractProcessingBlockEntity;
import com.smashingmods.alchemylib.api.blockentity.processing.ProcessingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractProcessingBlockEntity.class)
public abstract class AbstractProcessingBlockEntityMixin extends BlockEntity implements ProcessingBlockEntity {

    @Unique
    private static final int RAGNAMOD_CORE$RECIPE_LOOKUP_INTERVAL = 20;

    @Unique
    private int ragnamodCore$recipeLookupCooldown = 0;

    private AbstractProcessingBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * @author Ragnamod VII Core
     * @reason Throttle the per-tick recipe registry scan that AlchemyLib runs unconditionally on every machine,
     *         which causes idle Dissolvers (and friends) to burn CPU and mark their chunks dirty every tick.
     *         Inventory changes still trigger an immediate recipe update via the slot handler's onContentsChanged,
     *         so throttling the polling path here is safe.
     */
    @Overwrite(remap = false)
    public void tick() {
        Level level = this.level;
        if (level == null || level.isClientSide() || this.isProcessingPaused()) {
            return;
        }

        if (!this.isRecipeLocked()) {
            if (this.ragnamodCore$recipeLookupCooldown <= 0) {
                this.updateRecipe();
                this.ragnamodCore$recipeLookupCooldown = RAGNAMOD_CORE$RECIPE_LOOKUP_INTERVAL;
            } else {
                this.ragnamodCore$recipeLookupCooldown--;
            }
        }

        if (this.getCanProcess()) {
            this.processRecipe();
        }
    }
}
