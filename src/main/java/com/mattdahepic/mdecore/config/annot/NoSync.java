package com.mattdahepic.mdecore.config.annot;

import java.lang.annotation.*;

/**
 * If annotation exists, this field will not be synced to the client upon connection to a server. Has no effect if this field is not also annotated with {@link Config}
 *
 * This is useful for configs that have no need to be the same between client and server, or are purely client-sided.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface NoSync {}
