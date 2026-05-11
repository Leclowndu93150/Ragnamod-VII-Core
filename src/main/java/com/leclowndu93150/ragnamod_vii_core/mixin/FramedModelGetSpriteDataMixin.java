package com.leclowndu93150.ragnamod_vii_core.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(targets = "com.buuz135.functionalstorage.client.loader.FramedModel$Baked")
public class FramedModelGetSpriteDataMixin {

    @Unique
    private static final ResourceLocation ragnamodCore$FIERY_BLOCK_ID = new ResourceLocation("twilightforest", "fiery_block");
    @Unique
    private static final ResourceLocation ragnamodCore$FIERY_BLOCK_SPRITE = new ResourceLocation("twilightforest", "block/fiery_block");

    @Unique
    private static final VarHandle ragnamodCore$NO_DELEGATE = ragnamodCore$sentinel();

    @Unique
    private static final ClassValue<VarHandle> ragnamodCore$DELEGATE_HANDLES = new ClassValue<>() {
        @Override
        protected VarHandle computeValue(Class<?> type) {
            Class<?> cls = type;
            while (cls != null && cls != Object.class) {
                for (Field field : cls.getDeclaredFields()) {
                    if (!BakedModel.class.isAssignableFrom(field.getType())) {
                        continue;
                    }
                    try {
                        field.setAccessible(true);
                        MethodHandles.Lookup privateLookup = MethodHandles.privateLookupIn(cls, MethodHandles.lookup());
                        return privateLookup.unreflectVarHandle(field);
                    } catch (Throwable ignored) {
                    }
                }
                cls = cls.getSuperclass();
            }
            return ragnamodCore$NO_DELEGATE;
        }
    };

    @Redirect(
            method = "getSpriteData(Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Ljava/util/Random;Lnet/minecraft/core/Direction;)Ljava/util/Optional;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Ljava/util/Random;Lnet/minecraftforge/client/model/data/IModelData;)Ljava/util/List;"
            ),
            remap = false,
            require = 0
    )
    private static List<BakedQuad> ragnamodCore$safeGetQuadsWithData(BakedModel self, BlockState state, Direction side, Random rand, IModelData ignored) {
        try {
            List<BakedQuad> outer = self.getQuads(state, side, rand, EmptyModelData.INSTANCE);
            if (!outer.isEmpty()) {
                return ragnamodCore$retextureFiery(state, outer);
            }
        } catch (IllegalStateException ignoredOuter) {
        }
        BakedModel target = ragnamodCore$unwrap(self);
        IModelData data = ragnamodCore$buildModelData(target, state);
        try {
            return ragnamodCore$retextureFiery(state, target.getQuads(state, side, rand, data));
        } catch (IllegalStateException e) {
            return List.of();
        }
    }

    @Redirect(
            method = "getSpriteFromModel(Lnet/minecraft/client/renderer/block/model/BakedQuad;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Ljava/util/List;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Ljava/util/Random;)Ljava/util/List;",
                    remap = true
            ),
            remap = false,
            require = 0
    )
    private static List<BakedQuad> ragnamodCore$safeGetQuadsBareCall(BakedModel self, BlockState state, Direction side, Random rand) {
        try {
            List<BakedQuad> outer = self.getQuads(state, side, rand);
            if (!outer.isEmpty()) {
                return ragnamodCore$retextureFiery(state, outer);
            }
        } catch (IllegalStateException ignoredOuter) {
        }
        BakedModel target = ragnamodCore$unwrap(self);
        IModelData data = ragnamodCore$buildModelData(target, state);
        try {
            return ragnamodCore$retextureFiery(state, target.getQuads(state, side, rand, data));
        } catch (IllegalStateException e) {
            return List.of();
        }
    }

    private static List<BakedQuad> ragnamodCore$retextureFiery(BlockState state, List<BakedQuad> quads) {
        if (quads.isEmpty() || state == null) {
            return quads;
        }
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (!ragnamodCore$FIERY_BLOCK_ID.equals(blockId)) {
            return quads;
        }
        TextureAtlasSprite fierySprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ragnamodCore$FIERY_BLOCK_SPRITE);
        if (fierySprite == null) {
            return quads;
        }
        List<BakedQuad> rewritten = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads) {
            rewritten.add(new BakedQuad(
                    quad.getVertices().clone(),
                    quad.getTintIndex(),
                    quad.getDirection(),
                    fierySprite,
                    quad.isShade()
            ));
        }
        return rewritten;
    }

    private static IModelData ragnamodCore$buildModelData(BakedModel model, BlockState state) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return EmptyModelData.INSTANCE;
        }
        try {
            return model.getModelData(mc.level, BlockPos.ZERO, state, EmptyModelData.INSTANCE);
        } catch (Throwable t) {
            return EmptyModelData.INSTANCE;
        }
    }

    private static BakedModel ragnamodCore$unwrap(BakedModel model) {
        BakedModel current = model;
        for (int i = 0; i < 8 && current != null; i++) {
            VarHandle handle = ragnamodCore$DELEGATE_HANDLES.get(current.getClass());
            if (handle == ragnamodCore$NO_DELEGATE) {
                return current;
            }
            Object inner = handle.get(current);
            if (!(inner instanceof BakedModel innerModel) || innerModel == current) {
                return current;
            }
            current = innerModel;
        }
        return current;
    }

    private static VarHandle ragnamodCore$sentinel() {
        try {
            return MethodHandles.lookup().findStaticVarHandle(FramedModelGetSpriteDataMixin.class, "ragnamodCore$DELEGATE_HANDLES", ClassValue.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
