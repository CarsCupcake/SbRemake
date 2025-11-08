package me.carscupcake.processor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palantir.javapoet.*;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.impl.shard.*;
import me.carscupcake.sbremake.util.Pair;
import net.minestom.server.item.Material;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ShardGenerator {
    private static final Pattern EXPR_PATTERN = Pattern.compile("\\$\\$(?<expression>.+)\\$\\$");
    private Map<String, String> mapName = new HashMap<>();
    public void process(InputStream stream, Path outputDir) {
        var root = JsonParser.parseReader(new java.io.InputStreamReader(stream)).getAsJsonObject();
        var shards = root.getAsJsonArray("attributes");
        var enumBuilder = TypeSpec.enumBuilder("Shard")
                .addJavadoc("The Attribute Shards")
                .addSuperinterface(IAttributeShard.class);
        var displayNameField = FieldSpec.builder(String.class, "displayName", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(displayNameField.build());
        var rarityField = FieldSpec.builder(ItemRarity.class, "rarity", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(rarityField.build());
        var idField = FieldSpec.builder(String.class, "id", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(idField.build());
        var shardIdField = FieldSpec.builder(String.class, "shardId", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(shardIdField.build());
        var shardCategoryField = FieldSpec.builder(ShardCategory.class, "category", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(shardCategoryField.build());
        var shardFamiliesField = FieldSpec.builder(ShardFamily[].class, "families", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(shardFamiliesField.build());
        var abilityNameField = FieldSpec.builder(String.class, "abilityName", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(abilityNameField.build());
        var loreField = FieldSpec.builder(Lore.class, "lore", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(loreField.build());
        var headValueField = FieldSpec.builder(String.class, "headValue", Modifier.PRIVATE, javax.lang.model.element.Modifier.FINAL);
        enumBuilder.addField(headValueField.build());
        var constructor = MethodSpec.constructorBuilder()
                .addParameter(String.class, "id")
                .addParameter(String.class, "displayName")
                .addParameter(ItemRarity.class, "rarity")
                .addParameter(ShardCategory.class, "category")
                .addParameter(ShardFamily[].class, "families")
                .addParameter(String.class, "shardId")
                .addParameter(String.class, "abilityName")
                .addParameter(String[].class, "loreRows")
                .addParameter(String.class, "headValue")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Function.class),
                        ClassName.get(Integer.class), ClassName.get(Double.class))), "placeholder")
                .addStatement("this.id = $L", "id")
                .addStatement("this.displayName = $L", "displayName")
                .addStatement("this.rarity = $L", "rarity")
                .addStatement("this.category = $L", "category")
                .addStatement("this.families = $L", "families")
                .addStatement("this.shardId = $L", "shardId")
                .addStatement("this.abilityName = $L", "abilityName")
                .addStatement("this.lore = new $T($T.of($L), $T.toPlaceholderMap(this, $L))", Lore.class, List.class, "loreRows", AttributeMenu.class, "placeholder")
                .addStatement("this.headValue = $L", "headValue")
                .build();
        enumBuilder.addMethod(constructor);
        var fusionElements = new LinkedList<JsonObject>();
        for (var element : shards) {
            var shard = element.getAsJsonObject();
            if (shard.has("fusion")) fusionElements.add(shard.getAsJsonObject("fusion"));
            var id = shard.get("id").getAsString();
            var displayName = shard.get("displayName").getAsString();
            var rarity = ItemRarity.valueOf(shard.get("rarity").getAsString());
            var category = ShardCategory.valueOf(shard.get("alignment").getAsString());
            var families = shard.getAsJsonArray("family");
            var familyArray = families == null ? new ShardFamily[0] : new ShardFamily[families.size()];
            var builder = new StringBuilder("$S, $S, $T.$L, $T.$L, ");
            if (familyArray.length > 0)
                builder.append("new $T[]{").append("$T.$L,".repeat(familyArray.length), 0, familyArray.length * 6 - 1).append("}, ");
            else builder.append("new $T[0], ");
            builder.append("$S, $S, new $T{");
            var loreRows = new ArrayList<String>();
            int loreExpId = 0;
            var expressions = new HashMap<String, String>();
            for (var element1 : shard.getAsJsonArray("lore")) {
                var lore = element1.getAsString();
                if (lore.contains("$")) {
                    var matcher = EXPR_PATTERN.matcher(lore);
                    while (matcher.find()) {
                        var expression = matcher.group("expression");
                        lore = lore.replaceFirst(EXPR_PATTERN.pattern(), "%" + loreExpId + "%");
                        if (expression.equals("l*1") || expression.equals("1*l")) expression = "l";
                        expressions.put("%" + loreExpId + "%", expression);
                        loreExpId++;
                    }
                }
                loreRows.add(lore);
            }
            StringBuilder mapLiteral;
            if (!expressions.isEmpty()) {
                mapLiteral = new StringBuilder("$T.of(");
                int i = 0;
                for (var entry : expressions.entrySet()) {
                    mapLiteral.append("\"").append(entry.getKey()).append("\", l -> (double)").append(entry.getValue()).append(")");
                    if (i != expressions.size() - 1) mapLiteral.append(", ");
                    i++;
                }
            } else {
                mapLiteral = new StringBuilder("$T.of()");
            }
            var lastEmptyIndex = loreRows.lastIndexOf("§7You can Syphon this shard from");
            if (lastEmptyIndex == -1)
                lastEmptyIndex = loreRows.lastIndexOf("§eRight-click to send to Hunting Box!");
            loreRows.subList(lastEmptyIndex - 1, loreRows.size()).clear();
            loreRows.removeFirst();
            var objects = familyArray.length == 0 ? new Object[12 + loreRows.size()] : new Object[12 + familyArray.length * 2 + loreRows.size()];
            objects[0] = id.replace("'", "");
            objects[1] = displayName.replace("'", "\\'");
            objects[2] = ItemRarity.class;
            objects[3] = rarity.name();
            objects[4] = ShardCategory.class;
            objects[5] = category.name();
            objects[6] = ShardFamily.class;
            for (int i = 0; i < familyArray.length; i++) {
                familyArray[i] = ShardFamily.valueOf(families.get(i).getAsString().replace(" ", ""));
                objects[7 + i * 2] = ShardFamily.class;
                objects[8 + i * 2] = familyArray[i].name();
            }
            var shardId = shard.get("shardId").getAsString();
            var abilityName = shard.get("abilityName").getAsString().replace("'", "\\'");
            objects[7 + familyArray.length * 2] = shardId;
            objects[8 + familyArray.length * 2] = abilityName;
            objects[9 + familyArray.length * 2] = String[].class;
            builder.append("$S,".repeat(loreRows.size()), 0, loreRows.size() * 3 - 1).append("}, $S");
            for (int i = 0; i < loreRows.size(); i++) {
                objects[10 + i + familyArray.length * 2] = loreRows.get(i);
            }
            var textures = shard.get("textures");
            var headValue = textures != null ? textures.getAsJsonObject().get("value").getAsString() : null;
            objects[10 + loreRows.size() + familyArray.length * 2] = headValue;
            objects[11 + loreRows.size() + familyArray.length * 2] = Map.class;
            builder.append(", ").append(mapLiteral);
            var enumType = TypeSpec.anonymousClassBuilder(builder.toString(), objects);
            if (headValue == null) {
                enumType.addMethod(MethodSpec.methodBuilder("getMaterial")
                        .returns(Material.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(Override.class)
                        .addStatement("return $T.$L", Material.class, shard.get("material").getAsString().toUpperCase())
                        .build());
            }
            var typeName = GeneratorUtils.spacedToUpperCamelCase(abilityName.replace("\\", "").replace("'", ""));
            mapName.put(id, typeName);
            enumBuilder.addEnumConstant(typeName, enumType.build());
        }
        enumBuilder.addMethod(MethodSpec.methodBuilder("getDisplayName")
                .returns(String.class)
                .addStatement("return this.displayName")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getRarity")
                .returns(ItemRarity.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.rarity")
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getShardId")
                .returns(String.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.shardId")
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getAbilityName")
                .returns(String.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.abilityName")
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getId")
                .returns(String.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.id")
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getCategory")
                .returns(ShardCategory.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.category")
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getFamilies")
                .returns(ShardFamily[].class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.families")
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getLore")
                .returns(Lore.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.lore")
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getHeadValue")
                .returns(String.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.headValue")
                .build());
        enumBuilder.addMethod(MethodSpec.methodBuilder("getMaterial")
                .returns(Material.class)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $T.$L", Material.class, "PLAYER_HEAD")
                .build());
        enumBuilder.addModifiers(Modifier.PUBLIC);
        var builder = new StringBuilder("$T.<$T<$T, $T>>of(");
        var list = new ArrayList<>();
        list.add(List.class);
        list.add(Pair.class);
        list.add(FusionIngredient.class);
        list.add(FusionIngredient.class);
        for (var obj : fusionElements) {
            builder.append("new $T<>(");
            list.add(Pair.class);
            var components = obj.getAsJsonArray("components");
            var first = components.get(0).getAsJsonObject();
            computeFusionIngredient(first, builder, list);
            builder.append(", ");
            var second = components.get(1).getAsJsonObject();
            computeFusionIngredient(second, builder, list);
            builder.append("),\n");
        }
        builder.deleteCharAt(builder.length() - 2).append(")");
        enumBuilder.addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class), ParameterizedTypeName.get(ClassName.get(Pair.class), ClassName.get(FusionIngredient.class), ClassName.get(FusionIngredient.class))),
                        "FUSION_RECIPES", Modifier.PUBLIC, javax.lang.model.element.Modifier.FINAL, javax.lang.model.element.Modifier.STATIC)
                        .initializer(builder.toString(), list.toArray())
                .build());
        var javaFile = JavaFile.builder("me.carscupcake.sbremake.item.impl.shard", enumBuilder.build()).build();
        try {
            javaFile.writeTo(outputDir);
            System.out.println("Generated Constants class");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void computeFusionIngredient(JsonObject obj, StringBuilder builder, List<Object> list) {
        builder.append("new $T($L, $T.of(");
        list.add(FusionIngredient.class);
        var amount = obj.get("amount").getAsInt();
        list.add(amount);
        list.add(List.class);
        var type = obj.get("type").getAsString();
        switch (type) {
            case "family" -> {
                builder.append("new $T(");
                list.add(FusionIngredient.FamilyConstrain.class);
                var values = obj.get("value");
                if (values.isJsonArray()) {
                    builder.append("$T.of(");
                    list.add(List.class);
                    var array = values.getAsJsonArray();
                    for (var element : array) {
                        builder.append("$T.$L,");
                        list.add(ShardFamily.class);
                        list.add(element.getAsString().replace(" ", ""));
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    builder.append("))");
                } else {
                    builder.append("$T.of($T.$L))");
                    list.add(List.class);
                    list.add(ShardFamily.class);
                    list.add(values.getAsString().replace(" ", ""));
                }
            }
            case "rarity" -> {
                builder.append("new $T(");
                list.add(FusionIngredient.RarityConstrain.class);
                var values = obj.get("value");
                if (values.isJsonArray()) {
                    builder.append("$T.of(");
                    list.add(List.class);
                    var array = values.getAsJsonArray();
                    for (var element : array) {
                        builder.append("$T.$L,");
                        list.add(ItemRarity.class);
                        list.add(element.getAsString());
                    }
                    builder.deleteCharAt(builder.length() - 1).append("))");
                } else {
                    builder.append("$T.of($T.$L))");
                    list.add(List.class);
                    list.add(ItemRarity.class);
                    list.add(values.getAsString());
                }
            }
            case "shard" -> {
                builder.append("new $T(");
                list.add(FusionIngredient.ShardConstrain.class);
                var values = obj.getAsJsonArray("value");
                builder.append("$T.of(");
                list.add(List.class);
                var array = values.getAsJsonArray();
                for (var element : array) {
                    builder.append("$L.$L,");
                    list.add("Shard");
                    list.add(mapName.get(element.getAsString()));
                }
                builder.deleteCharAt(builder.length() - 1).append("))");
            }
            case "alignment" -> {
                builder.append("new $T(");
                list.add(FusionIngredient.CategoryConstrain.class);
                var values = obj.get("value");
                if (values.isJsonArray()) {
                    builder.append("$T.of(");
                    list.add(List.class);
                    var array = values.getAsJsonArray();
                    for (var element : array) {
                        builder.append("$T.$L,");
                        list.add(ShardCategory.class);
                        list.add(element.getAsString());
                    }
                    builder.deleteCharAt(builder.length() - 1).append("))");
                } else {
                    builder.append("$T.of($T.$L))");
                    list.add(List.class);
                    list.add(ShardCategory.class);
                    list.add(values.getAsString());
                }
            }
        }
        if (obj.has("rarity")) {
            builder.append(", ");
            builder.append("new $T(");
            list.add(FusionIngredient.RarityConstrain.class);
            var values = obj.get("rarity");
            if (values.isJsonArray()) {
                builder.append("$T.of(");
                list.add(List.class);
                var array = values.getAsJsonArray();
                for (var element : array) {
                    builder.append("$T.$L,");
                    list.add(ItemRarity.class);
                    list.add(element.getAsString());
                }
                builder.deleteCharAt(builder.length() - 1).append("))");
            } else {
                builder.append("$T.of($T.$L))");
                list.add(List.class);
                list.add(ItemRarity.class);
                list.add(values.getAsString());
            }
        }
        builder.append("))");
    }
}
