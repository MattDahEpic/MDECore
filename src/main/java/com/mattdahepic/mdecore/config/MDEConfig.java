package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.config.sync.Config;
import com.mattdahepic.mdecore.config.sync.ConfigSyncable;

public class MDEConfig extends ConfigSyncable {
    public String getConfigVersion () {return "1";}
    public MDEConfig (String configName) {super(configName);}
    private static final String CAT_TWEAKS = "tweaks";

    //@Config(comment = {"If this value is true anonymous statistics about any MattDahEpic mods and their versions you are running will be sent to the author through Google Analytics."}, sync = false) public static boolean reportUsageStats = true;
    @Config(comment = {"A message to be sent to the player every time they join the server.","Supports color codes as specified at http://minecraft.gamepedia.com/Formatting_codes#Color_codes","If this string is empty no message will be sent."}) public static String loginMessage = "";

    @Config(cat = CAT_TWEAKS, comment = {"If true, water will wash away redstone.","True is vanilla behaviour."}, restartReq = Config.RestartReqs.REQUIRES_MC_RESTART) public static boolean waterBreaksRedstone = true;
    @Config(cat = CAT_TWEAKS, comment = {"If true, water bottles will fill cauldrons by 1 level.","False is vanilla behaviour."}) public static boolean waterBottlesFillCauldrons = true;
    @Config(cat = CAT_TWEAKS, comment = {"If true, you can sleep through the day to the next night.","False is vanilla behaviour."}) public static boolean sleepDuringDayChangesToNight = false;
    @Config(cat = CAT_TWEAKS, comment = {"If true, any time the ender dragon is killed, it will drop an elytra."}) public static boolean dragonDropsElytra = true;
    @Config(cat = CAT_TWEAKS, comment = {"Should players get a message and sometimes an item on holidays?"}) public static boolean holidayRewards = true;
}
