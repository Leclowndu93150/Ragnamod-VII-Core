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
    public static final ForgeConfigSpec.IntValue PIPEZ_BASE_BACKOFF_TICKS;
    public static final ForgeConfigSpec.IntValue PIPEZ_MAX_BACKOFF_TICKS;
    public static final ForgeConfigSpec.BooleanValue PIPEZ_BACKOFF_ENABLED;

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

        builder.push("pipez_backoff");
        PIPEZ_BACKOFF_ENABLED = builder
            .comment("Enable exponential backoff for Pipez item/fluid/energy pipes that fail to transfer. Massively reduces tick cost on idle networks with Infinity upgrades.")
            .define("enabled", true);
        PIPEZ_BASE_BACKOFF_TICKS = builder
            .comment("Initial backoff delay in ticks after the first failed transfer attempt.")
            .defineInRange("baseBackoffTicks", 2, 1, 1200);
        PIPEZ_MAX_BACKOFF_TICKS = builder
            .comment("Maximum backoff delay in ticks. The delay doubles after each failure up to this cap.")
            .defineInRange("maxBackoffTicks", 32, 1, 1200);
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
