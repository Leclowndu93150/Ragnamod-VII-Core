package com.leclowndu93150.ragnamod_vii_core.pipez;

import net.minecraft.core.Direction;

public interface IPipeBackoff {
    long ragnamodCore$getNextActiveTick(Direction side);
    void ragnamodCore$setNextActiveTick(Direction side, long tick);
    int ragnamodCore$getBackoffDelay(Direction side);
    void ragnamodCore$setBackoffDelay(Direction side, int delay);
}
