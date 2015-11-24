package com.mattdahepic.mdecore.helpers;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class LogHelper {
    public static void debug (String modid,String message) {
        FMLLog.log(modid,Level.DEBUG,message);
    }
    public static void info (String modid,String message) {
        FMLLog.log(modid,Level.INFO,message);
    }
    public static void warn (String modid,String message) {
        FMLLog.log(modid,Level.WARN,message);
    }
    public static void error (String modid,String message) {
        FMLLog.log(modid,Level.ERROR,message);
    }
    public static void trace (String modid,Exception message) {
        FMLLog.log(modid,Level.ERROR,message.toString());
    }
}
