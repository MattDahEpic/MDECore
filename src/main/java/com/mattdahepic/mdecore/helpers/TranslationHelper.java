package com.mattdahepic.mdecore.helpers;

import net.minecraft.util.StatCollector;

public class TranslationHelper {
    public static String getTranslatedString (String langKey) {
        return StatCollector.translateToLocal(langKey);
    }
    public static String getTranslatedStringFormatted (String langKey, Object... formatArgs) {
        return StatCollector.translateToLocalFormatted(langKey, formatArgs);
    }
}
