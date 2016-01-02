package com.mattdahepic.mdecore.config.sync;

import com.google.common.collect.*;
import com.google.common.reflect.TypeToken;
import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.MDEConfig;
import com.mattdahepic.mdecore.config.annot.*;
import com.mattdahepic.mdecore.config.annot.Range;
import com.mattdahepic.mdecore.network.PacketHandler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigProcessor {
    public interface ITypeAdapter<ACTUAL, BASE> {
        TypeToken<ACTUAL> getActualType();
        Property.Type getType();
        /**
         * If this binds to a primitive type, return it here (e.g. int.class).
         * Otherwise, return null.
         *
         * @return The class for this ITypeAdapter's primitive type.
         */
        @Nullable
        Class<?> getPrimitiveType();
        ACTUAL createActualType(BASE base);
        BASE createBaseType(ACTUAL actual);
    }
    static final Map<String, ConfigProcessor> processorMap = Maps.newHashMap();

    private final List<ITypeAdapter<?, ?>> adapters = Lists.newArrayList();

    private final Class<?> configs;
    private final Configuration configFile;
    final String configFileName;

    Map<String, Object> configValues = Maps.newHashMap();
    Map<String, Object> defaultValues = Maps.newHashMap();
    Map<String, Object> originalValues = Maps.newHashMap();

    private Set<String> sections = Sets.newHashSet();

    public ConfigProcessor(Class<?> configs, Configuration configFile, String configName) {
        this.configs = configs;
        this.configFile = configFile;
        this.configFileName = configName;
        processorMap.put(configName, this);
        FMLCommonHandler.instance().bus().register(this);
        adapters.addAll(TypeAdapterBase.all);
    }
    public <ACTUAL, BASE> ConfigProcessor addAdapter(ITypeAdapter<ACTUAL, BASE> adapter) {
        adapters.add(adapter);
        return this;
    }
    public <ACTUAL, BASE> ConfigProcessor addAdapters(ITypeAdapter<ACTUAL, BASE>... adapters) {
        for (ITypeAdapter<ACTUAL, BASE> adapter : adapters) {
            addAdapter(adapter);
        }
        return this;
    }
    /**
     * Processes all the configs in this processors class, optionally loading them
     * from file first.
     *
     * @param load
     *          If true, the values from the file will be loaded. Otherwise, the
     *          values existing in memory will be used.
     */
    public void process(boolean load) {
        if (load) {
            configFile.load();
        }

        try {
            for (Field f : configs.getDeclaredFields()) {
                processField(f);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        configFile.save();
    }
    // returns true if the config value changed
    private boolean processField(Field f) throws Exception {
        Config cfg = f.getAnnotation(Config.class);
        if (cfg == null) {
            return false;
        }
        String name = f.getName();
        Object value = defaultValues.get(name);
        if (value == null) {
            value = f.get(null);
            defaultValues.put(name, value);
        }

        Object newValue = getConfigValue(cfg.value(), getComment(f), f, value);

        configValues.put(f.getName(), newValue);
        originalValues.put(f.getName(), newValue);
        f.set(null, newValue);

        sections.add(cfg.value());

        return !value.equals(newValue);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object getConfigValue(String section, String[] commentLines, Field f, Object defVal) {
        Property prop = null;
        Object res = null;
        Bound<Double> bound = getBound(f);
        ITypeAdapter adapter = getAdapterFor(f);
        String comment = StringUtils.join(commentLines, "\n");
        if (adapter != null) {
            defVal = adapter.createBaseType(defVal);
            switch (adapter.getType()) {
                case BOOLEAN:
                    if (defVal.getClass().isArray()) {
                        prop = configFile.get(section, f.getName(), (boolean[]) defVal, comment);
                        res = prop.getBooleanList();
                    } else {
                        prop = configFile.get(section, f.getName(), (Boolean) defVal, comment);
                        res = prop.getBoolean();
                    }
                    break;
                case DOUBLE:
                    if (defVal.getClass().isArray()) {
                        prop = configFile.get(section, f.getName(), (double[]) defVal, comment);
                        res = ConfigSyncable.boundDoubleArr(prop, Bound.of(bound.min.doubleValue(), bound.max.doubleValue()));
                    } else {
                        prop = configFile.get(section, f.getName(), (Double) defVal, comment);
                        res = ConfigSyncable.boundValue(prop, Bound.of(bound.min.doubleValue(), bound.max.doubleValue()), (Double) defVal);
                    }
                    break;
                case INTEGER:
                    if (defVal.getClass().isArray()) {
                        prop = configFile.get(section, f.getName(), (int[]) defVal, comment);
                        res = ConfigSyncable.boundIntArr(prop, Bound.of(bound.min.intValue(), bound.max.intValue()));
                    } else {
                        prop = configFile.get(section, f.getName(), (Integer) defVal, comment);
                        res = ConfigSyncable.boundValue(prop, Bound.of(bound.min.intValue(), bound.max.intValue()), (Integer) defVal);
                    }
                    break;
                case STRING:
                    if (defVal.getClass().isArray()) {
                        prop = configFile.get(section, f.getName(), (String[]) defVal, comment);
                        res = prop.getStringList();
                    } else {
                        prop = configFile.get(section, f.getName(), (String) defVal, comment);
                        res = prop.getString();
                    }
                    break;
                default:
                    break;
            }
            if (res != null) {
                ConfigSyncable.setBounds(prop, bound);
                ConfigSyncable.addCommentDetails(prop, bound);
                getRestartReq(f).apply(prop);
                return adapter.createActualType(res);
            }
        }
        throw new IllegalArgumentException(String.format("No adapter for type %s in class %s, field %s", f.getGenericType(), configs, f));
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private ITypeAdapter getAdapterFor(Field f) {
        TypeToken<?> t = TypeToken.of(f.getGenericType());
        Class<?> c = f.getType();
        for (ITypeAdapter adapter : adapters) {
            if ((c.isPrimitive() && c == adapter.getPrimitiveType()) || adapter.getActualType().isAssignableFrom(t)) {
                return adapter;
            }
        }
        return null;
    }
    public ImmutableSet<String> sections() {
        return ImmutableSet.copyOf(sections);
    }
    public ConfigCategory getCategory(String category) {
        return configFile.getCategory(category);
    }
    public void syncTo(Map<String, Object> values) {
        this.configValues = values;
        for (String s : configValues.keySet()) {
            try {
                Field f = configs.getDeclaredField(s);
                Config annot = f.getAnnotation(Config.class);
                if (annot != null && !getNoSync(f)) {
                    Object newVal = configValues.get(s);
                    Object oldVal = f.get(null);
                    if (!oldVal.equals(newVal)) {
                        MDECore.logger.debug("Config {}.{} differs from new data. Changing from {} to {}", configs.getName(), f.getName(), oldVal, newVal);
                        f.set(null, newVal);
                    }
                } else if (annot != null) {
                    MDECore.logger.debug("Skipping syncing field {}.{} as it was marked NoSync", configs.getName(), f.getName());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private String[] getComment(Field f) {
        Comment c = f.getAnnotation(Comment.class);
        return c == null ? new String[0] : c.value();
    }

    private Bound<Double> getBound(Field f) {
        Range r = f.getAnnotation(Range.class);
        return r == null ? Bound.MAX_BOUND : Bound.of(r.min(), r.max());
    }

    private boolean getNoSync(Field f) {
        return f.getAnnotation(NoSync.class) != null;
    }

    private ConfigSyncable.RestartReqs getRestartReq(Field f) {
        RestartReq r = f.getAnnotation(RestartReq.class);
        return r == null ? ConfigSyncable.RestartReqs.NONE : r.value();
    }

    /* Event Handling */

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (MDEConfig.debugLogging) MDECore.logger.info(String.format("Sending server configs to client %s for %s", event.player.getDisplayNameString(), configFileName+".cfg"));
        PacketHandler.net.sendTo(new PacketConfigSync(this), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerLogout(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        syncTo(originalValues);
        if (MDEConfig.debugLogging) MDECore.logger.info(String.format("Reset configs to client values for %s", configFileName+".cfg"));
    }
}
