package com.mattdahepic.mdecore.config.v2.annot;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a {@code static} field as a config value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Config {
    /** The section that the config value should reside in. */
    String cat() default Configuration.CATEGORY_GENERAL;
    /** The comment for the config value. Multiple Strings will be split into separate lines. */
    String[] comment() default "";
    /** Does this config value take effect immediately, after world restart, or after game restart? */
    RestartReqs restartReq() default RestartReqs.NONE;
    /** What is the range for this config value? Can only be applied to number values and is applied forcefully. */
    Range range() default @Config.Range();
    /** Should the value by synced to the client when joining a server? */
    boolean sync() default true;

    @interface Range {
        double min() default Double.MIN_VALUE;
        double max() default Double.MAX_VALUE;
    }

    enum RestartReqs {
        /** No restart needed for this config to be applied. Default value. */
        NONE,
        /** This config requires the world to be restarted to take effect. */
        REQUIRES_WORLD_RESTART,
        /** This config requires the game to be restarted to take effect. {@code REQUIRES_WORLD_RESTART} is implied when using this. */
        REQUIRES_MC_RESTART;

        public Property apply(Property prop) {
            if (this == REQUIRES_MC_RESTART) {
                prop.setRequiresMcRestart(true);
            } else if (this == REQUIRES_WORLD_RESTART) {
                prop.setRequiresWorldRestart(true);
            }
            return prop;
        }
    }
    interface ConfigSubValue {}
}
