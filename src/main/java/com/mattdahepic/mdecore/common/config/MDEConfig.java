package com.mattdahepic.mdecore.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public final class MDEConfig { //thanks to https://github.com/Vazkii/Botania/blob/0aa02597f4255456732ec77f702785fefa208d6d/src/main/java/vazkii/botania/common/core/handler/ConfigHandler.java
    public static class Common {
        public final ForgeConfigSpec.ConfigValue<String> loginMessage;

        public Common(ForgeConfigSpec.Builder builder) {
            loginMessage = builder
                    .comment("A message to be sent to the player every time they join the server.","Supports color codes as specified at http://minecraft.gamepedia.com/Formatting_codes#Color_codes","If this string is empty no message will be sent.")
                    .define("loginMessage","");

        }
    }
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<Common,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
