package com.mattdahepic.mdecore.config.sync;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class ConfigProcessor {
    private final Class<? extends ConfigSyncable> configClass;
    private final Configuration config;
    final String configFileName;

    public static Map<String,ConfigProcessor> processorMap = Maps.newHashMap();

    private Map<String, Object> defaultValues = Maps.newHashMap();
    public Map<String, Object> currentValues = Maps.newHashMap();
    private Map<String, Object> originalValues = Maps.newHashMap();

    public ConfigProcessor (Class<? extends ConfigSyncable> configClass, Configuration config, String configName) {
        this.configClass = configClass;
        this.config = config;
        this.configFileName = configName;
        MinecraftForge.EVENT_BUS.register(this);
        processorMap.put(configName,this);
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
    /** Parses value and returns true if the value changed */
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
        Range range = Range.of(cfg.range());
        Property prop = null;
        Object newValue = null;
        for (ConfigType type : ConfigType.values()) {
            if (c == type.primitiveType || c == type.actualType.getRawType()) {
                switch (type) {
                    case BOOLEAN:
                    case BOOLEAN_ARR:
                    case BOOLEAN_LIST:
                        if (c.isArray()) {
                            prop = config.get(cfg.cat(),name,(boolean[]) defaultValues.get(name),comment);
                            newValue = prop.getBooleanList();
                        } else {
                            prop = config.get(cfg.cat(),name,(Boolean)defaultValues.get(name),comment);
                            newValue = prop.getBoolean();
                        }
                        break;
                    case DOUBLE:
                    case DOUBLE_ARR:
                    case DOUBLE_LIST:
                        if (c.isArray()) {
                            prop = config.get(cfg.cat(),name,(double[])defaultValues.get(name),comment);
                            newValue = range.clampArr(Arrays.asList(prop.getDoubleList())).toArray();
                        } else {
                            prop = config.get(cfg.cat(),name,(Double)defaultValues.get(name),comment);
                            newValue = range.clamp(prop.getDouble());
                        }
                        break;
                    case INTEGER:
                    case INTEGER_ARR:
                    case INTEGER_LIST:
                        if (c.isArray()) {
                            prop = config.get(cfg.cat(),name,(int[])defaultValues.get(name),comment);
                            newValue = range.clampArr(Arrays.asList(prop.getIntList())).toArray();
                        } else {
                            prop = config.get(cfg.cat(),name,(Integer)defaultValues.get(name),comment);
                            newValue = range.clamp(prop.getInt());
                        }
                        break;
                    case STRING:
                    case STRING_ARR:
                    case STRING_LIST:
                        if (c.isArray()) {
                            prop = config.get(cfg.cat(),name,(String[])defaultValues.get(name),comment);
                            newValue = prop.getStringList();
                        } else {
                            prop = config.get(cfg.cat(),name,(String)defaultValues.get(name),comment);
                            newValue = prop.getString();
                        }
                        break;
                    default:
                        throw new RuntimeException(String.format("Attempted to assign config value to non-number, non-string field %s",f.getName()));
                }
            }
        }
        //apply range
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

        currentValues.put(f.getName(), newValue);
        originalValues.put(f.getName(), newValue);
        f.set(null, newValue);

        return !value.equals(newValue);
    }

    void syncTo(Map<String, Object> values) {
        this.currentValues = values;
        try {
            for (Field f : configClass.getDeclaredFields()) {
                if (currentValues.containsKey(f.getName())) {
                    Config annot = f.getAnnotation(Config.class);
                    if (annot != null) {
                        if (annot.sync()) {
                            Object newVal = currentValues.get(f.getName());
                            Object oldVal = f.get(null);
                            if (!oldVal.equals(newVal)) {
                                MDECore.logger.debug("Config {}.{} differs from new data. Changing from {} to {}", configClass.getName(), f.getName(), oldVal, newVal);
                                f.set(null,newVal);
                            }
                        } else {
                            MDECore.logger.debug("Skipping syncing field {}.{} as it was marked sync=false", configClass.getName(), f.getName());
                        }
                    }
                } else if (f.getType().isAssignableFrom(Config.ConfigSubValue.class)) {
                    //TODO: assign all fields in sub config value
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* Event Handling */

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        MDECore.logger.debug(String.format("Sending server configs to client %s for %s", event.player.getDisplayNameString(), configFileName+".cfg"));
        PacketHandler.net.sendTo(new PacketConfigSync(this), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerLogout(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        syncTo(originalValues);
        MDECore.logger.debug(String.format("Reset configs to client values for %s", configFileName+".cfg"));
    }

    private enum ConfigType {
        INTEGER(TypeToken.of(Integer.class),Property.Type.INTEGER,int.class),
        INTEGER_ARR(TypeToken.of(int[].class), Property.Type.INTEGER),
        DOUBLE(TypeToken.of(Double.class), Property.Type.DOUBLE,double.class),
        DOUBLE_ARR(TypeToken.of(double[].class), Property.Type.DOUBLE),
        BOOLEAN(TypeToken.of(Boolean.class), Property.Type.BOOLEAN,boolean.class),
        BOOLEAN_ARR(TypeToken.of(boolean[].class), Property.Type.BOOLEAN),
        STRING(TypeToken.of(String.class), Property.Type.STRING,String.class),
        STRING_ARR(TypeToken.of(String[].class),Property.Type.STRING),
        FLOAT(TypeToken.of(Float.class),Property.Type.DOUBLE,float.class),
        FLOAT_ARR(TypeToken.of(float[].class), Property.Type.DOUBLE),
        INTEGER_LIST(new TypeToken<List<Integer>>(){}, Property.Type.INTEGER),
        DOUBLE_LIST(new TypeToken<List<Double>>(){}, Property.Type.DOUBLE),
        FLOAT_LIST(new TypeToken<List<Float>>(){}, Property.Type.DOUBLE),
        BOOLEAN_LIST(new TypeToken<List<Boolean>>(){}, Property.Type.BOOLEAN),
        STRING_LIST(new TypeToken<List<String>>(){},Property.Type.STRING);

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
