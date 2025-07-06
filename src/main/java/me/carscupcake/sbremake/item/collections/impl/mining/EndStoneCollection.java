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

public class EmeraldCollection extends Collection {
    public EmeraldCollection(SkyblockPlayer player) {
        super(player, new int[]{50, 100, 250, 1_000, 2_500, 5_000, 10_000, 25_000, 50_000}, List.of(List.of(new SkyblockXpReward(4)),
                List.of(new SkyblockXpReward(4)), 
                List.of(new SkyblockXpReward(4)), new RecipeReward(Recipe.craftingRecipes.get("COIN_TALISMAN"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("MAGNETIC_TALISMAN"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("ENCHANTED_EMERALD"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("EMERALD_RING"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("PERSONAL_BANK_ITEM"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("ENCHANTED_EMERALD_BLOCK"))),
                List.of(new SkyblockXpReward(4), new RecipeReward(Recipe.craftingRecipes.get("EMERALD_BLADE"))),
        
                List.of(new RecipeReward(Recipe.craftingRecipes.get(new RecipeReward(Recipe.craftingRecipes.get ("EMERALD_ARMOR_HELMET")),
                                                                                                new RecipeReward(Recipe.craftingRecipes.get ("EMERALD_ARMOR_CHESTPLATE")),
                                                                                                new RecipeReward(Recipe.craftingRecipes.get ("EMERALD_ARMOR_LEGGINGS")),
                                                                                                new RecipeReward(Recipe.craftingRecipes.get ("EMERALD_ARMOR_BOOTS")),
        SkyblockXpReward(4))));
        
        
        
    }

    @Override
    public Material showItem() {
        return Material.EMERALD;
    }

    @Override
    public String getId() {
        return "EMERALD";
    }

    @Override
    public String getName() {
        return "EMERALD";
    }

    @Override
    public int progress(ISbItem item) {
        return switch (item) {
            case BaseSbItem i -> {
                if (i.getMaterial() == Material.EMERALD) yield 1;
                yield 0;
            }
            case EnchantedEmerald _ -> 160;
            case EnchantedBlockOfEmerald _ -> 160 * 160;
            case null, default -> 0;
        };
    }
              }
