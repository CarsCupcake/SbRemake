package me.carscupcake.sbremake.config;

/**
 * This interface guarantees that the implementing class
 * has an empty constructor
 * If the field is annotated {@link ConfigField} then it will be saved in the players default field.
 * If the field is annotated {@link ConfigFileField} then it will be saved in a separate file.
 */
public interface ConfigItem {
    void save(ConfigSection section);
    void load(ConfigSection section);
}
