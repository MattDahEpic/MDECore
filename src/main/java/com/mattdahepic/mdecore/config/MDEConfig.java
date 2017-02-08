package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.sync.Config;
import com.mattdahepic.mdecore.config.sync.ConfigSyncable;

public class MDEConfig extends ConfigSyncable {
    public String getConfigVersion () {return "1";}
    public String getConfigName () {return MDECore.MODID;}
    public Class getConfigClass () {return getClass();}
    private static final String CAT_TWEAKS = "tweaks";
    private static final String CAT_COMMANDS = "commands";

    @Config(comment = {"A message to be sent to the player every time they join the server.","Supports color codes as specified at http://minecraft.gamepedia.com/Formatting_codes#Color_codes","If this string is empty no message will be sent."}) public static String loginMessage = "";

    @Config(cat = CAT_TWEAKS, comment = {"If true, water will wash away redstone.","True is vanilla behaviour."}, restartReq = Config.RestartReqs.REQUIRES_MC_RESTART) public static boolean waterBreaksRedstone = true;
    @Config(cat = CAT_TWEAKS, comment = {"If true, water bottles will fill cauldrons by 1 level.","False is vanilla behaviour."}) public static boolean waterBottlesFillCauldrons = true;
    @Config(cat = CAT_TWEAKS, comment = {"If true, you can sleep through the day to the next night.","False is vanilla behaviour."}) public static boolean sleepDuringDayChangesToNight = false;
    @Config(cat = CAT_TWEAKS, comment = {"What the ender dragon drops when killed.","Default is elytra."}) public static String dragonDrop = "minecraft:elytra@0";
    @Config(cat = CAT_TWEAKS, comment = {"Should players get a message and sometimes an item on holidays?"}) public static boolean holidayRewards = true;
    
    @Config(cat = CAT_COMMANDS, comment = {"Whould the /mde tpa command allow cross dimensional teleporting?","Default is true."}) public static boolean tpaCrossDimension = true;
    @Config(cat = CAT_COMMANDS, comment = {"How long should players have to stand still before being TPA'd?","Default is 3"}, range = @Config.Range(min = 0, max = 20)) public static int tpaWaitTime = 3;
    
    @Config(cat = CAT_COMMANDS,comment = {"Below how many ticks pre second should the pregenerator wait before generating more chunks?","0 to disable, 5 is default."},range = @Config.Range(min = 0, max = 19),sync = false) public static double pregenMinTPS = 5D;
}
