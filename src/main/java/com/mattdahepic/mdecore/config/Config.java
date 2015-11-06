package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.MDECore;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {
    public static boolean updateCheckEnabled = true;
    public static boolean waterBreaksRedstone = true;
    public static boolean waterBottlesFillCauldrons = true;
    public static boolean sleepDuringDayChangesToNight = false;
    public static void load (FMLPreInitializationEvent event) {
        MDECore.configFile = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }
    public static void syncConfig () {
        try {
            Config.processConfig(MDECore.configFile);
        } catch (Exception e) {
            MDECore.logger.error("Error loading config!");
        } finally {
            if (MDECore.configFile.hasChanged()) {
                MDECore.configFile.save();
            }
        }
    }
    @SubscribeEvent
    public void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(MDECore.MODID)) {
            MDECore.logger.info(MDECore.MODID, "Updating config...");
            syncConfig();
        }
    }
    public static void processConfig (Configuration c) {
        updateCheckEnabled = c.getBoolean("updateCheckEnabled", Configuration.CATEGORY_GENERAL, updateCheckEnabled, "Enable update checking for mods that implement MattDahEpic Core?");
        waterBreaksRedstone = c.getBoolean("waterBreaksRedstone",Configuration.CATEGORY_GENERAL,waterBreaksRedstone,"If true, water will wash away redstone. True is vanilla behaviour.");
        waterBottlesFillCauldrons = c.getBoolean("waterBottlesFillCauldrons",Configuration.CATEGORY_GENERAL,waterBottlesFillCauldrons, "If true, water bottles will fill cauldrons by 1 level. False is vanilla behaviour.");
        sleepDuringDayChangesToNight = c.getBoolean("sleepDuringDayChangesToNight",Configuration.CATEGORY_GENERAL,sleepDuringDayChangesToNight, "If true, you can sleep through the day to the next night. False is vanilla behaviour.");
    }
}
