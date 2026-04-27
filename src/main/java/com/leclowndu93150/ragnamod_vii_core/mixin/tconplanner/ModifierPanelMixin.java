package com.leclowndu93150.ragnamod_vii_core.mixin.tconplanner;

import net.tiffit.tconplanner.screen.ModifierPanel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;

@Mixin(ModifierPanel.class)
public class ModifierPanelMixin {

    @Redirect(
        method = "<init>(IIIILnet/minecraft/world/item/ItemStack;Lslimeknights/tconstruct/library/tools/nbt/ToolStack;Ljava/util/List;Lnet/tiffit/tconplanner/screen/PlannerScreen;)V",
        at = @At(
            value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/recipe/modifiers/ModifierRecipeLookup;getNeededPerLevel(Lslimeknights/tconstruct/library/modifiers/ModifierId;)I",
            remap = false
        ),
        remap = false
    )
    private int ragnamod$redirectModifierPanelCtor(ModifierId modifier) {
        return ModifierRecipeLookup.getNeededPerLevel(modifier);
    }
}
