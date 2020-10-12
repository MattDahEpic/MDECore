package com.mattdahepic.mdecore.common.registries;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nullable;

public class ConfigRegistry {
    public static void registerConfig (@Nullable ForgeConfigSpec client, ForgeConfigSpec common) {
        if (client != null) ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT,client);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,common);
    }
}
