package com.mattdahepic.mdecore.config.sync;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.io.File;

public abstract class ConfigSyncable {
    public abstract String getConfigVersion ();

    final String configFileName;
    public Configuration config;
    private static ConfigProcessor processor;
    private static ConfigSyncable instance;

    public static ConfigSyncable instance (String configName) {
        if (instance == null) {
            throw new RuntimeException(String.format("Config instance is null, instantiate and initialize first! Config: {}.cfg",configName));
        }
        return instance;
    }

    protected ConfigSyncable(String configName) {
        instance = this;
        configFileName = configName;
        MinecraftForge.EVENT_BUS.register(this);
    }
    public final void initalize (FMLPreInitializationEvent e) {
        config = new Configuration(new File(e.getModConfigurationDirectory().getAbsolutePath()+ File.separator+"mattdahepic"+File.separator+configFileName+".cfg"),getConfigVersion());
        processor = new ConfigProcessor(getClass(),config,configFileName);
        processor.process(true);
    }

    public static class ConfigSyncEvent extends Event {
        public final String configName;
        public ConfigSyncEvent (String configName) {
            this.configName = configName;
        }
    }

}
