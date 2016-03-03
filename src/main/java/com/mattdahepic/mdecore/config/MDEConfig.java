package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.config.annot.Comment;
import com.mattdahepic.mdecore.config.annot.Config;
import com.mattdahepic.mdecore.config.annot.NoSync;
import com.mattdahepic.mdecore.config.annot.RestartReq;
import com.mattdahepic.mdecore.config.sync.ConfigProcessor;
import com.mattdahepic.mdecore.config.sync.ConfigSyncable;

public class MDEConfig extends ConfigSyncable {
    private static final String CATEGORY_TWEAKS = "tweaks";

    @Config @Comment({"If this value is true anonymous statistics about any MattDahEpic mods and their versions you are running will be sent to the author through Google Analytics."}) @NoSync public static boolean reportUsageStats = true;
    @Config @Comment({"A message to be sent to the player every time they join the server.","Supports color codes as specified at http://minecraft.gamepedia.com/Formatting_codes#Color_codes","If this string is empty no message will be sent."}) public static String loginMessage = "";

    @Config(CATEGORY_TWEAKS) @Comment({"If true, water will wash away redstone.","True is vanilla behaviour."}) @RestartReq(RestartReqs.REQUIRES_MC_RESTART) public static boolean waterBreaksRedstone = true;
    @Config(CATEGORY_TWEAKS) @Comment({"If true, water bottles will fill cauldrons by 1 level.","False is vanilla behaviour."}) public static boolean waterBottlesFillCauldrons = true;
    @Config(CATEGORY_TWEAKS) @Comment({"If true, you can sleep through the day to the next night.","False is vanilla behaviour."}) public static boolean sleepDuringDayChangesToNight = false;
    @Config(CATEGORY_TWEAKS) @Comment({"If true, any time the ender dragon is killed, it will drop an elytra."}) public static boolean dragonDropsElytra = true;

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
        processor = new ConfigProcessor(getClass(), this.config, this.configFileName);
        processor.process(true);
    }
    @Override
    protected void reloadIngameConfigs() {}
    @Override
    protected void reloadNonIngameConfigs() {}
    @Override
    public String getConfigName() {
        return this.configFileName;
    }
}
