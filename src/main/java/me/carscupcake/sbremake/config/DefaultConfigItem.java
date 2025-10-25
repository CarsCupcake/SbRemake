package me.carscupcake.sbremake.config;

import com.google.gson.JsonObject;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.util.TypeUtil;
import net.minestom.server.coordinate.Point;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.util.*;

public interface DefaultConfigItem extends ConfigItem {
    default Map<String, Object> createConfigContext() {
        return Map.of();
    }

    static Class<?> getClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType parameterizedType) {
            return getClass(parameterizedType.getRawType());
        } else if (type instanceof GenericArrayType genericArrayType) {
            return java.lang.reflect.Array.newInstance(
                    getClass(genericArrayType.getGenericComponentType()), 0
            ).getClass();
        } else if (type instanceof TypeVariable<?> typeVariable) {
            return getClass(typeVariable.getBounds()[0]);
        } else if (type instanceof WildcardType wildcardType) {
            return getClass(wildcardType.getUpperBounds()[0]);
        }
        throw new IllegalArgumentException("Type not convertible to Class: " + type.getClass().getName());
    }

    private static String determineFieldName(Field field) {
        String fieldName = field.getName();
        if (field.isAnnotationPresent(ConfigField.class)) {
            ConfigField configField = field.getAnnotation(ConfigField.class);
            if (!configField.name().isEmpty()) {
                fieldName = configField.name();
            }
        } else if (field.isAnnotationPresent(ConfigParameter.class)) {
            ConfigParameter configParameter = field.getAnnotation(ConfigParameter.class);
            fieldName = configParameter.value();
        }
        return fieldName;
    }

    static boolean isValidConfigKey(Class<?> type) {
        return type.isEnum() || String.class == type || KeyClass.class.isAssignableFrom(type);
    }

    @SuppressWarnings("unchecked")
    static Object getMapKey(Class<?> p0, String obj, Method method) throws IllegalAccessException, InvocationTargetException {
        return (p0.isEnum()) ? Enum.valueOf((Class<? extends Enum>) p0, obj) : (method != null ? method.invoke(null, obj) : obj);
    }

    static String getMapKey(Class<?> p0, Object obj) {
        return p0.isEnum() ? ((Enum<?>) obj).name() : ((obj instanceof KeyClass keyClass) ? keyClass.key() : (String) obj);
    }

    static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    default void load(ConfigSection section) {
        var context = createConfigContext();
        for (var field : getAllFields(this.getClass())) {
            if (field.isAnnotationPresent(ConfigField.class)) {
                ConfigField configField = field.getAnnotation(ConfigField.class);
                if (configField.loadInPost()) continue;
                var fieldName = configField.name();
                if (fieldName.isEmpty()) {
                    fieldName = field.getName();
                }
                field.setAccessible(true);
                var type = field.getGenericType();
                try {
                    var value = evaluateField(section, field.getType(), type, fieldName, field.get(this));
                    if (value != null) {
                        field.set(this, value);
                    }
                } catch (Exception e) {
                    Main.LOGGER.error("Error loading config item {}", fieldName);
                    throw new RuntimeException(e);
                }
                field.setAccessible(false);
            }
            if (field.isAnnotationPresent(ConfigFileField.class)) {
                ConfigFileField configFileField = field.getAnnotation(ConfigFileField.class);
                var fileName = configFileField.name();
                if (fileName.isEmpty()) {
                    fileName = field.getName();
                }
                var file = createFile(fileName);
                field.setAccessible(true);
                try {
                    field.set(this, this.fromClassWithDefault(field.getType(), field.get(this), file));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                field.setAccessible(false);
            }
            if (field.isAnnotationPresent(ConfigContext.class)) {
                ConfigContext configContext = field.getAnnotation(ConfigContext.class);
                field.setAccessible(true);
                try {
                    if (!context.containsKey(configContext.value())) {
                        throw new RuntimeException("Context " + configContext.value() + " not found");
                    }
                    var c = context.get(configContext.value());
                    if (!field.getType().isAssignableFrom(c.getClass())) {
                        throw new RuntimeException("Context " + configContext.value() + " is not of type " + field.getType().getName());
                    }
                    field.set(this, c);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                field.setAccessible(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    default Object evaluateField(ConfigSection section, Class<?> clazz, @Nullable Type type, String fieldName, @Nullable Object defaultValue) {
        Object value = null;
        if (ConfigItem.class.isAssignableFrom(clazz)) {
            var sec = section.get(fieldName, ConfigSection.SECTION, new ConfigSection(new JsonObject()));
            value = fromClassWithDefault(clazz, defaultValue, sec);
        } else if (type instanceof ParameterizedType pType) {
            var rawType = (Class<?>) pType.getRawType();
            var typeArgs = pType.getActualTypeArguments();
            if (Collection.class.isAssignableFrom(rawType)) {
                var p0 = getClass(typeArgs[0]);
                if (ConfigItem.class.isAssignableFrom(p0)) {
                    try {
                        Collection<Object> collection = (Collection<Object>) (defaultValue != null ? defaultValue : clazz.getConstructor().newInstance());
                        var secs = section.get(fieldName, ConfigSection.SECTION_ARRAY, new ConfigSection[0]);
                        for (var sec : secs) {
                            collection.add(fromClass(p0, sec));
                        }
                        value = collection;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (Map.class.isAssignableFrom(rawType)) {
                typeArgs = TypeUtil.getMapGenericTypes(type);
                var p0 = getClass(typeArgs[0]);
                if (isValidConfigKey(p0)) {
                    try {
                        Method method = (KeyClass.class.isAssignableFrom(p0) ? (p0.getMethod("fromKey", String.class)) : null);
                        var objMap = (Map<Object, Object>) (defaultValue != null ? defaultValue : clazz.getConstructor().newInstance());
                        var valueType = getClass(typeArgs[1]);
                        if (ConfigItem.class.isAssignableFrom(getClass(typeArgs[1]))) {
                            var map = section.get(fieldName, ConfigSection.SECTIONS, null);
                            if (map != null) {
                                for (var obj : map.entrySet()) {
                                    var val = fromClass(valueType, obj.getValue());
                                    objMap.put(getMapKey(p0, obj.getKey(), method), val);
                                }
                                value = objMap;

                            }

                        } else {
                            var data = resolveDatatype(valueType, typeArgs[1]);
                            if (data != null) {
                                var sec = section.get(fieldName, ConfigSection.SECTION, new ConfigSection(new JsonObject()));
                                sec.forEntries((key, v) -> {
                                    try {
                                        objMap.put(getMapKey(p0, key, method), v.as(data));
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else if (ConfigItem[].class.isAssignableFrom(clazz)) {
            var secs = section.get(fieldName, ConfigSection.SECTION_ARRAY, new ConfigSection[0]);
            var objs = new Object[secs.length];
            for (int i = 0; i < secs.length; i++) {
                objs[i] = fromClass(clazz.getComponentType(), secs[i]);
            }
            value = objs;
        } else if (clazz.isEnum()) {
            var out = section.get(fieldName, ConfigSection.STRING, null);
            if (out != null) {
                value = Enum.valueOf((Class<? extends Enum>) clazz, out);
            }
        } else if (KeyClass.class.isAssignableFrom(clazz)) {
            try {
                var method = clazz.getMethod("fromKey", String.class);
                var out = section.get(fieldName, ConfigSection.STRING, null);
                if (out != null) {
                    value = method.invoke(null, out);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                var data = resolveDatatype(clazz, type);
                if (data == null) {
                    throw new RuntimeException("Cannot resolve datatype for field " + fieldName);
                }
                value = section.get(fieldName, data, null);
            } catch (Exception e) {
                Main.LOGGER.error("Could not resolve datatype for field {}", fieldName);
                throw e;
            }
        }
        return value;
    }

    default Object fromClass(Class<?> tClass, ConfigSection section) {
        for (var constructor : tClass.getConstructors()) {
            if (constructor.isAnnotationPresent(ConfigConstructor.class)) {
                return loadByConstructor(constructor, section);
            }
        }
        return loadByEmptyConstructor(tClass, section);
    }

    default Object fromClassWithDefault(Class<?> tClass, Object fieldValue, ConfigSection section) {
        try {
            if (fieldValue == null) {
                return fromClass(tClass, section);
            }
            var method = tClass.getMethod("load", ConfigSection.class);
            method.invoke(fieldValue, section);
            return fieldValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default Object loadByEmptyConstructor(Class<?> tClass, ConfigSection section) {
        try {
            var instance = tClass.getConstructor().newInstance();
            var method = tClass.getMethod("load", ConfigSection.class);
            method.invoke(instance, section);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T loadByConstructor(Constructor<T> constructor, ConfigSection section) {
        try {
            if (section.element.getAsJsonObject().isEmpty()) return null;
            var context = createConfigContext();
            Object[] args = new Object[constructor.getParameterCount()];
            int i = 0;
            for (var param : constructor.getParameters()) {

                if (param.isAnnotationPresent(ConfigContext.class)) {
                    ConfigContext configContext = param.getAnnotation(ConfigContext.class);
                    try {
                        if (!context.containsKey(configContext.value())) {
                            throw new RuntimeException("Context " + configContext.value() + " not found");
                        }
                        var c = context.get(configContext.value());
                        if (!param.getType().isAssignableFrom(c.getClass())) {
                            throw new RuntimeException("Context " + configContext.value() + " is not of type " + param.getType().getName());
                        }
                        args[i++] = c;
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }

                var ann = param.getAnnotation(ConfigParameter.class);
                var name = ann.value();
                args[i++] = evaluateField(section, param.getType(), param.getParameterizedType(), name, null);
            }
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default ConfigFile createFile(String name) {
        return new ConfigFile(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    default void save(ConfigSection section) {
        for (var field : getAllFields(this.getClass())) {
            if (field.isAnnotationPresent(ConfigField.class) || field.isAnnotationPresent(ConfigParameter.class)) {
                String fieldName = determineFieldName(field);
                field.setAccessible(true);
                var clazz = field.getType();
                var type = field.getGenericType();
                try {
                    var instance = field.get(this);
                    if (type instanceof ParameterizedType parameterizedType) {
                        var typeArgs = parameterizedType.getActualTypeArguments();
                        if (Collection.class.isAssignableFrom(clazz)) {
                            ParameterizedType tt;
                            while (typeArgs.length != 1) {
                                tt = (ParameterizedType) ((Class<?>) parameterizedType.getRawType()).getGenericSuperclass();
                                typeArgs = tt.getActualTypeArguments();
                            }
                            var p0 = getClass(typeArgs[0]);
                            if (ConfigItem.class.isAssignableFrom(p0)) {
                                Collection<?> collection = (Collection<?>) instance;
                                var secs = new ConfigSection[collection.size()];
                                var method = p0.getMethod("save", ConfigSection.class);
                                int i = 0;
                                for (var obj : collection) {
                                    var sec = new ConfigSection(new JsonObject());
                                    method.invoke(obj, sec);
                                    secs[i++] = sec;
                                }
                                section.set(fieldName, secs, ConfigSection.SECTION_ARRAY);
                                continue;
                            }
                        } else if (Map.class.isAssignableFrom(clazz)) {
                            typeArgs = TypeUtil.getMapGenericTypes(type);
                            var p0 = getClass(typeArgs[0]);
                            if (isValidConfigKey(p0)) {
                                if (ConfigItem.class.isAssignableFrom(getClass(typeArgs[1]))) {
                                    var map = new HashMap<String, ConfigSection>();
                                    var valueType = getClass(parameterizedType.getActualTypeArguments()[1]);
                                    var method = valueType.getMethod("save", ConfigSection.class);
                                    var objMap = (Map<?, ?>) instance;
                                    for (var obj : objMap.entrySet()) {
                                        var sec = new ConfigSection(new JsonObject());
                                        method.invoke(obj.getValue(), sec);
                                        map.put(getMapKey(p0, obj.getKey()), sec);
                                    }
                                    section.set(fieldName, map, ConfigSection.SECTIONS);
                                    continue;
                                }
                                var dataClass = resolveDatatype(getClass(typeArgs[1]), typeArgs[1]);
                                if (dataClass != null) {
                                    var configSection = new ConfigSection(new JsonObject());
                                    var objMap = (Map<?, ?>) instance;
                                    for (var obj : objMap.entrySet()) {
                                        configSection.unsafeSet(getMapKey(p0, obj.getKey()), obj.getValue(), (ConfigSection.Data<Object>) dataClass);
                                    }
                                    section.set(fieldName, configSection, ConfigSection.SECTION);
                                    continue;
                                }
                            }
                        }
                    }
                    if (instance == null) {
                        section.set(fieldName, null, ConfigSection.SECTION);
                        continue;
                    }
                    if (ConfigItem.class.isAssignableFrom(clazz)) {
                        var sec = section.get(fieldName, ConfigSection.SECTION, new ConfigSection(new JsonObject()));
                        var method = clazz.getMethod("save", ConfigSection.class);
                        method.invoke(instance, sec);
                        section.set(fieldName, sec, ConfigSection.SECTION);
                    } else if (ConfigItem[].class.isAssignableFrom(clazz)) {
                        var items = (ConfigItem[]) instance;
                        var objs = new ConfigSection[items.length];
                        var method = clazz.getComponentType().getMethod("save", ConfigSection.class);
                        for (int i = 0; i < items.length; i++) {
                            var sec = new ConfigSection(new JsonObject());
                            method.invoke(items[i], sec);
                            objs[i] = sec;
                        }
                        section.set(fieldName, objs, ConfigSection.SECTION_ARRAY);
                    } else if (clazz.isEnum()) {
                        var string = ((Enum<?>) instance).name();
                        section.set(fieldName, string, ConfigSection.STRING);
                    } else if (instance instanceof KeyClass keyClass) {
                        section.set(fieldName, keyClass.key(), ConfigSection.STRING);
                    } else {
                        var data = resolveDatatype(field.getType(), type);
                        if (data == null) {
                            throw new RuntimeException("Cannot resolve datatype for field " + fieldName + " with type " + field.getGenericType());
                        }
                        section.unsafeSet(fieldName, instance, (ConfigSection.Data<Object>) data);
                    }
                    field.setAccessible(false);
                } catch (Exception e) {
                    Main.LOGGER.error("An Error occurred with field {}", fieldName);
                    throw new RuntimeException(e);
                }
            }
            if (field.isAnnotationPresent(ConfigFileField.class)) {
                ConfigFileField configFileField = field.getAnnotation(ConfigFileField.class);
                var fileName = configFileField.name();
                if (fileName.isEmpty()) {
                    fileName = field.getName();
                }
                var file = createFile(fileName);
                field.setAccessible(true);
                try {
                    var i = field.get(this);
                    var method = field.getType().getMethod("save", ConfigSection.class);
                    method.invoke(i, file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                file.save();
                field.setAccessible(false);
            }
        }
    }

    default ConfigSection.Data<?> resolveDatatype(Class<?> clazz, Type type) {
        if (clazz == Integer.class || clazz == int.class)
            return ConfigSection.INTEGER;
        if (clazz == Boolean.class || clazz == boolean.class)
            return ConfigSection.BOOLEAN;
        if (clazz == String.class)
            return ConfigSection.STRING;
        if (clazz == Double.class || clazz == double.class)
            return ConfigSection.DOUBLE;
        if (clazz == Float.class || clazz == float.class)
            return ConfigSection.FLOAT;
        if (clazz == Long.class || clazz == long.class)
            return ConfigSection.LONG;
        if (clazz == Short.class || clazz == short.class)
            return ConfigSection.SHORT;
        if (clazz == Byte.class || clazz == byte.class)
            return ConfigSection.BYTE;
        if (clazz == Character.class || clazz == char.class)
            return ConfigSection.CHARACTER;
        if (clazz == String[].class)
            return ConfigSection.STRING_ARRAY;
        if (Point.class.isAssignableFrom(clazz))
            return ConfigSection.POSITION;
        if (SbItemStack.class == clazz)
            return ConfigSection.ITEM;
        if (SbItemStack[].class == clazz)
            return ConfigSection.ITEM_ARRAY;
        if (UUID.class == clazz)
            return ConfigSection.UUID;
        if (LocalDateTime.class == clazz)
            return ConfigSection.LOCAL_DATE_TIME;
        if (clazz == ConfigSection.class)
            return ConfigSection.SECTION;
        if (ConfigSection[].class == clazz) {
            return ConfigSection.SECTION_ARRAY;
        }
        if (type instanceof ParameterizedType parameterizedType) {
            if (List.class.isAssignableFrom(clazz)) {
                var param0 = parameterizedType.getActualTypeArguments()[0];
                if (param0 == String.class) {
                    return ConfigSection.STRING_LIST;
                }
            }
            if (Map.class.isAssignableFrom(clazz)) {
                var genericParams = TypeUtil.getMapGenericTypes(type);
                var type0 = genericParams[0];
                var type1 = genericParams[1];
                if (type0 == String.class && type1 == ConfigSection.class)
                    return ConfigSection.SECTIONS;

            }
        }
        return null;
    }
}
