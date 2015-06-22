package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.LogHelper;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {
    public static boolean updateCheckEnabled = true;
    public static boolean waterBreaksRedstone = true;
    public static final String DEBUG_CATEGORY = "debug_tools";
    public static void load (FMLPreInitializationEvent event) {
        MDECore.configFile = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }
    public static void syncConfig () {
        try {
            Config.processConfig(MDECore.configFile);
        } catch (Exception e) {
            LogHelper.error(MDECore.MODID,"Error loading config!");
        } finally {
            if (MDECore.configFile.hasChanged()) {
                MDECore.configFile.save();
            }
        }
    }
    @SubscribeEvent
    public void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(MDECore.MODID)) {
            LogHelper.info(MDECore.MODID,"Updating config...");
            syncConfig();
        }
    }
    public static void processConfig (Configuration c) {
        updateCheckEnabled = c.getBoolean("updateCheckEnabled", Configuration.CATEGORY_GENERAL, updateCheckEnabled, "Enable update checking for mods that implement MattDahEpic Core?");
        waterBreaksRedstone = c.getBoolean("waterBreaksRedstone",Configuration.CATEGORY_GENERAL,waterBreaksRedstone,"If true, water will wash away redstone. True is vanilla behaviour.");
    }
}
