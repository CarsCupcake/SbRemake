package me.carscupcake.sbremake.item;

public interface EnchantedRecipe {
    Class<? extends ISbItem> base();
    String collection();
    int level();
}
