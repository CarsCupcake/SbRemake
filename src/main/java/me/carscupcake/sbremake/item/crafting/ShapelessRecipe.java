package me.carscupcake.sbremake.item.crafting;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.SbItemStack;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public record ShapelessRecipe(ISbItem result, int amount, Requirement[] requirements,
                              CraftingIngredient... ingredients) implements Recipe {
    public ShapelessRecipe(ISbItem result, int amount, CraftingIngredient... ingredients) {
        this(result, amount, new Requirement[0], ingredients);
    }

    @Override
    public SbItemStack getResult(@Nullable List<SbItemStack> items) {
        return result.create().withAmount(amount);
    }

    @Override
    public boolean creatable(List<SbItemStack> items) {
        List<CraftingIngredient> checked = new ArrayList<>();
        HashMap<CraftingIngredient, Integer> ammountMissing = new HashMap<>();
        for (SbItemStack itemStack : items) {
            if (itemStack == null || itemStack == SbItemStack.AIR) continue;
            CraftingIngredient ingredient = null;
            for (CraftingIngredient i : this.ingredients) if (i.contains(itemStack)) ingredient = i;
            if (ingredient == null)
                return false;
            if (checked.contains(ingredient)) return false;
            int amount = itemStack.item().amount();
            int delta = ammountMissing.getOrDefault(ingredient, ingredient.amount()) - amount;
            if (delta <= 0) checked.add(ingredient);
            else ammountMissing.put(ingredient, delta);
        }
        return checked.size() == this.ingredients.length;
    }

    @Override
    public Inventory recipePreview() {
        return null;
    }

    @Override
    public void consume(List<SbItemStack> items) {
        HashMap<CraftingIngredient, Integer> ammountMissing = new HashMap<>();
        int index = 0;
        for (SbItemStack i : items) {
            if (i == SbItemStack.AIR) {
                index++;
                continue;
            }
            CraftingIngredient ingredient = null;
            for (CraftingIngredient in : this.ingredients) {
                if (in.contains(i)) {
                    ingredient = in;
                    break;
                }
            }
            assert ingredient != null;
            int amount = i.item().amount();
            int delta;
            int am = ammountMissing.getOrDefault(ingredient, ingredient.amount());
            if (am > 64)
                items.set(index, i.withAmount(0));
            else if (am > i.item().amount())
                items.set(index, i.withAmount(0));
            else
                items.set(index, i.withAmount(i.item().amount() - am));
            delta = am - amount;
            if (delta <= 0) ammountMissing.remove(ingredient);
            index++;
        }
    }
}
