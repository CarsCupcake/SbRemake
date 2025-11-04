package me.carscupcake.processor;

public class GeneratorUtils {
    public static String spacedToUpperCamelCase(String name) {
        var split = name.split(" ");
        var sb = new StringBuilder();
        for (var s : split) {
            sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
        }
        return sb.toString();
    }
}
