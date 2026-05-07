package com.leclowndu93150.ragnamod_vii_core;

import com.leclowndu93150.ragnamod_vii_core.mixin.BlockEntityTypeMixin;
import com.sammy.malum.registry.common.block.BlockRegistry;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import team.lodestar.lodestone.setup.LodestoneBlockEntityRegistry;

import java.util.HashSet;
import java.util.Set;

@Mod("ragnamod_vii_core")
public class RagnamodVIICore {
    public RagnamodVIICore() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CoreConfig.SPEC, "ragnamod_vii_core-common.toml");
    }

    @Mod.EventBusSubscriber(modid = "ragnamod_vii_core", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonEvents {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                BlockEntityType<?> signType = LodestoneBlockEntityRegistry.SIGN.get();
                BlockEntityTypeMixin<?> accessor = (BlockEntityTypeMixin<?>) signType;
                Set<Block> current = accessor.getValidBlocks();
                HashSet<Block> expanded = new HashSet<>(current);
                expanded.add(BlockRegistry.RUNEWOOD_SIGN.get());
                expanded.add(BlockRegistry.RUNEWOOD_WALL_SIGN.get());
                expanded.add(BlockRegistry.SOULWOOD_SIGN.get());
                expanded.add(BlockRegistry.SOULWOOD_WALL_SIGN.get());
                accessor.setValidBlocks(expanded);
            });
        }
    }

    @Mod.EventBusSubscriber(modid = "ragnamod_vii_core", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(LodestoneBlockEntityRegistry.SIGN.get(), SignRenderer::new);
        }

        @SubscribeEvent
        public static void onTextureStitch(TextureStitchEvent.Pre event) {
            if (event.getAtlas().location().equals(Sheets.SIGN_SHEET)) {
                event.addSprite(new ResourceLocation("malum", "entity/signs/runewood"));
                event.addSprite(new ResourceLocation("malum", "entity/signs/soulwood"));
            }
        }
    }
}
