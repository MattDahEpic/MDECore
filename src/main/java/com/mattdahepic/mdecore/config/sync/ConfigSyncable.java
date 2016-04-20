package com.mattdahepic.mdecore.config.sync;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.io.File;

public abstract class ConfigSyncable {
    public abstract String getConfigVersion ();
    public abstract String getConfigName ();
    public abstract Class<? extends ConfigSyncable> getConfigClass ();

    private final String configFileName;
    public Configuration config;
    private static ConfigSyncable instance;

    public static ConfigSyncable instance () {
        if (instance == null) {
            throw new RuntimeException(String.format("Config instance is null, instantiate and initialize first!"));
        }
        return instance;
    }

    protected ConfigSyncable() {
        instance = this;
        configFileName = getConfigName();
        MinecraftForge.EVENT_BUS.register(this);
    }
    public final void initalize (FMLPreInitializationEvent e) {
        config = new Configuration(new File(e.getModConfigurationDirectory().getAbsolutePath()+ File.separator+"mattdahepic"+File.separator+configFileName+".cfg"),getConfigVersion());
        ConfigProcessor processor = new ConfigProcessor(getConfigClass(),config,configFileName);
        processor.process(true);
    }

    public static class ConfigSyncEvent extends Event {
        public final String configName;
        public ConfigSyncEvent (String configName) {
            this.configName = configName;
        }
    }

}
