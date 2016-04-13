package com.mattdahepic.mdecore.config.v2;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.mattdahepic.mdecore.config.v2.annot.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.StringUtils;

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
                if (f.getType().isAssignableFrom(Config.ConfigSubValue.class)) {
                    //TODO: process all fields in sub class
                } else {
                    processField(f);
                }
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
        String comment = StringUtils.join(cfg.comment(),"\n");
        Property prop = null;
        for (ConfigType type : ConfigType.values()) {
            //if config type matches
            //prop = config.get();
        }


        //apply range
        Range range = Range.of(cfg.range());
        range.apply(prop);
        //apply comment
        if (!range.equals(Range.MAX_RANGE)) { //has a range
            if (range.min.doubleValue() == range.min.intValue() && range.max.intValue() == range.max.intValue()) { //if bound is integer
                prop.setComment(prop.getComment()+String.format("\nRange: [%s - %s]", range.min.intValue(), range.max.intValue()));
            } else {
                prop.setComment(prop.getComment()+String.format("\nRange: [%s - %s]", range.min, range.max));
            }
        }
        cfg.restartReq().apply(prop); //apply restart requirement
        /* END CONFIG VALUE PROCESSING */
    }

    private enum ConfigType {
        INTEGER(TypeToken.of(Integer.class),Property.Type.INTEGER,int.class),
        INTEGER_ARR,
        DOUBLE,
        DOUBLE_ARR,
        BOOLEAN,
        BOOLEAN_ARR,
        STRING,
        STRING_ARR,
        FLOAT,
        FLOAT_ARR,
        INTEGER_LIST,
        DOUBLE_LIST,
        FLOAT_LIST,
        BOOLEAN_LIST,
        STRING_LIST

        private final TypeToken actualType;
        private final Property.Type type;
        private final Class<?> primitiveType;

        ConfigType(TypeToken actualType, Property.Type type, Class<?> primitiveType) {
            this.actualType = actualType;
            this.type = type;
            this.primitiveType = primitiveType;
        }
        ConfigType(TypeToken actualType, Property.Type type) {
            this(actualType,type,null);
        }
    }
}
