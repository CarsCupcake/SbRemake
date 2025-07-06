package me.carscupcake.sbremake.item.collections.impl.mining;

import me.carscupcake.sbremake.item.BaseSbItem;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedBlockOfCoal;
import me.carscupcake.sbremake.item.impl.other.mining.resources.EnchantedCoal;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.rewards.impl.RecipeReward;
import me.carscupcake.sbremake.rewards.impl.SkyblockXpReward;
import net.minestom.server.item.Material;

import java.util.List;

public class GlowStoneCollection extends Collection {
    public GlowStoneCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 1_000, 2_500, 5_000, 10_000, 25_000}, List.of(List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)), 
                List.of(new SkyblockXpReward(4)), new RecipeReward(Recipe.craftingRecipes.get("PORTAL_TO_THE_CRIMSON_ISLE"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("ENCHANTED_GLOWSTONE_DUST"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("CRIMSON_ISLE"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("ENCHANTED_GLOWSTONE"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("ENCHANTED_REDSTONE_LAMP"))),
                List.of(new RecipeReward(Recipe.craftingRecipes.get("VANQUISHED_GLOWSTONE_GAUNTLET"))),
                List.of(new SkyblockXpReward(4))));
               
        
        
        
    }

    @Override
    public Material showItem() {
        return Material.GLOWSTONE;
    }

    @Override
    public String getId() {
        return "GLOWSTONE";
    }

    @Override
    public String getName() {
        return "GLOWSTONE";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item) {
            case BaseSbItem i -> {
                if (i.getMaterial() == Material.Glowstone) yield 1;
                yield 0;
            }
            case EnchantedGlowStoneDust _ -> 160;
            case EnchantedGlowStone _ -> 160 * 160;
            case null, default -> 0;
        };
    }
    }
