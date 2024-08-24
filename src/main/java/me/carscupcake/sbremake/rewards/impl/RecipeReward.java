package me.carscupcake.sbremake.rewards.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.Reward;

public record RecipeReward(Recipe recipe) implements Reward {
    @Override
    public void reward(SkyblockPlayer player) {}

    @Override
    public Lore lore() {
        SbItemStack result = recipe.getResult(null);
        return new Lore(STR."\{result.getRarity().getPrefix()}\{result.displayName()} §7Recipe");
    }
}
