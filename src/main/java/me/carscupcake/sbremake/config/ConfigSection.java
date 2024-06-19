package me.carscupcake.sbremake.config;

import com.google.gson.JsonElement;
import org.junit.Assert;

import java.util.function.Consumer;

public class ConfigSection {
    protected final JsonElement element;
    public ConfigSection(JsonElement base) {
        element = base;
    }
    public <T> T get(String key, ConfigFile.Data<T> data) {
        return data.get(element, key);
    }

    public <T> void set(String key, T value, ConfigFile.Data<T> type) {

    }

    public JsonElement getRawElement() {
        return element;
    }

    public void forEach(Consumer<ConfigSection> elementConsumer) {
        Assert.assertTrue("Json is not an array", element.isJsonArray());
        element.getAsJsonArray().forEach(element1 -> {
            elementConsumer.accept(new ConfigSection(element1));
        });
    }
}
