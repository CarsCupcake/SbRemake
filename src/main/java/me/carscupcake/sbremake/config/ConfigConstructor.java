package me.carscupcake.sbremake.config;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In case of classes that requires a construct with the data like records you can annotate the constructor with this annotation.
 * You have to annotate every parameter with {@link ConfigParameter}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR})
public @interface ConfigConstructor {
}
