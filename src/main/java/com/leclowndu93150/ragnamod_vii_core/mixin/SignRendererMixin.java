package com.leclowndu93150.ragnamod_vii_core.mixin;

import com.sammy.malum.registry.common.block.WoodTypeRegistry;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(SignRenderer.class)
public class SignRendererMixin {

    @Shadow
    @Final
    @Mutable
    private Map<WoodType, SignRenderer.SignModel> signModels;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addMalumSignModels(BlockEntityRendererProvider.Context context, CallbackInfo ci) {
        HashMap<WoodType, SignRenderer.SignModel> mutable = new HashMap<>(signModels);
        for (WoodType woodType : WoodTypeRegistry.WOOD_TYPES) {
            if (!mutable.containsKey(woodType)) {
                mutable.put(woodType, new SignRenderer.SignModel(
                        context.bakeLayer(ModelLayers.createSignModelName(woodType))
                ));
            }
        }
        signModels = mutable;
    }
}
