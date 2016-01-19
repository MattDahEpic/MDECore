package com.mattdahepic.mdecore.helpers;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class TranslationHelper {
    public static String getTranslatedString (String langKey) {
        return StatCollector.translateToLocal(langKey);
    }
    public static String getTranslatedStringFormatted (String langKey, Object... formatArgs) {
        return StatCollector.translateToLocalFormatted(langKey, formatArgs);
    }
    public static IChatComponent getTranslatedChat (String langKey) {
        return new ChatComponentText(getTranslatedString(langKey));
    }
    public static IChatComponent getTranslatedChatFormatted (String langKey, Object... formatArgs) {
        return new ChatComponentText(getTranslatedStringFormatted(langKey,formatArgs));
    }
}
