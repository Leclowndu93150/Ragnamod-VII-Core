package com.leclowndu93150.ragnamod_vii_core.mixin.tconplanner;

import net.tiffit.tconplanner.util.ModifierStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;

@Mixin(ModifierStack.class)
public class ModifierStackMixin {

    @Redirect(
        method = "setIncrementalDiff(Lslimeknights/tconstruct/library/modifiers/Modifier;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/recipe/modifiers/ModifierRecipeLookup;getNeededPerLevel(Lslimeknights/tconstruct/library/modifiers/ModifierId;)I",
            remap = false
        ),
        remap = false
    )
    private int ragnamod$redirectSetIncremental(ModifierId modifier) {
        return ModifierRecipeLookup.getNeededPerLevel(modifier);
    }

    @Redirect(
        method = "lambda$applyIncrementals$2(Lslimeknights/tconstruct/library/tools/nbt/ToolStack;Lnet/tiffit/tconplanner/data/ModifierInfo;)V",
        at = @At(
            value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/recipe/modifiers/ModifierRecipeLookup;getNeededPerLevel(Lslimeknights/tconstruct/library/modifiers/ModifierId;)I",
            remap = false
        ),
        remap = false
    )
    private int ragnamod$redirectApplyIncrementalsLambda(ModifierId modifier) {
        return ModifierRecipeLookup.getNeededPerLevel(modifier);
    }
}
