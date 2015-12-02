package com.mattdahepic.mdecore.config.annot;


import net.minecraftforge.common.config.Configuration;

import java.lang.annotation.*;

/**
 * Used to mark a {@code static} field as a config option. Has no effect if the class is not processed with a {@link com.mattdahepic.mdecore.config.sync.ConfigSyncable}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Config {
    /**
     * The section of the config option (aka category).
     *
     * @return A string section name.
     */
    String value() default Configuration.CATEGORY_GENERAL;
}
