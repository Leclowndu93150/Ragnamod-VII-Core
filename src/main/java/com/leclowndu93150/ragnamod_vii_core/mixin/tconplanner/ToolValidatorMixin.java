package com.leclowndu93150.ragnamod_vii_core.mixin.tconplanner;

import net.tiffit.tconplanner.util.ToolValidator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.recipe.modifiers.ModifierRecipeLookup;

@Mixin(ToolValidator.class)
public class ToolValidatorMixin {

    @Redirect(
        method = "validateModRemoval(Lnet/tiffit/tconplanner/data/Blueprint;Lslimeknights/tconstruct/library/tools/nbt/ToolStack;Lnet/tiffit/tconplanner/data/ModifierInfo;)Lslimeknights/tconstruct/library/recipe/tinkerstation/ValidatedResult;",
        at = @At(
            value = "INVOKE",
            target = "Lslimeknights/tconstruct/library/recipe/modifiers/ModifierRecipeLookup;getNeededPerLevel(Lslimeknights/tconstruct/library/modifiers/ModifierId;)I",
            remap = false
        ),
        remap = false
    )
    private static int ragnamod$redirectValidateModRemoval(ModifierId modifier) {
        return ModifierRecipeLookup.getNeededPerLevel(modifier);
    }
}
