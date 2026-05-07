package com.leclowndu93150.ragnamod_vii_core.mixin;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Mixin(targets = "com.buuz135.functionalstorage.client.loader.FramedModel$Baked")
public class FramedModelGetSpriteDataMixin {

    @Inject(
            method = "getSpriteData(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Ljava/util/Random;Lnet/minecraft/core/Direction;)Ljava/util/Optional;",
            at = @At("HEAD"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private static void ragnamodCore$guardCtmModel(BakedModel model, BlockState state, @Nullable Direction side, Random rand, @Nullable Direction rotation, CallbackInfoReturnable<Optional<List<Triple<TextureAtlasSprite, Integer, int[]>>>> cir) {
        try {
            model.getQuads(state, side, rand, EmptyModelData.INSTANCE);
        } catch (IllegalStateException e) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
