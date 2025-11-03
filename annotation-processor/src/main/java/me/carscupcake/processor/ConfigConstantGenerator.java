package me.carscupcake.processor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palantir.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ConfigConstantGenerator {
    private JsonObject configFile;

    public void process(InputStream stream, Path outputDir) {
        try {
            try (var reader = new java.io.InputStreamReader(stream)) {
                configFile = JsonParser.parseReader(reader).getAsJsonObject();
            }

        } catch (Exception e) {
            System.err.println("Failed to read config file");
            e.printStackTrace(System.err);
        }
        if (configFile == null) return;
        if (configFile.entrySet().isEmpty()) {
            System.out.println("No config options found!");
            return;
        }
        var classBuilder = generateClass("Constants", configFile).toBuilder().addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                .addMember("value", "$S", "unused").build());
        var javaFile = JavaFile.builder("me.carscupcake.config", classBuilder.build()).build();
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
                var array = element.getAsJsonArray();
                if (array.isEmpty()) continue;
                var firstElement = array.get(0);
                if (firstElement.isJsonPrimitive()) {
                    if (firstElement.getAsJsonPrimitive().isString()) {
                        var strs = new String[array.size()];
                        for (int i = 0; i < array.size(); i++) {
                            strs[i] = array.get(i).getAsString();
                        }
                        var filedBuilder = FieldSpec.builder(String[].class, fieldName, modifiers)
                                .initializer("new String[]{" + "$S,".repeat(strs.length).substring(0, strs.length * 3 - 1) + "}", (Object[]) strs)
                                .addJavadoc("The value of the config option $L", elements.getKey());
                        classBuilder.addField(filedBuilder.build());
                    } else if (firstElement.getAsJsonPrimitive().isNumber()) {
                        Class<?> type;
                        if (array.get(0).getAsJsonPrimitive().getAsNumber() instanceof Integer) {
                            type = int[].class;
                        } else if (array.get(0).getAsJsonPrimitive().getAsNumber() instanceof Long) {
                            type = long[].class;
                        } else if (array.get(0).getAsJsonPrimitive().getAsNumber() instanceof Float) {
                            type = float[].class;
                        } else  {
                            type = double[].class;
                        }
                        var numbers = new Object[array.size()+1];
                        numbers[0] = type;
                        for (int i = 0; i < array.size(); i++) {
                            StringBuilder s = new StringBuilder(array.get(i).getAsNumber() + "");
                            if ((type == float[].class ||type == double[].class) && !s.toString().contains(".")) {
                                s.append(".");
                                if (type == float[].class) s.append("0f");
                            }
                            numbers[i+1] = s.toString();
                        }
                        var filedBuilder = FieldSpec.builder(type, fieldName, modifiers)
                                .initializer("new $T{" + "$L,".repeat(numbers.length).substring(0, numbers.length * 3 - 4) + "}", (Object[]) numbers)
                                .addJavadoc("The value of the config option $L", elements.getKey());
                        classBuilder.addField(filedBuilder.build());
                    }
                } else {
                    System.err.println("Only string arrays are supported yet");
                }
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
