package com.mattdahepic.mdecore.config;

import net.minecraftforge.common.config.Configuration;

public class MDEConfig extends Config {
    public static final String CATEGORY_TWEAKS = "tweaks";
    public static final String CACEGORY_DEBUG = "debug";

    public static boolean updateCheckEnabled = true;
    public static boolean waterBreaksRedstone = true;
    public static boolean waterBottlesFillCauldrons = true;
    public static boolean sleepDuringDayChangesToNight = false;
    public static boolean reportUsageStats = true;
    public static boolean debugLogging = false;
    @Override
    public void processConfig (Configuration c) {
        //general
        reportUsageStats = c.getBoolean("reportUsageStats",Configuration.CATEGORY_GENERAL,reportUsageStats,"If this value is true anonymous statistics about any MattDahEpic mods and their versions you are running will be sent to the author through Google Analytics.");
        updateCheckEnabled = c.getBoolean("updateCheckEnabled", Configuration.CATEGORY_GENERAL, updateCheckEnabled, "Enable update checking for mods that implement MattDahEpic Core?");
        //tweaks
        waterBreaksRedstone = c.getBoolean("waterBreaksRedstone",CATEGORY_TWEAKS,waterBreaksRedstone,"If true, water will wash away redstone. True is vanilla behaviour.");
        waterBottlesFillCauldrons = c.getBoolean("waterBottlesFillCauldrons",CATEGORY_TWEAKS,waterBottlesFillCauldrons, "If true, water bottles will fill cauldrons by 1 level. False is vanilla behaviour.");
        sleepDuringDayChangesToNight = c.getBoolean("sleepDuringDayChangesToNight",CATEGORY_TWEAKS,sleepDuringDayChangesToNight, "If true, you can sleep through the day to the next night. False is vanilla behaviour.");
        //debug
        debugLogging = c.getBoolean("debugLogging",CACEGORY_DEBUG,debugLogging,"If true extra lines will be printed to the console about things that are happening.");
    }
}
