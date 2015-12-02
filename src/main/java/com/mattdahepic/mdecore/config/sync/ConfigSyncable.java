package com.mattdahepic.mdecore.config.sync;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.annot.*;
import com.mattdahepic.mdecore.config.sync.ConfigHelper;
import com.mattdahepic.mdecore.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigSyncable {
    public interface ITypeAdapter<ACTUAL, BASE> {
        TypeToken<ACTUAL> getActualType();
        Property.Type getType();

        /**
         * If this binds to a primitive type, return it here (eg: int.class).
         * Otherwise, return null.
         *
         * @return The class for this ITypeAdapter's primitive type.
         */
        @Nullable
        Class<?> getPrimitiveType();
        ACTUAL createActualType(BASE base);
        BASE createBaseType(ACTUAL actual);
    }
    private final List<ITypeAdapter<?, ?>> adapters = Lists.newArrayList();
    public <ACTUAL, BASE> ConfigSyncable addAdapter(ITypeAdapter<ACTUAL, BASE> adapter) {
        adapters.add(adapter);
        return this;
    }
    public <ACTUAL, BASE> ConfigSyncable addAdapters(ITypeAdapter<ACTUAL, BASE>... adapters) {
        for (ITypeAdapter<ACTUAL, BASE> adapter : adapters) {
            addAdapter(adapter);
        }
        return this;
    }
    static final Map<String, ConfigSyncable> configMap = Maps.newHashMap();

    private final Class<?> configs;
    private final Configuration configFile;
    final String configFileName;

    Map<String,Object> configValues = Maps.newHashMap();
    Map<String,Object> defaultValues = Maps.newHashMap();
    Map<String,Object> originalValues = Maps.newHashMap();
    private Set<String> sections = Sets.newHashSet();

    public ConfigSyncable (Class<?> configs,String configName,FMLPreInitializationEvent e) {
        this.configs = configs;
        this.configFile = new Configuration(new File(e.getModConfigurationDirectory().getAbsolutePath()+File.separator+"mattdahepic"+File.separator+configName+".cfg"));
        this.configFileName = configName;
        configMap.put(configName,this);
        MinecraftForge.EVENT_BUS.register(this);
        adapters.addAll(TypeAdapterBase.all);
    }

    public void process (boolean loadFromDisk) {
        if (loadFromDisk) {
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
    private boolean processField (Field f) throws Exception {
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

        Object newValue = getConfigValue(cfg.value(), getComment(f),f,value);
        configValues.put(f.getName(), newValue);
        originalValues.put(f.getName(), newValue);
        f.set(null, newValue);

        sections.add(cfg.value());

        return !value.equals(newValue);
    }
    @SuppressWarnings({"rawtypes","unchecked"})
    private Object getConfigValue (String section, String[] commentLines, Field f, Object defVal) {
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
                        prop = configFile.get(section,f.getName(),(boolean[])defVal, comment);
                        res = prop.getBooleanList();
                    } else {
                        prop = configFile.get(section, f.getName(), (Boolean) defVal, comment);
                        res = prop.getBoolean();
                    }
                    break;
                case DOUBLE:
                    if (defVal.getClass().isArray()) {
                        prop = configFile.get(section, f.getName(), (double[])defVal,comment);
                        res = ConfigHelper.bindDoubleArr(prop, Bound.of(bound.min.doubleValue(), bound.max.doubleValue()));
                    } else {
                        prop = configFile.get(section, f.getName(), (Double)defVal,comment);
                        res = ConfigHelper.bindValue(prop, Bound.of(bound.min.doubleValue(), bound.max.doubleValue()), (Double) defVal);
                    }
                     break;
                case INTEGER:
                    if (defVal.getClass().isArray()) {
                        prop = configFile.get(section, f.getName(), (int[]) defVal, comment);
                        res = ConfigHelper.bindIntArr(prop, Bound.of(bound.min.intValue(), bound.max.intValue()));
                    } else {
                        prop = configFile.get(section, f.getName(), (Integer) defVal, comment);
                        res = ConfigHelper.bindValue(prop, Bound.of(bound.min.intValue(), bound.max.intValue()), (Integer) defVal);
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
                ConfigHelper.setBounds(prop, bound);
                ConfigHelper.addCommentDetails(prop, bound);
                getRestartReq(f).apply(prop);
                return adapter.createActualType(res);
            }
        }
        throw new IllegalArgumentException(String.format("No adapter for type %s in class %s, field %s",f.getGenericType(),configs,f));
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


    @SuppressWarnings({"unchecked","rawtypes"})
    private ITypeAdapter getAdapterFor (Field f) {
        TypeToken<?> t = TypeToken.of(f.getGenericType());
        Class<?> c = f.getType();
        for (ITypeAdapter adapter : adapters) {
            if ((c.isPrimitive() && c == adapter.getPrimitiveType()) || adapter.getActualType().isAssignableFrom(t)) {
                return adapter;
            }
        }
        return null;
    }
    private String[] getComment (Field f) {
        Comment c = f.getAnnotation(Comment.class);
        return c == null ? new String[0] : c.value();
    }
    private Bound<Double> getBound (Field f) {
        Range r = f.getAnnotation(Range.class);
        return r == null ? Bound.MAX_BOUND : Bound.of(r.min(), r.max());
    }
    private boolean getNoSync (Field f) {
        return f.getAnnotation(NoSync.class) != null;
    }
    private ConfigHelper.RestartReqs getRestartReq (Field f) {
        RestartReq r = f.getAnnotation(RestartReq.class);
        return r == null ? ConfigHelper.RestartReqs.NONE : r.value();
    }
    public ConfigCategory getCategory(String category) {
        return configFile.getCategory(category);
    }
    public ImmutableSet<String> sections() {
        return ImmutableSet.copyOf(sections);
    }

    //Event Handling

    @Mod.EventHandler
    public void onLoginServer (PlayerEvent.PlayerLoggedInEvent e) {
        MDECore.logger.info("Syncing config "+configFileName+".cfg to connecting client "+e.player.getDisplayName()+".");
        PacketHandler.net.sendTo(new PacketConfigSync(this),(EntityPlayerMP)e.player);
    }
    @Mod.EventHandler
    public void onLogoutClient (FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        MDECore.logger.info("Resetting config values for config "+configFileName+".cfg.");
        syncTo(originalValues);
    }
}
