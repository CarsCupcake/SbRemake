package me.carscupcake.sbremake.util;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class ComponentUtil {
    private static final Pattern PATTERN = Pattern.compile("(?<style>§\\w)(?<content>[^§])*", Pattern.MULTILINE);
    public static TextComponent compile(String string) {
        var result = PATTERN.matcher(string);
        TextColor previousColor = null;
        Set<TextDecoration> previousStyle = new HashSet<>();
        TextComponent component = null;
        var lastIndex = 0;
        while (result.find()) {
            var index = result.start();
            if (index > lastIndex) {
                var newComponent = Component.text(string.substring(lastIndex, index), Style.style(previousColor, Set.copyOf(previousStyle)));
                if (component == null) {
                    component = newComponent;
                } else {
                    component = component.append(newComponent);
                }
            }
            lastIndex = index + 2;
            var style = result.group("style");
            var styleChar = style.charAt(1);
            if (styleChar == 'r') {
                previousColor = null;
                previousStyle.clear();
            } else {
                var decoration = getDecoration(styleChar);
                if (decoration != null) {
                    previousStyle.add(decoration);
                } else {
                    var color = getColor(styleChar);
                    if (color != null) {
                        previousColor = color;
                        previousStyle.clear();
                    }
                }
            }
        }
        if (lastIndex + 1 < string.length()) {
            String substring = string.substring(lastIndex);
            if (component == null) {
                component = Component.text(substring);
            } else {
                component = component.append(Component.text(substring, Style.style(previousColor, Set.copyOf(previousStyle))));
            }
        }
        return component == null ? Component.text("") : component;
    }

    private static @Nullable TextColor getColor(char character) {
        return switch (character) {
            case 'f' -> TextColor.color(255, 255, 255);
            case '0' -> TextColor.color(0, 0, 0);
            case '1' -> TextColor.color(0, 0, 170);
            case '2' -> TextColor.color(0, 170, 0);
            case '3' -> TextColor.color(0, 170, 170);
            case '4' -> TextColor.color(170, 0, 0);
            case '5' -> TextColor.color(170, 0, 170);
            case '6' -> TextColor.color(255, 177, 0);
            case '7' -> TextColor.color(170, 170, 170);
            case '8' -> TextColor.color(85, 85, 85);
            case '9' -> TextColor.color(85, 85, 255);
            case 'a' -> TextColor.color(85, 255, 85);
            case 'b' -> TextColor.color(85, 255, 255);
            case 'c' -> TextColor.color(255, 85, 85);
            case 'd' -> TextColor.color(255, 85, 255);
            case 'e' -> TextColor.color(255, 255, 85);
            case 'g' -> TextColor.color(221, 214, 5);
            case 'h' -> TextColor.color(227, 214, 209);
            case 'i' -> TextColor.color(206, 202, 202);
            case 'j' -> TextColor.color(0x443A3B);
            case 'm' -> TextColor.color(0x971607);
            case 'n' -> TextColor.color(0xB4684D);
            case 'p' -> TextColor.color(0xDEB12D);
            case 'q' -> TextColor.color(0x47A036);
            case 's' -> TextColor.color(0x2CBAA8);
            case 't' -> TextColor.color(0x21497B);
            case 'u' -> TextColor.color(0x9A5CC6);
            default -> null;
        };
    }

    @Contract(pure = true)
    private static @Nullable TextDecoration getDecoration(char character) {
        return switch (character) {
            case 'k' -> TextDecoration.OBFUSCATED;
            case 'l' -> TextDecoration.BOLD;
            case 'm' -> TextDecoration.STRIKETHROUGH;
            case 'n' -> TextDecoration.UNDERLINED;
            case 'o' -> TextDecoration.ITALIC;
            default -> null;
        };
    }

    private static final Gson GSON = new GsonBuilder().create();

    public static String toJsonFormat(TextComponent component) {
        if (component.children().isEmpty()) return GSON.toJson(toJsonFormatSingle(component));
        var array = new JsonArray();
        array.add(toJsonFormatSingle(component));
        for (var child : component.children()) {
            array.add(toJsonFormatSingle((TextComponent) child));
        }
        return GSON.toJson(array);
    }

    private static JsonObject toJsonFormatSingle(TextComponent component) {
        var obj = new JsonObject();
        obj.addProperty("content", component.content());
        if (component.color() != null)
            obj.addProperty("color", "#" + Integer.toHexString(Objects.requireNonNull(component.color()).value()));
        for (var decorationEntry : component.decorations().entrySet()) {
            if (decorationEntry.getValue() == TextDecoration.State.NOT_SET) continue;
            obj.addProperty(decorationEntry.getKey().name().toLowerCase(), decorationEntry.getValue() == TextDecoration.State.TRUE);
        }
        if (component.font() != null)
            obj.addProperty("font", Objects.requireNonNull(component.font()).toString());
        return obj;
    }
}
