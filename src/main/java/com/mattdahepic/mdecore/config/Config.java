package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.MDECore;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public abstract class Config {
    public static Configuration config;
    public static void load (String modid, FMLPreInitializationEvent e, Config sub) {
        config = new Configuration(new File(e.getModConfigurationDirectory().getAbsolutePath()+File.separator+"mattdahepic"+File.separator+modid+".cfg"));
        syncConfig(sub);
    }
    public static void syncConfig (Config sub) {
        try {
            sub.processConfig(config);
        } catch (Exception e) {
            MDECore.logger.error("Error loading config!");
        } finally {
            if (config.hasChanged()) config.save();
        }
    }
    public abstract void processConfig (Configuration c);
}
