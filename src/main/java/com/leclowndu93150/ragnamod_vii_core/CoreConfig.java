package com.leclowndu93150.ragnamod_vii_core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public final class CoreConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GOLDEN_LASSO_BLACKLIST;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("golden_lasso");
        GOLDEN_LASSO_BLACKLIST = builder
            .comment("Entity types that the Golden Lasso cannot capture or release. Use registry IDs like \"minecraft:player\".")
            .defineListAllowEmpty(
                List.of("blacklist"),
                () -> List.of("minecraft:player"),
                obj -> obj instanceof String s && ResourceLocation.tryParse(s) != null
            );
        builder.pop();
        SPEC = builder.build();
    }

    private CoreConfig() {}

    public static boolean isLassoBlacklisted(Entity entity) {
        return isLassoBlacklisted(EntityType.getKey(entity.getType()));
    }

    public static boolean isLassoBlacklisted(ResourceLocation id) {
        if (id == null) return false;
        for (String entry : GOLDEN_LASSO_BLACKLIST.get()) {
            if (id.toString().equals(entry)) return true;
        }
        return false;
    }

    public static boolean isLassoBlacklistedById(String id) {
        if (id == null || id.isEmpty()) return true;
        ResourceLocation rl = ResourceLocation.tryParse(id);
        if (rl == null) return true;
        if (!ForgeRegistries.ENTITIES.containsKey(rl)) return true;
        return isLassoBlacklisted(rl);
    }
}
