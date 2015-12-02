package com.mattdahepic.mdecore.config.annot;

import java.lang.annotation.*;

/**
 * Contains the comment for a config option.
 * Has no effect if this field is not also annotated with {@link Config}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Comment {
    /**
     * The comment for the config option.
     * Multiple strings will be split into lines.
     */
    String[] value() default "";
}
