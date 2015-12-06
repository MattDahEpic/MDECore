package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.config.annot.Comment;
import com.mattdahepic.mdecore.config.annot.NoSync;
import com.mattdahepic.mdecore.config.annot.RestartReq;
import com.mattdahepic.mdecore.config.sync.ConfigProcessor;
import com.mattdahepic.mdecore.config.sync.ConfigSyncable;
import net.minecraftforge.common.config.ConfigCategory;

import java.util.List;

public class MDEConfig extends ConfigSyncable {
    private static final String CATEGORY_TWEAKS = "tweaks";
    private static final String CATEGORY_DEBUG = "debug";

    @com.mattdahepic.mdecore.config.annot.Config
    @Comment({"If this value is true anonymous statistics about any MattDahEpic mods and their versions you are running will be sent to the author through Google Analytics."})
    @NoSync
    public static boolean reportUsageStats = true;

    @com.mattdahepic.mdecore.config.annot.Config
    @Comment({"Enable update checking for mods that implement MattDahEpic Core?"})
    @NoSync
    public static boolean updateCheckEnabled = true;

    @com.mattdahepic.mdecore.config.annot.Config(CATEGORY_TWEAKS)
    @Comment({"If true, water will wash away redstone.","True is vanilla behaviour."})
    @RestartReq(RestartReqs.REQUIRES_MC_RESTART)
    public static boolean waterBreaksRedstone = true;

    @com.mattdahepic.mdecore.config.annot.Config(CATEGORY_TWEAKS)
    @Comment({"If true, water bottles will fill cauldrons by 1 level.","False is vanilla behaviour."})
    @RestartReq(RestartReqs.REQUIRES_MC_RESTART)
    public static boolean waterBottlesFillCauldrons = true;

    @com.mattdahepic.mdecore.config.annot.Config(CATEGORY_TWEAKS)
    @Comment({"If true, you can sleep through the day to the next night.","False is vanilla behaviour."})
    @RestartReq(RestartReqs.REQUIRES_MC_RESTART)
    public static boolean sleepDuringDayChangesToNight = false;

    @com.mattdahepic.mdecore.config.annot.Config(CATEGORY_DEBUG)
    @Comment({"If true extra lines will be printed to the console about things that are happening."})
    @NoSync
    public static boolean debugLogging = false;

    private static ConfigSyncable INSTANCE;
    public static ConfigSyncable instance(String configName) {
        if (INSTANCE == null) {
            INSTANCE = new MDEConfig(configName);
        }
        return INSTANCE;
    }

    public static ConfigProcessor processor;

    protected MDEConfig(String configName) {
        super(configName);
    }
    @Override
    public void init() {
        addSection(CATEGORY_TWEAKS);
        addSection(CATEGORY_DEBUG);
        processor = new ConfigProcessor(getClass(), this.config, this.configFileName);
        processor.process(true);
    }
    @Override
    protected void reloadIngameConfigs() {

    }
    @Override
    protected void reloadNonIngameConfigs() {}
    @Override
    public String getConfigName() {
        return this.configFileName;
    }
    @Override
    public List<Section> getSections() {
        return this.getSections();
    }
    @Override
    public ConfigCategory getCategory(String name) {
        return this.getCategory(name);
    }
}
