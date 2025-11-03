package me.carscupcake.processor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigConstantGenerator {
    private static final String CONFIG_FILE = "constants.json";
    private JsonObject configFile;

    public void process(Path outputDir) {
        var path = "src/main/resources/" + CONFIG_FILE;
        try {
            var file = new File(path);
            if (!file.exists()) {
                System.out.println("Config file not found: " + path);
                return;
            }
            try (var reader = new java.io.FileReader(file)) {
                configFile = JsonParser.parseReader(reader).getAsJsonObject();
            }

        } catch (Exception e) {
            System.err.println("Failed to read config file: " + path);
            e.printStackTrace(System.err);
        }
        if (configFile == null) return;
        if (configFile.entrySet().isEmpty()) {
            System.out.println("No config options found!");
            return;
        }
        var classBuilder = generateClass("Constants", configFile);
        var javaFile = JavaFile.builder("me.carscupcake.config", classBuilder).build();
        try {
            javaFile.writeTo(outputDir);
            System.out.println("Generated Constants class");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private TypeSpec generateClass(String name, JsonObject section) {
        var modifiers = new javax.lang.model.element.Modifier[]{javax.lang.model.element.Modifier.PUBLIC, javax.lang.model.element.Modifier.STATIC, javax.lang.model.element.Modifier.FINAL};
        var classBuilder = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
        for (var elements : section.entrySet()) {
            var fieldName = elements.getKey().toUpperCase().replace("-", "_");
            var element = elements.getValue();
            if (element.isJsonObject()) {
                var subclass = generateClass(fieldName, element.getAsJsonObject());
                classBuilder.addType(subclass.toBuilder().addModifiers(Modifier.STATIC).build());
            } else if (element.isJsonPrimitive()) {
                var primitive = element.getAsJsonPrimitive();
                if (primitive.isString()) {
                    var filedBuilder = FieldSpec.builder(String.class, fieldName, modifiers)
                            .initializer("$S", primitive.getAsString())
                            .addJavadoc("The value of the config option $L", elements.getKey());
                    classBuilder.addField(filedBuilder.build());
                } else if (primitive.isNumber()) {
                    var number = primitive.getAsNumber();
                    var filedBuilder = FieldSpec.builder(number.getClass(), fieldName, modifiers)
                            .initializer("$L", number)
                            .addJavadoc("The value of the config option $L", elements.getKey());
                    classBuilder.addField(filedBuilder.build());
                } else if (primitive.isBoolean()) {
                    var filedBuilder = FieldSpec.builder(TypeName.BOOLEAN, fieldName, modifiers)
                            .initializer("$L", primitive.getAsBoolean())
                            .addJavadoc("The value of the config option $L", elements.getKey());
                    classBuilder.addField(filedBuilder.build());
                }
            } else if (element.isJsonArray()) {
                System.err.println("Arrays are not supported yet");
                var filedBuilder = FieldSpec.builder(String[].class, fieldName, modifiers)
                        .initializer("$L", "null")
                        .addJavadoc("The value of the config option $L - No Support in version 0.0.11", elements.getKey());
                classBuilder.addField(filedBuilder.build());
            } else if (element.isJsonNull()) {
                var filedBuilder = FieldSpec.builder(String.class, fieldName, modifiers)
                        .initializer("$L", "null")
                        .addJavadoc("The value of the config option $L", elements.getKey());
                classBuilder.addField(filedBuilder.build());
            }
        }
        return classBuilder.build();
    }
}
