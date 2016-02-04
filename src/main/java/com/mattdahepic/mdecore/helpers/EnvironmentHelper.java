package com.mattdahepic.mdecore.helpers;

import com.mattdahepic.mdecore.MDECore;

import net.minecraft.launchwrapper.Launch;

public class EnvironmentHelper {
    public static boolean isDeobf = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static boolean isServer;

    public static void printEnvironmentToLog () {
        MDECore.logger.info(String.format("Running on java %s. System: %s-bit %s version %s",System.getProperty("java.version"),System.getProperty("os.arch"), System.getProperty("os.name"),System.getProperty("os.name")));
    }
}
