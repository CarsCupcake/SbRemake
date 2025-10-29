package me.carscupcake.sbremake.item.crafting;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.SbItemStack;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public record ShapedRecipe(List<CraftingIngredient> ingredients, ISbItem result, int amount, int prioritySlot,
                           Grid grid, Requirement... requirements) implements Recipe {
    public static ShapedRecipe createShapedRecipe(ISbItem result, int amount, int prioritySlot, Map<Character, CraftingIngredient> ingredientMap, String... items) {
        return createShapedRecipe(result, amount, prioritySlot, new Requirement[0], ingredientMap, items);
    }

    public static ShapedRecipe createShapedRecipe(ISbItem result, int amount, Map<Character, CraftingIngredient> ingredientMap, String... items) {
        return createShapedRecipe(result, amount, -1, new Requirement[0], ingredientMap, items);
    }

    public static ShapedRecipe createShapedRecipe(ISbItem result, int amount, int prioritySlot, Requirement[] requirement, Map<Character, CraftingIngredient> ingredientMap, String... items) {
        assert items.length < 4 && items.length > 0;
        int length = items[0].length();
        List<CraftingIngredient> itemsList = new ArrayList<>();
        for (String s : items)
            assert length == s.length();
        for (String i : items)
            for (char j : i.toCharArray()) {
                if (j == ' ') itemsList.add(null);
                else {
                    assert ingredientMap.containsKey(j);
                    itemsList.add(ingredientMap.get(j));
                }
            }
        int height = items.length;
        return new ShapedRecipe(itemsList, result, amount, prioritySlot, switch (length) {
            case 1 -> {
                switch (height) {
                    case 2 -> {
                        yield Grid.Grid1x2;
                    }
                    case 3 -> {
                        yield Grid.Grid1x3;
                    }
                    default -> throw new IllegalStateException("Not possible");
                }
            }
            case 2 -> {
                switch (height) {
                    case 1 -> {
                        yield Grid.Grid2x1;
                    }
                    case 2 -> {
                        yield Grid.Grid2x2;
                    }
                    case 3 -> {
                        yield Grid.Grid2x3;
                    }
                    default -> throw new IllegalStateException("Not possible");
                }
            }
            case 3 -> {
                switch (height) {
                    case 1 -> {
                        yield Grid.Grid3x1;
                    }
                    case 2 -> {
                        yield Grid.Grid3x2;
                    }
                    case 3 -> {
                        yield Grid.Grid3x3;
                    }
                    default -> throw new IllegalStateException("Not possible");
                }
            }
            default -> throw new IllegalStateException("Not possible");
        }, requirement);
    }

    @Override
    public SbItemStack getResult(@Nullable List<SbItemStack> items) {
        //TODO: Invalid Gemstone Handling
        var item = result.create().withAmount(amount);
        if (prioritySlot == -1 || items == null)
            return item;
        var component = items.get(prioritySlot).item().get(DataComponents.CUSTOM_DATA).nbt();
        var resC = item.item().get(DataComponents.CUSTOM_DATA).nbt();
        for (var entry : component) {
            if (entry.getKey().equalsIgnoreCase("id")) continue;
            resC = resC.put(entry.getKey(), entry.getValue());
        }
        return SbItemStack.from(item.item().with(DataComponents.CUSTOM_DATA, new CustomData(resC))).update();
    }

    @Override
    public boolean creatable(List<SbItemStack> items) {
        for (List<SbItemStack> current : toSegments(items)) {
            boolean b = true;
            int i = 0;
            for (SbItemStack itemStack : current) {
                if (itemStack == null) itemStack = SbItemStack.AIR;
                CraftingIngredient ingredient = this.ingredients.get(i);
                if ((ingredient == null && itemStack == SbItemStack.AIR)) {
                    i++;
                    continue;
                }
                if (ingredient == null) {
                    b = false;
                    break;
                }
                if (ingredient.items().length == 0 && itemStack == SbItemStack.AIR) {
                    i++;
                    continue;
                }
                if (itemStack == SbItemStack.AIR) {
                    b = false;
                    break;
                }
                if (!ingredient.check(itemStack)) {
                    b = false;
                    break;
                }
                i++;
            }
            if (b) return true;
        }
        return false;
    }

    @Override
    public Inventory recipePreview() {
        //TODO
        return null;
    }

    @Override
    public void consume(List<SbItemStack> items) {
        List<Integer> segments = null;
        for (List<Integer> combi : grid.gridCombinations) {
            List<Integer> b = new ArrayList<>();
            boolean bol = true;
            for (int i = 0; i < 9; i++) {
                if (combi.contains(i)) b.add(i);
                else if (items.get(i) != SbItemStack.AIR) {
                    bol = false;
                    break;
                }
            }
            if (bol) {
                segments = b;
                break;
            }
        }
        if (segments == null) return;
        int i = 0;
        for (Integer slot : segments) {
            SbItemStack item = items.get(slot);
            if (item != SbItemStack.AIR)
                items.set(slot, item.withAmount(item.item().amount() - this.ingredients.get(i).amount()));
            i++;
        }
    }

    private List<List<SbItemStack>> toSegments(List<SbItemStack> items) {
        LinkedList<LinkedList<Integer>> slotCombination = grid.gridCombinations;
        List<List<SbItemStack>> lists = new LinkedList<>();
        for (List<Integer> combi : slotCombination) {
            List<SbItemStack> b = new LinkedList<>();
            boolean bol = true;
            for (int i = 0; i < 9; i++) {
                if (combi.contains(i)) b.add(items.get(i));
                else if (items.get(i) != SbItemStack.AIR) {
                    bol = false;
                    break;
                }
            }
            if (bol) lists.add(b);
        }
        return lists;


    }

    public enum Grid {
        Grid2x2(linkedListOf(0, 1, 3, 4), linkedListOf(1, 2, 4, 5), linkedListOf(3, 4, 6, 7), linkedListOf(4, 5, 7, 8)), Grid2x3(() -> {
            LinkedList<LinkedList<Integer>> list = new LinkedList<>();
            for (int i = 0; i + 7 < 9; i++) {
                if ((i + 1) % 3 == 0) continue;
                list.add(linkedListOf(i, i + 1, i + 3, i + 4, i + 6, i + 7));
            }
            return list;
        }), Grid3x2(() -> {
            LinkedList<LinkedList<Integer>> list = new LinkedList<>();
            for (int i = 0; i + 5 < 9; i += 3) {
                if ((i + 1) % 3 == 0) continue;
                list.add(linkedListOf(i, i + 1, i + 2, i + 3, i + 4, i + 5));
            }
            return list;
        }), Grid1x3(linkedListOf(0, 3, 6), linkedListOf(1, 4, 7), linkedListOf(2, 5, 8)), Grid3x1(linkedListOf(0, 1, 2), linkedListOf(3, 4, 5), linkedListOf(6, 7, 8)), Grid1x2(() -> {
            LinkedList<LinkedList<Integer>> list = new LinkedList<>();
            for (int i = 0; i + 3 < 9; i++) {
                list.add(linkedListOf(i, i + 3));
            }
            return list;
        }), Grid2x1(() -> {
            LinkedList<LinkedList<Integer>> list = new LinkedList<>();
            for (int i = 0; i + 1 < 9; i++) {
                if ((i + 1) % 3 == 0) continue;
                list.add(linkedListOf(i, i + 1));
            }
            return list;
        }), Grid3x3(linkedListOf(0, 1, 2, 3, 4, 5, 6, 7, 8));
        private final LinkedList<LinkedList<Integer>> gridCombinations;

        @SafeVarargs
        Grid(LinkedList<Integer>... grids) {
            gridCombinations = new LinkedList<>(List.of(grids));
        }

        Grid(Supplier<LinkedList<LinkedList<Integer>>> returnable) {
            gridCombinations = returnable.get();
        }

        @SafeVarargs
        private static <T> LinkedList<T> linkedListOf(T... t) {
            return new LinkedList<>(Arrays.asList(t));
        }
    }
}