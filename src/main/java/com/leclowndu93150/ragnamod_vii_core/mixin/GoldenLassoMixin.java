package com.leclowndu93150.ragnamod_vii_core.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import onelemonyboi.miniutilities.items.GoldenLasso;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(GoldenLasso.class)
public class GoldenLassoMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true, remap = false)
    private void safeAppendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        if (stack.getTag() != null && stack.getTag().contains("EntityTag")) {
            EntityType.byString(stack.getTagElement("EntityTag").getString("id"))
                .ifPresent(type -> tooltip.add(new TextComponent("Contains: ").append(type.getDescription())));
        }
        ci.cancel();
    }
}
