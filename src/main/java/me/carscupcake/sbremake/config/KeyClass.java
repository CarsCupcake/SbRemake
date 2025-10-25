package me.carscupcake.sbremake.config;

/**
 * Requires the implementing class to have a static fromKey method that takes a {@link String} and return the class
 */
public interface KeyClass {
    String key();
}
