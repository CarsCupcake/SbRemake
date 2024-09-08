package me.carscupcake.sbremake.player.potion;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.SbItemStack;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record PotionRecipe(Class<ISbItem> normal, Class<ISbItem> enchanted) implements Recipe {
    @Override
    public SbItemStack getResult(@Nullable List<SbItemStack> items) {
        return null;
    }

    @Override
    public boolean creatable(List<SbItemStack> items) {
        return false;
    }

    @Override
    public Inventory recipePreview() {
        return null;
    }

    @Override
    public Requirement[] requirements() {
        return new Requirement[0];
    }

    @Override
    public void consume(List<SbItemStack> items) {

    }
}
