package com.mattdahepic.mdecore.config.v2;

import com.google.common.collect.Maps;
import com.mattdahepic.mdecore.config.v2.annot.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

class ConfigProcessor {
    private final Class<? extends ConfigSyncable> configClass;
    private final Configuration config;
    final String configFileName;

    private Map<String, Object> defaultValues = Maps.newHashMap();

    public ConfigProcessor (Class<? extends ConfigSyncable> configClass, Configuration config, String configName) {
        this.configClass = configClass;
        this.config = config;
        this.configFileName = configName;
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Process the config file for this processor.
     * @param load Should the file be loaded from disk or should the values in memory be used?
     */
    public void process (boolean load) {
        if (load) config.load();

        try {
            for (Field f : configClass.getDeclaredFields()) {
                processField(f);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        config.save();
    }
    private boolean processField (Field f) throws Exception {
        Config cfg = configClass.getAnnotation(Config.class);
        if (cfg == null) return false;

        String name = f.getName();
        Object value = defaultValues.get(name);
        if (value == null) {
            value = f.get(null);
            defaultValues.put(name,value);
        }

        /* BEGIN CONFIG VALUE PROCESSING */
        Class<?> c = f.getType();
        if (c.isAssignableFrom(Number.class) && c.isAssignableFrom(Comparable.class)) { //is number

        } else if (c.isAssignableFrom(String.class)) { //string

        } else if (c.isAssignableFrom(Serializable.class)) {

        } else {
            throw new RuntimeException("Attempted to apply config value to a non number, non string, non serializable field!");
        }
        /* END CONFIG VALUE PROCESSING */
    }
}
