package com.mattdahepic.mdecore.config.annot;

import java.lang.annotation.*;

/**
 * Represents the range a config value can be.
 * This is applied forcefully, any value past one of the extremes will be clampecd inside this range.
 * Has no effect if this field is not also annotated with {@link Config}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Range {
    /**
     * The min value of the config.
     * For non-numeric values, or if there is no min, this should remain unset.
     *
     * @return A double minimum valeu for the config.
     */
    double min() default Double.MIN_VALUE;

    /**
     * The max value of the config.
     * For non-numeric values, or if there is no max, this should remain unset.
     *
     * @return A double maximum value for the config.
     */
    double max() default Double.MAX_VALUE;
}
