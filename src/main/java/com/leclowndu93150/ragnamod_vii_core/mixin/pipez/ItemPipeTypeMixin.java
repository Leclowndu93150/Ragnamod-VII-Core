package com.leclowndu93150.ragnamod_vii_core.mixin.pipez;

import com.leclowndu93150.ragnamod_vii_core.CoreConfig;
import com.leclowndu93150.ragnamod_vii_core.pipez.IPipeBackoff;
import com.leclowndu93150.ragnamod_vii_core.pipez.TrackingItemHandler;
import de.maxhenkel.pipez.blocks.tileentity.PipeLogicTileEntity;
import de.maxhenkel.pipez.blocks.tileentity.PipeTileEntity;
import de.maxhenkel.pipez.blocks.tileentity.types.ItemPipeType;
import net.minecraft.core.Direction;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ItemPipeType.class, remap = false)
public class ItemPipeTypeMixin {

    @Unique
    private final ThreadLocal<Boolean> ragnamodCore$success = ThreadLocal.withInitial(() -> Boolean.FALSE);

    @Inject(method = "insertEqually", at = @At("HEAD"), cancellable = true, remap = false)
    private void ragnamodCore$startEqually(PipeLogicTileEntity tileEntity, Direction side,
                                           List<PipeTileEntity.Connection> connections, IItemHandler itemHandler, CallbackInfo ci) {
        ragnamodCore$success.set(Boolean.FALSE);
        if (ragnamodCore$shouldSuppress(tileEntity, side)) {
            ci.cancel();
        }
    }

    @Inject(method = "insertOrdered", at = @At("HEAD"), cancellable = true, remap = false)
    private void ragnamodCore$startOrdered(PipeLogicTileEntity tileEntity, Direction side,
                                           List<PipeTileEntity.Connection> connections, IItemHandler itemHandler, CallbackInfo ci) {
        ragnamodCore$success.set(Boolean.FALSE);
        if (ragnamodCore$shouldSuppress(tileEntity, side)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "insertEqually", at = @At("HEAD"), argsOnly = true, remap = false)
    private IItemHandler ragnamodCore$wrapHandlerEqually(IItemHandler handler) {
        if (!CoreConfig.PIPEZ_BACKOFF_ENABLED.get()) {
            return handler;
        }
        return new TrackingItemHandler(handler, () -> ragnamodCore$success.set(Boolean.TRUE));
    }

    @ModifyVariable(method = "insertOrdered", at = @At("HEAD"), argsOnly = true, remap = false)
    private IItemHandler ragnamodCore$wrapHandlerOrdered(IItemHandler handler) {
        if (!CoreConfig.PIPEZ_BACKOFF_ENABLED.get()) {
            return handler;
        }
        return new TrackingItemHandler(handler, () -> ragnamodCore$success.set(Boolean.TRUE));
    }

    @Inject(method = "insertEqually", at = @At("RETURN"), remap = false)
    private void ragnamodCore$endEqually(PipeLogicTileEntity tileEntity, Direction side, List<PipeTileEntity.Connection> connections,
                                         IItemHandler itemHandler, CallbackInfo ci) {
        ragnamodCore$applyBackoff(tileEntity, side);
    }

    @Inject(method = "insertOrdered", at = @At("RETURN"), remap = false)
    private void ragnamodCore$endOrdered(PipeLogicTileEntity tileEntity, Direction side, List<PipeTileEntity.Connection> connections,
                                         IItemHandler itemHandler, CallbackInfo ci) {
        ragnamodCore$applyBackoff(tileEntity, side);
    }

    @Unique
    private boolean ragnamodCore$shouldSuppress(PipeLogicTileEntity tileEntity, Direction side) {
        if (!CoreConfig.PIPEZ_BACKOFF_ENABLED.get()) {
            return false;
        }
        if (tileEntity instanceof IPipeBackoff backoff) {
            long nextActive = backoff.ragnamodCore$getNextActiveTick(side);
            return tileEntity.getLevel().getGameTime() < nextActive;
        }
        return false;
    }

    @Unique
    private void ragnamodCore$applyBackoff(PipeLogicTileEntity tileEntity, Direction side) {
        if (!CoreConfig.PIPEZ_BACKOFF_ENABLED.get()) {
            return;
        }
        if (!(tileEntity instanceof IPipeBackoff backoff)) {
            return;
        }

        if (Boolean.TRUE.equals(ragnamodCore$success.get())) {
            backoff.ragnamodCore$setBackoffDelay(side, 0);
            backoff.ragnamodCore$setNextActiveTick(side, 0L);
            return;
        }

        int baseDelay = CoreConfig.PIPEZ_BASE_BACKOFF_TICKS.get();
        int maxDelay = CoreConfig.PIPEZ_MAX_BACKOFF_TICKS.get();
        int currentDelay = backoff.ragnamodCore$getBackoffDelay(side);
        int newDelay = (currentDelay == 0) ? baseDelay : Math.min(currentDelay * 2, maxDelay);

        backoff.ragnamodCore$setBackoffDelay(side, newDelay);
        backoff.ragnamodCore$setNextActiveTick(side, tileEntity.getLevel().getGameTime() + newDelay);
    }
}
