package com.mattdahepic.mdecore.helpers;

import net.minecraft.launchwrapper.Launch;

public class EnvironmentHelper {
    public static boolean isDeobf = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static boolean isServer;
}
