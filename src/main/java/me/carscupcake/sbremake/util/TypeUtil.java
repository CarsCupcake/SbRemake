package me.carscupcake.sbremake.util;
import java.lang.reflect.*;
import java.util.*;

public class TypeUtil {
    public static Type[] getMapGenericTypes(Type type) {
        return resolveMapGenerics(type, new HashMap<>());
    }

    private static Type[] resolveMapGenerics(Type type, Map<TypeVariable<?>, Type> typeVarMap) {
        if (type instanceof ParameterizedType pt) {
            Class<?> rawClass = (Class<?>) pt.getRawType();
            TypeVariable<?>[] vars = rawClass.getTypeParameters();
            Type[] args = pt.getActualTypeArguments();
            for (int i = 0; i < vars.length; i++) {
                typeVarMap.put(vars[i], args[i]);
            }
            if (Map.class.equals(rawClass)) {
                Type keyType = resolveType(pt.getActualTypeArguments()[0], typeVarMap);
                Type valueType = resolveType(pt.getActualTypeArguments()[1], typeVarMap);
                return new Type[]{ keyType, valueType };
            }
            Type[] result = trySuperTypes(rawClass, typeVarMap);
            if (result.length == 2) return result;
        } else if (type instanceof Class<?> clazz) {
            if (Map.class.equals(clazz)) {
                TypeVariable<?>[] vars = clazz.getTypeParameters();
                if (vars.length == 2)
                    return new Type[]{ resolveType(vars[0], typeVarMap), resolveType(vars[1], typeVarMap) };
            }
            Type[] result = trySuperTypes(clazz, typeVarMap);
            if (result.length == 2) return result;
        }
        return new Type[0];
    }

    private static Type[] trySuperTypes(Class<?> clazz, Map<TypeVariable<?>, Type> typeVarMap) {
        Type superType = clazz.getGenericSuperclass();
        if (superType != null) {
            Type[] resolved = resolveMapGenerics(superType, new HashMap<>(typeVarMap));
            if (resolved.length == 2) return resolved;
        }
        for (Type gi : clazz.getGenericInterfaces()) {
            Type[] resolved = resolveMapGenerics(gi, new HashMap<>(typeVarMap));
            if (resolved.length == 2) return resolved;
        }
        return new Type[0];
    }

    private static Type resolveType(Type type, Map<TypeVariable<?>, Type> typeVarMap) {
        if (type instanceof TypeVariable<?> tv) {
            Type resolved = typeVarMap.get(tv);
            return (resolved != null && !resolved.equals(tv))
                    ? resolveType(resolved, typeVarMap)
                    : tv;
        }
        return type;
    }
}
