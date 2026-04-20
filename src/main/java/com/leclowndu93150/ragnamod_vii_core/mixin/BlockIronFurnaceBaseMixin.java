package com.leclowndu93150.ragnamod_vii_core.mixin;

import ironfurnaces.blocks.furnaces.BlockIronFurnaceBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

@Mixin(BlockIronFurnaceBase.class)
public class BlockIronFurnaceBaseMixin {

    @Redirect(
            method = "getComparatorInputOverride",
            at = @At(value = "INVOKE", target = "Lorg/apache/commons/compress/utils/Lists;newArrayList()Ljava/util/ArrayList;"),
            remap = false
    )
    private ArrayList<?> redirectBadLists() {
        return new ArrayList<>();
    }
}
