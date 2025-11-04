package me.carscupcake.processor;

import com.google.gson.JsonParser;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.impl.shard.IAttributeShard;
import me.carscupcake.sbremake.item.impl.shard.ShardCategory;
import me.carscupcake.sbremake.item.impl.shard.ShardFamily;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ShardGenerator {
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
        var constructor = MethodSpec.constructorBuilder()
                .addParameter(String.class, "id")
                .addParameter(String.class, "displayName")
                .addParameter(ItemRarity.class, "rarity")
                .addParameter(ShardCategory.class, "category")
                .addParameter(ShardFamily[].class, "families")
                .addParameter(String.class, "shardId")
                .addParameter(String.class, "abilityName")
                .addStatement("this.id = $L", "id")
                .addStatement("this.displayName = $L", "displayName")
                .addStatement("this.rarity = $L", "rarity")
                .addStatement("this.category = $L", "category")
                .addStatement("this.families = $L", "families")
                .addStatement("this.shardId = $L", "shardId")
                .addStatement("this.abilityName = $L", "abilityName")
                .build();
        enumBuilder.addMethod(constructor);
        for (var element : shards) {
            var shard = element.getAsJsonObject();
            var id = shard.get("id").getAsString();
            var displayName = shard.get("displayName").getAsString();
            var rarity = ItemRarity.valueOf(shard.get("rarity").getAsString());
            var category = ShardCategory.valueOf(shard.get("alignment").getAsString());
            var families = shard.getAsJsonArray("family");
            var familyArray = families == null ? new ShardFamily[0] : new ShardFamily[families.size()];
            var builder = new StringBuilder("$S, $S, $T.$L, $T.$L, ");
            if (familyArray.length > 0) builder.append("new $T[]{").append("$T.$L,".repeat(familyArray.length).substring(0, familyArray.length * 6 - 1)).append("}, ");
            else builder.append("new $T[0], ");
            builder.append("$S, $S");
            var objects = familyArray.length == 0 ? new Object[9] : new Object[9 + familyArray.length * 2];
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
            enumBuilder.addEnumConstant(GeneratorUtils.spacedToUpperCamelCase(abilityName.replace("\\", "").replace("'", "")), TypeSpec.anonymousClassBuilder(builder.toString(), objects).build());
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
        enumBuilder.addModifiers(Modifier.PUBLIC);
        var javaFile = JavaFile.builder("me.carscupcake.sbremake.item.impl.shard", enumBuilder.build()).build();
        try {
            javaFile.writeTo(outputDir);
            System.out.println("Generated Constants class");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
