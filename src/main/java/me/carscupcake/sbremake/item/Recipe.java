package me.carscupcake.sbremake.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.item.crafting.CraftingIngredient;
import me.carscupcake.sbremake.item.crafting.ShapedRecipe;
import me.carscupcake.sbremake.item.crafting.ShapelessRecipe;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;

public interface Recipe {
    SbItemStack getResult(@Nullable List<SbItemStack> items);

    boolean creatable(List<SbItemStack> items);

    Inventory recipePreview();

    Requirement[] requirements();

    default boolean canCraft(SkyblockPlayer player) {
        for (Requirement requirement : requirements())
            if (!requirement.canUse(player, null)) return false;
        return true;
    }

    void consume(List<SbItemStack> items);

    HashMap<String, CraftingIngredient> tags = new HashMap<>();
    LinkedList<Recipe> craftingRecipes = new LinkedList<>();

    static void init() {
        try {
            File folder = new File(Objects.requireNonNull(Main.class.getClassLoader().getResource("assets/tags/items")).getFile());
            Map<String, JsonArray> later = new HashMap<>();
            for (File f : Objects.requireNonNull(folder.listFiles())) {
                JsonArray array = JsonParser.parseReader(new FileReader(f)).getAsJsonObject().getAsJsonArray("values");
                ISbItem[] items = new ISbItem[array.size()];
                int i = 0;
                boolean doLater = false;
                for (JsonElement e : array) {
                    String member = e.getAsString();
                    if (member.startsWith("#")) {
                        doLater = true;
                        later.put(f.getName().split("\\.")[0], array);
                        break;
                    }
                    items[i] = Objects.requireNonNull(SbItemStack.from(member.split(":", 2)[1].toUpperCase())).sbItem();
                    i++;
                }
                if (doLater) continue;
                tags.put(f.getName().split("\\.")[0], new CraftingIngredient(1, items));
            }
            for (Map.Entry<String, JsonArray> entry : later.entrySet()) {
                JsonArray array = entry.getValue();
                List<ISbItem> items = new ArrayList<>();
                for (JsonElement e : array) {
                    String member = e.getAsString();
                    if (member.startsWith("#")) {
                        items.addAll(Arrays.asList(tags.get(member.split(":", 2)[1]).items()));
                        continue;
                    }
                    items.add(Objects.requireNonNull(SbItemStack.from(member.split(":", 2)[1].toUpperCase())).sbItem());
                }
                tags.put(entry.getKey(), new CraftingIngredient(1, items.toArray(new ISbItem[0])));
            }
            folder = new File(Objects.requireNonNull(Main.class.getClassLoader().getResource("assets/crafting")).getFile());
            for (File f : Objects.requireNonNull(folder.listFiles())) {
                JsonObject o = JsonParser.parseReader(new FileReader(f)).getAsJsonObject();
                String type = o.get("type").getAsString();
                for (RecipeType recipeType : RecipeType.values()) {
                    if (recipeType.id.equals(type)) {
                        craftingRecipes.add(recipeType.apply(o));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Main.LOGGER.error("An error occurred", e);
        }
    }

    enum RecipeType implements Function<JsonObject, Recipe> {
        CraftingShaped("minecraft:crafting_shaped") {
            @Override
            public Recipe apply(JsonObject jsonObject) {
                try {
                    JsonArray patter = jsonObject.getAsJsonArray("pattern");
                    String[] lines = new String[patter.size()];
                    int i = 0;
                    for (JsonElement p : patter) {
                        String s = p.getAsString();
                        lines[i] = s;
                        i++;
                    }
                    HashMap<Character, CraftingIngredient> ingredientHashMap = new HashMap<>();
                    for (Map.Entry<String, JsonElement> element : jsonObject.getAsJsonObject("key").asMap().entrySet()) {
                        char c = element.getKey().charAt(0);
                        if (element.getValue().isJsonArray()) {
                            JsonArray array = element.getValue().getAsJsonArray();
                            ISbItem[] items = new ISbItem[array.size()];
                            int index = 0;
                            for (JsonElement e : array) {
                                items[index++] = Objects.requireNonNull(SbItemStack.from(e.getAsJsonObject().get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem();
                            }
                            ingredientHashMap.put(c, new CraftingIngredient(1, items));
                        } else {
                            JsonObject o = element.getValue().getAsJsonObject();
                            if (o.has("tag")) {
                                ingredientHashMap.put(c, tags.get(o.get("tag").getAsString().split(":", 2)[1]));
                            } else {

                                ingredientHashMap.put(c, new CraftingIngredient(1, Objects.requireNonNull(SbItemStack.from(o.get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem()));
                            }


                        }
                    }
                    JsonObject resultObject = jsonObject.get("result").getAsJsonObject();
                    ISbItem result = Objects.requireNonNull(SbItemStack.from(resultObject.get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem();
                    int count = resultObject.has("count") ? resultObject.get("count").getAsInt() : 1;
                    return new ShapedRecipe(result, count, ingredientHashMap, lines);
                } catch (Exception e) {
                    System.out.println(jsonObject.toString());
                    throw new RuntimeException(e);
                }
            }
        },
        CraftingShapeless("minecraft:crafting_shapeless") {
            @Override
            public Recipe apply(JsonObject jsonObject) {
                try {
                    JsonArray jsonIngredients = jsonObject.get("ingredients").getAsJsonArray();
                    CraftingIngredient[] ingredients = new CraftingIngredient[jsonIngredients.size()];
                    int i = 0;
                    for (JsonElement e : jsonIngredients) {
                        if (e.isJsonArray()) {
                            JsonArray array = e.getAsJsonArray();
                            ISbItem[] items = new ISbItem[array.size()];
                            int index = 0;
                            for (JsonElement element : array) {
                                items[index++] = Objects.requireNonNull(SbItemStack.from(element.getAsJsonObject().get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem();
                            }
                            ingredients[i] = new CraftingIngredient(1, items);
                        } else {
                            JsonObject o = e.getAsJsonObject();
                            ingredients[i] = (o.has("tag")) ? tags.get(o.get("tag").getAsString().split(":", 2)[1]) : new CraftingIngredient(1, Objects.requireNonNull(SbItemStack.from(o.get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem());
                        }
                        i++;
                    }
                    JsonObject resultObject = jsonObject.get("result").getAsJsonObject();
                    ISbItem result = Objects.requireNonNull(SbItemStack.from(resultObject.get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem();
                    int count = resultObject.has("count") ? resultObject.get("count").getAsInt() : 1;
                    return new ShapelessRecipe(result, count, ingredients);
                } catch (Exception e) {
                    System.out.println(jsonObject.toString());
                    throw new RuntimeException(e);
                }
            }
        };
        private final String id;

        RecipeType(String id) {
            this.id = id;
        }
    }
}
