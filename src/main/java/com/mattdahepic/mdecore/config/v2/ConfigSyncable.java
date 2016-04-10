package com.mattdahepic.mdecore.config.v2;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public abstract class ConfigSyncable {
    public abstract String getConfigVersion ();

    final String configFileName;
    public Configuration config;
    public static ConfigProcessor processor;

    protected ConfigSyncable(String configName) {
        configFileName = configName;
        MinecraftForge.EVENT_BUS.register(this);
    }
    public final void initalize (FMLPreInitializationEvent e) {
        config = new Configuration(new File(e.getModConfigurationDirectory().getAbsolutePath()+ File.separator+"mattdahepic"+File.separator+configFileName+".cfg"),getConfigVersion());
        processor = new ConfigProcessor(getClass(),config,configFileName);
        processor.process(true);
    }
}
