package me.carscupcake.sbremake.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;

public class FileLoader {
    public static JsonElement load(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return new JsonObject();
        }
        try {
            return JsonParser.parseReader(new FileReader(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
