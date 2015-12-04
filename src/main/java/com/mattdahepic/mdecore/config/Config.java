package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.MDECore;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public abstract class Config {
    public static Configuration config;
    @Deprecated
    public static void load (String configName, FMLPreInitializationEvent e, Config sub) {
        config = new Configuration(new File(e.getModConfigurationDirectory().getAbsolutePath()+File.separator+"mattdahepic"+File.separator+configName+".cfg"));
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
        throw new RuntimeException("A mod is trying to use the OLD config interface. Please update that mod or backdate mdecore to version 1.8.8-1.5 until an update for that mod is available.");
    }
    public abstract void processConfig (Configuration c);
}
