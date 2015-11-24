package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.LogHelper;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class Config {
    public static boolean updateCheckEnabled = true;
    public static final String DEBUG_CATEGORY = "debug_tools";
    public static boolean loadStateLogEnabled = false;
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
        loadStateLogEnabled = c.getBoolean("loadStateLogEnabled", DEBUG_CATEGORY, loadStateLogEnabled, "Enable a log entry when forge loading enters a different state?");
    }
}
