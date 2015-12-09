package com.mattdahepic.mdecore.config.sync;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ConfigSyncEvent extends Event {
    public final String configName;
    public ConfigSyncEvent (String configName) {
        this.configName = configName;
    }
}
