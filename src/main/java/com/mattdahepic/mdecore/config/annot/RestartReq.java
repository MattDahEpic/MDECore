package com.mattdahepic.mdecore.config.annot;


import com.mattdahepic.mdecore.config.sync.ConfigSyncable;

import java.lang.annotation.*;

/**
 * Represents the restart requirements of a config value.
 * Use this if your config will have no effect if changed while the game is running.
 * Has no effect if this field is not also annotated with {@link Config}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented

public @interface RestartReq {
    /**
     * What requirements this config has for restarting the game.
     *
     * @see ConfigSyncable.RestartReqs#NONE
     * @see ConfigSyncable.RestartReqs#REQUIRES_WORLD_RESTART
     * @see ConfigSyncable.RestartReqs#REQUIRES_MC_RESTART
     */
    ConfigSyncable.RestartReqs value() default ConfigSyncable.RestartReqs.NONE;
}
