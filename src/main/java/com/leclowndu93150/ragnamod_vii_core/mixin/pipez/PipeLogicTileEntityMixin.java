package com.leclowndu93150.ragnamod_vii_core.mixin.pipez;

import com.leclowndu93150.ragnamod_vii_core.pipez.IPipeBackoff;
import de.maxhenkel.pipez.blocks.tileentity.PipeLogicTileEntity;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = PipeLogicTileEntity.class, remap = false)
public class PipeLogicTileEntityMixin implements IPipeBackoff {

    @Unique
    private final long[] ragnamodCore$nextActiveTick = new long[6];

    @Unique
    private final int[] ragnamodCore$backoffDelay = new int[6];

    @Override
    public long ragnamodCore$getNextActiveTick(Direction side) {
        return ragnamodCore$nextActiveTick[side.ordinal()];
    }

    @Override
    public void ragnamodCore$setNextActiveTick(Direction side, long tick) {
        ragnamodCore$nextActiveTick[side.ordinal()] = tick;
    }

    @Override
    public int ragnamodCore$getBackoffDelay(Direction side) {
        return ragnamodCore$backoffDelay[side.ordinal()];
    }

    @Override
    public void ragnamodCore$setBackoffDelay(Direction side, int delay) {
        ragnamodCore$backoffDelay[side.ordinal()] = delay;
    }
}
