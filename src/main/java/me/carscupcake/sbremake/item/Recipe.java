package me.carscupcake.sbremake.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.item.crafting.CraftingIngredient;
import me.carscupcake.sbremake.item.crafting.ShapedRecipe;
import me.carscupcake.sbremake.item.crafting.ShapelessRecipe;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedDiamondBlock;
import me.carscupcake.sbremake.item.requirements.CollectionRequirement;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.item.Gui;
import me.carscupcake.sbremake.util.item.InventoryBuilder;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

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
    LinkedHashMap<String, Recipe> craftingRecipes = new LinkedHashMap<>();
    Map<String, JsonArray> later = new HashMap<>();

    static void loadTags(String path) throws URISyntaxException, IOException {
        forFilesInResourceFolder(path, (name, f) -> {
            JsonArray array;
            try {
                array = JsonParser.parseReader(new InputStreamReader(f)).getAsJsonObject().getAsJsonArray("values");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            ISbItem[] items = new ISbItem[array.size()];
            int i = 0;
            boolean doLater = false;
            for (JsonElement e : array) {
                String member = e.getAsString();
                if (member.startsWith("#")) {
                    doLater = true;
                    later.put(name.split("\\.")[0], array);
                    break;
                }
                items[i] = Objects.requireNonNull(SbItemStack.from(member.split(":", 2)[1].toUpperCase())).sbItem();
                i++;
            }
            if (doLater) return;
            tags.put(name.split("\\.")[0], new CraftingIngredient(1, items));
        });
    }

    static void loadRecipes(String path) throws URISyntaxException, IOException {
        forFilesInResourceFolder(path, (name, f) ->  {
            JsonObject o;
            try {
                o = JsonParser.parseReader(new InputStreamReader(f)).getAsJsonObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String type = o.get("type").getAsString();
            for (RecipeType recipeType : RecipeType.values()) {
                if (recipeType.id.equals(type)) {
                    craftingRecipes.put(name.split("\\.")[0], recipeType.apply(o));
                    break;
                }
            }
        });
    }

    static void forFilesInResourceFolder(String inResourcesPath, BiConsumer<String, InputStream> fileConsumer) throws URISyntaxException, IOException {
        URI uri = Objects.requireNonNull(Main.class.getClassLoader().getResource(inResourcesPath)).toURI();
        try( FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap() ) ){
            Path folderRootPath = fileSystem.getPath(inResourcesPath);
            Stream<Path> walk = Files.walk(folderRootPath, 1);
            walk.forEach(childFileOrFolder -> {
                try {
                    fileConsumer.accept(childFileOrFolder.getFileName().toString(), Files.newInputStream(childFileOrFolder));
                } catch (FileSystemException e) {
                    if (e.getMessage().endsWith("is a directory")) return;
                    throw new RuntimeException(e);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void init() {
        try {
            loadTags("assets/tags/items/");
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
            loadRecipes("assets/crafting/native");
            loadRecipes("assets/crafting/custom");
        } catch (Exception e) {
            Main.LOGGER.error("An error occurred", e);
        }
        Command command = new Command("craft");
        command.setDefaultExecutor((commandSender, _) -> openCraftingGui((SkyblockPlayer) commandSender));
        MinecraftServer.getCommandManager().register(command);
        CraftingIngredient ENCHANTED_DIAMOND_BLOCK = new CraftingIngredient(1, ISbItem.get(EnchantedDiamondBlock.class));
        for (int i = 2; i < 13; i++) {
            String[] recipeShape = {" # ", "#!#", " # "};
            String id = STR."perfect_boots_\{i}";
            craftingRecipes.put(id, new ShapedRecipe(SbItemStack.raw(id.toUpperCase()), 1, 4, Map.of('#', ENCHANTED_DIAMOND_BLOCK, '!', new CraftingIngredient(1, SbItemStack.raw(STR."PERFECT_BOOTS_\{i - 1}"))), recipeShape));
            id = STR."perfect_leggings_\{i}";
            craftingRecipes.put(id, new ShapedRecipe(SbItemStack.raw(id.toUpperCase()), 1, 4, Map.of('#', ENCHANTED_DIAMOND_BLOCK, '!', new CraftingIngredient(1, SbItemStack.raw(STR."PERFECT_LEGGINGS_\{i - 1}"))), recipeShape));
            id = STR."perfect_chestplate_\{i}";
            craftingRecipes.put(id, new ShapedRecipe(SbItemStack.raw(id.toUpperCase()), 1, 4, Map.of('#', ENCHANTED_DIAMOND_BLOCK, '!', new CraftingIngredient(1, SbItemStack.raw(STR."PERFECT_CHESTPLATE_\{i - 1}"))), recipeShape));
            id = STR."perfect_helmet_\{i}";
            craftingRecipes.put(id, new ShapedRecipe(SbItemStack.raw(id.toUpperCase()), 1, 4, Map.of('#', ENCHANTED_DIAMOND_BLOCK, '!', new CraftingIngredient(1, SbItemStack.raw(STR."PERFECT_HELMET_\{i - 1}"))), recipeShape));
        }
    }

    private static File fileFromResource(String path) throws IOException, URISyntaxException {
        /*InputStream s = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        File f = new File("./tempLoad/");
        if (!f.exists()) f.mkdirs();
        assert s != null;
        Files.copy(s, f.toPath());*/
        return new File(Objects.requireNonNull(Main.class.getResource(path)).toURI());
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
                                if (!o.has("count"))
                                    ingredientHashMap.put(c, tags.get(o.get("tag").getAsString().split(":", 2)[1]));
                                else {
                                    ingredientHashMap.put(c, new CraftingIngredient(o.get("count").getAsInt(), tags.get(o.get("tag").getAsString().split(":", 2)[1]).items()));
                                }
                            } else {

                                ingredientHashMap.put(c, new CraftingIngredient(o.has("count") ? o.get("count").getAsInt() : 1, Objects.requireNonNull(SbItemStack.from(o.get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem()));
                            }


                        }
                    }
                    Requirement[] requirements = readRequirements(jsonObject);
                    JsonObject resultObject = jsonObject.get("result").getAsJsonObject();
                    ISbItem result = Objects.requireNonNull(SbItemStack.from(resultObject.get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem();
                    int count = resultObject.has("count") ? resultObject.get("count").getAsInt() : 1;
                    return new ShapedRecipe(result, count, resultObject.has("priority") ? resultObject.get("priority").getAsInt() : -1, requirements, ingredientHashMap, lines);
                } catch (Exception e) {
                    System.out.println(jsonObject.toString());
                    throw new RuntimeException(e);
                }
            }
        }, CraftingShapeless("minecraft:crafting_shapeless") {
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
                            ingredients[i] = (o.has("tag")) ? tags.get(o.get("tag").getAsString().split(":", 2)[1]) : new CraftingIngredient((o.has("count") ? o.get("count").getAsInt() : 1), Objects.requireNonNull(SbItemStack.from(o.get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem());
                        }
                        i++;
                    }
                    Requirement[] requirements = readRequirements(jsonObject);
                    JsonObject resultObject = jsonObject.get("result").getAsJsonObject();
                    ISbItem result = Objects.requireNonNull(SbItemStack.from(resultObject.get("item").getAsString().split(":", 2)[1].toUpperCase())).sbItem();
                    int count = resultObject.has("count") ? resultObject.get("count").getAsInt() : 1;
                    return new ShapelessRecipe(result, count, requirements, ingredients);
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

        private static Requirement[] readRequirements(JsonObject jsonObject) {
            Requirement[] requirements = new Requirement[0];
            if (jsonObject.has("requirements")) {
                JsonArray requirementsArray = jsonObject.getAsJsonArray("requirements");
                requirements = new Requirement[requirementsArray.size()];
                int index = 0;
                for (JsonElement req : requirementsArray) {
                    String type = req.getAsJsonObject().get("type").getAsString();
                    CraftingRequirement requirement = null;
                    for (CraftingRequirement r : CraftingRequirement.values()) {
                        if (r.id.equals(type)) {
                            requirement = r;
                            break;
                        }
                    }
                    assert requirement != null;
                    requirements[index] = requirement.apply(req.getAsJsonObject());
                    index++;
                }
            }
            return requirements;
        }
    }

    @RequiredArgsConstructor
    enum CraftingRequirement implements Function<JsonObject, Requirement> {
        Collection("collection") {
            @Override
            public Requirement apply(JsonObject jsonObject) {
                return new CollectionRequirement(jsonObject.get("id").getAsString(), jsonObject.get("level").getAsInt());
            }
        }, Skill("skill") {
            @Override
            public Requirement apply(JsonObject jsonObject) {
                return new SkillRequirement(me.carscupcake.sbremake.player.skill.Skill.valueOf(jsonObject.get("name").getAsString()), jsonObject.get("level").getAsInt());
            }
        };
        private final String id;
    }

    //Result: Slot 22
    ItemStack noRecipeFound = new ItemBuilder(Material.BARRIER).setName("§cRecipe Required").addLore("§7Add the items for a valid recipe in the crafting grid to the left!").build();
    InventoryBuilder craftingTable = new InventoryBuilder(6, "Crafting Table").fill(TemplateItems.EmptySlot.getItem()).fill(ItemStack.AIR, 10, 12).fill(ItemStack.AIR, 19, 21).fill(ItemStack.AIR, 28, 30).setItem(noRecipeFound, 23);
    List<Integer> craftingGrid = List.of(10, 11, 12, 19, 20, 21, 28, 29, 30);

    static void openCraftingGui(SkyblockPlayer player) {
        Gui gui = new Gui(craftingTable.build());
        AtomicReference<Recipe> cacheRecipe = new AtomicReference<>();
        AtomicReference<SbItemStack> cacheItem = new AtomicReference<>();
        gui.setGeneralClickEvent(event -> {
            if (event.getInventory() != null && !craftingGrid.contains(event.getSlot())) {
                if (event.getSlot() == 23 && cacheItem.get() != null) {
                    List<SbItemStack> itemStacks = new ArrayList<>();
                    for (int i : craftingGrid)
                        itemStacks.add(SbItemStack.from(gui.getInventory().getItemStack(i)));
                    if (event.getClickType() == ClickType.START_SHIFT_CLICK) {
                        SbItemStack item = cacheItem.get();
                        if (item.sbItem().isUnstackable()) {
                            if (!player.addItem(item, false)) return true;
                            cacheRecipe.get().consume(itemStacks);
                        } else {
                            Recipe recipe = cacheRecipe.get();
                            while (recipe.creatable(itemStacks)) {
                                if (!player.addItem(item, false)) break;
                                recipe.consume(itemStacks);
                            }
                        }
                    } else {
                        if (!cacheRecipe.get().canCraft(player)) {
                            Main.LOGGER.warn("Cached recipe was not craftable!");
                            return true;
                        }
                        if (event.getCursorItem() == ItemStack.AIR || (!cacheItem.get().sbItem().isUnstackable() && cacheItem.get().sbItem() == SbItemStack.from(event.getCursorItem()).sbItem() && event.getCursorItem().amount() + cacheItem.get().item().amount() <= event.getCursorItem().material().maxStackSize()))
                            cacheRecipe.get().consume(itemStacks);
                        else return true;
                    }
                    if (event.getClickType() != ClickType.START_SHIFT_CLICK) {
                        if (event.getCursorItem() != ItemStack.AIR)
                            player.getInventory().setCursorItem(Objects.requireNonNull(cacheItem.get().withAmount(event.getCursorItem().amount() + cacheItem.get().item().amount())).item());
                        else player.getInventory().setCursorItem(cacheItem.get().item());
                    }
                    for (int i = 0; i < 9; i++) {
                        SbItemStack item = itemStacks.get(i);
                        gui.getInventory().setItemStack(craftingGrid.get(i), (item == null) ? ItemStack.AIR : item.item());
                    }
                }
                return true;
            }
            return false;
        });
        gui.setItemChangeEvent(event -> {
            if (craftingGrid.contains(event.getSlot())){
                Recipe recipe = null;
                List<SbItemStack> itemStacks = new ArrayList<>();
                for (int i : craftingGrid)
                    itemStacks.add(SbItemStack.from(Objects.requireNonNull(event.getInventory()).getItemStack(i)));
                for (Recipe r : craftingRecipes.values())
                    if (r.creatable(itemStacks)) {
                        if (r.canCraft(player)) {
                            recipe = r;
                        }
                        break;
                    }
                if (recipe == null) {
                    cacheItem.set(null);
                    gui.getInventory().setItemStack(23, noRecipeFound);
                } else {
                    SbItemStack result = recipe.getResult(itemStacks);
                    cacheItem.set(result);
                    cacheRecipe.set(recipe);
                    gui.getInventory().setItemStack(23, result.item());
                }
            }
        });
        gui.setCloseEvent(() -> {
            for (int i : craftingGrid) {
                SbItemStack item = SbItemStack.from(gui.getInventory().getItemStack(i));
                if (item != null) {
                    if (!player.addItem(item, false)) item.drop(player.getInstance(), player.getPosition());
                }
            }
            return false;
        });
        gui.showGui(player);
    }
}
