package com.leclowndu93150.ragnamod_vii_core.mixin;

import com.leclowndu93150.ragnamod_vii_core.CoreConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import onelemonyboi.miniutilities.items.GoldenLasso;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(GoldenLasso.class)
public class GoldenLassoMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true, require = 0)
    private void safeAppendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        if (stack.getTag() != null && stack.getTag().contains("EntityTag")) {
            EntityType.byString(stack.getTagElement("EntityTag").getString("id"))
                .ifPresent(type -> tooltip.add(new TextComponent("Contains: ").append(type.getDescription())));
        }
        ci.cancel();
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void safeUseOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = context.getItemInHand();
        if (stack.getTag() == null || !stack.getTag().contains("EntityTag")) return;

        String id = stack.getTagElement("EntityTag").getString("id");
        boolean unresolvable = EntityType.byString(id).isEmpty();
        boolean blacklisted = CoreConfig.isLassoBlacklistedById(id);

        if (unresolvable || blacklisted) {
            stack.setTag(null);
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method = "onRightClick", at = @At("HEAD"), cancellable = true, remap = false)
    private static void blockBlacklistedCapture(PlayerInteractEvent.EntityInteract event, CallbackInfo ci) {
        if (event.getItemStack().getItem() instanceof GoldenLasso
            && CoreConfig.isLassoBlacklisted(event.getTarget())) {
            event.setCanceled(true);
            ci.cancel();
        }
    }
}
