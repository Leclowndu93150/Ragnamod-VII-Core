package com.leclowndu93150.ragnamod_vii_core.mixin;

import com.github.elenterius.biomancy.block.veins.FleshVeinsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.github.elenterius.biomancy.fluid.AcidFluid;

@Mixin(AcidFluid.class)
public class AcidFluidMixin {

    @Inject(method = "destroyFleshVeins", at = @At("HEAD"), cancellable = true, remap = false)
    private void destroyFleshVeins(Level level, BlockPos liquidPos, Block block, BlockState blockState, BlockPos pos, CallbackInfo ci) {
        if (block instanceof FleshVeinsBlock) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
            ci.cancel();
        }
    }
}
