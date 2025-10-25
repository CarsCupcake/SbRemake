package me.carscupcake.sbremake.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fills in a variable or a paramter with a preset context by the key defined by {@link ConfigContext#value()}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface ConfigContext {
    /**
     *
     * @return the context key
     */
    String value();
}
