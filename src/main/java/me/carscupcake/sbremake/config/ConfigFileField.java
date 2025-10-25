package me.carscupcake.sbremake.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requires the type of the field to implement {@link ConfigItem}.
 * Saves the class in a new config file.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ConfigFileField {
    String name();
}
