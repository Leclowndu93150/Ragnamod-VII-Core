package com.leclowndu93150.ragnamod_vii_core.mixin;

import com.sammy.malum.registry.common.block.WoodTypeRegistry;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheets.class)
public class SheetsMixin {

    @Inject(method = "getSignMaterial", at = @At("HEAD"), cancellable = true)
    private static void fixMalumSignMaterial(WoodType woodType, CallbackInfoReturnable<Material> cir) {
        if (WoodTypeRegistry.WOOD_TYPES.contains(woodType)) {
            String name = woodType.name();
            String[] parts = name.split(":");
            ResourceLocation texture;
            if (parts.length == 2) {
                texture = new ResourceLocation(parts[0], "entity/signs/" + parts[1]);
            } else {
                texture = new ResourceLocation("entity/signs/" + name);
            }
            cir.setReturnValue(new Material(Sheets.SIGN_SHEET, texture));
        }
    }
}
