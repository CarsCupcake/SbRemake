package me.carscupcake.sbremake.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requires the type of the field to implement {@link ConfigItem} or have an implementation of {@link ConfigSection.Data}.
 * Saves the class in the same file as the players 'default' config.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ConfigField {
    String name() default "";
    boolean loadInPost() default false;
}
