package me.carscupcake.sbremake.item.impl.pets;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.player.StoredPet;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Pet implements ISbItem {
    @Override
    public String getId() {
        return "PET";
    }

    @Override
    public String getName() {
        return "§cNo Name!";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Pet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.SPECIAL;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    public record PetInfo(IPet pet, ItemRarity rarity, int level, double exp, @Nullable PetItem petItem,
                          int petCandyUsed) {
        public PetInfo(IPet pet, ItemRarity rarity, double exp, @Nullable PetItem petItem, int petCandyUsed) {
            this(pet, rarity, pet.getLevel(rarity, exp), exp, petItem, petCandyUsed);
        }
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return new ArrayList<>(List.of(new ItemAbility<>("", AbilityType.RIGHT_CLICK, playerInteractEvent -> {
            playerInteractEvent.player().getPets().add(StoredPet.to(SbItemStack.from(playerInteractEvent.getPlayer().getItemInHand(Player.Hand.MAIN))));
            playerInteractEvent.player().sendMessage("§aAdded the Pet to you pet menu!");
            playerInteractEvent.getPlayer().setItemInMainHand(ItemStack.AIR);
        })));
    }

    public static final int[] common = {
            100, 110, 120, 130, 145, 160, 175, 190, 210, 230, 250, 275, 300, 330, 360,
            400, 440, 490, 540, 600, 660, 730, 800, 880, 960, 1050, 1150, 1260, 1380,
            1510, 1650, 1800, 1960, 2130, 2310, 2500, 2700, 2920, 3160, 3420, 3700,
            4000, 4350, 4750, 5200, 5700, 6300, 7000, 7800, 8700, 9700, 10800, 12000,
            13300, 14700, 16200, 17800, 19500, 21300, 23200, 25200, 27400, 29800,
            32400, 35200, 38200, 41400, 44800, 48400, 52200, 56200, 60400, 64800,
            69420, 74200, 79200, 84700, 90700, 97200, 104200, 111700, 119700, 128200,
            137200, 146700, 156700, 167700, 179700, 192700, 206700, 221700, 237700,
            254700, 272700, 291700, 311700, 333700, 357700, 383700
    };

    public static final int[] uncommon = {
            175, 190, 210, 230, 250, 275, 300, 330, 360, 400, 440, 490, 540, 600, 660,
            730, 800, 880, 960, 1050, 1150, 1260, 1380, 1510, 1650, 1800, 1960, 2130,
            2310, 2500, 2700, 2920, 3160, 3420, 3700, 4000, 4350, 4750, 5200, 5700,
            6300, 7000, 7800, 8700, 9700, 10800, 12000, 13300, 14700, 16200, 17800,
            19500, 21300, 23200, 25200, 27400, 29800, 32400, 35200, 38200, 41400,
            44800, 48400, 52200, 56200, 60400, 64800, 69400, 74200, 79200, 84700,
            90700, 97200, 104200, 111700, 119700, 128200, 137200, 146700, 156700,
            167700, 179700, 192700, 206700, 221700, 237700, 254700, 272700, 291700,
            311700, 333700, 357700, 383700, 411700, 441700, 476700, 516700, 561700,
            611700
    };

    public static final int[] rare = {
            275, 300, 330, 360, 400, 440, 490, 540, 600, 660, 730, 800, 880, 960, 1050,
            1150, 1260, 1380, 1510, 1650, 1800, 1960, 2130, 2310, 2500, 2700, 2920,
            3160, 3420, 3700, 4000, 4350, 4750, 5200, 5700, 6300, 7000, 7800, 8700,
            9700, 10800, 12000, 13300, 14700, 16200, 17800, 19500, 21300, 23200, 25200,
            27400, 29800, 32400, 35200, 38200, 41400, 44800, 48400, 52200, 56200,
            60400, 64800, 69400, 74200, 79200, 84700, 90700, 97200, 104200, 111700,
            119700, 128200, 137200, 146700, 156700, 167700, 179700, 192700, 206700,
            221700, 237700, 254700, 272700, 291700, 311700, 333700, 357700, 383700,
            411700, 441700, 476700, 516700, 561700, 611700, 666700, 726700, 791700,
            861700, 936700
    };

    public static final int[] epic = {  // Default value for index 0
            440, 490, 540, 600, 660, 730, 800, 880, 960, 1050, 1150, 1260, 1380, 1510,
            1650, 1800, 1960, 2130, 2310, 2500, 2700, 2920, 3160, 3420, 3700, 4000,
            4350, 4750, 5200, 5700, 6300, 7000, 7800, 8700, 8700, 10800, 12000, 13300,
            14700, 16200, 17800, 19500, 21300, 23200, 25200, 27400, 29800, 32400,
            35200, 38200, 41400, 44800, 48400, 48400, 52200, 56200, 60400, 64800,
            69400, 74200, 79200, 84700, 90700, 97200, 104200, 111700, 119700, 128200,
            137200, 146700, 156700, 167700, 179700, 192700, 206700, 221700, 237700,
            254700, 272700, 291700, 311700, 333700, 357700, 383700, 411700, 441700,
            476700, 516700, 611700, 666700, 726700, 791700, 861700, 936700, 1016700,
            1101700, 1191700, 1286700, 1386700
    };

    public static final int[] legendary = {
            660, 730, 800, 880, 960, 1050, 1150, 1260, 1380, 1510, 1650, 1800, 1960,
            2130, 2310, 2500, 2700, 2920, 3160, 3420, 3700, 4000, 4350, 4750, 5200,
            5700, 6300, 7000, 7800, 8700, 9700, 10800, 12000, 13300, 14700, 16200,
            17800, 19500, 21300, 23200, 25200, 27400, 29800, 32400, 35200, 38200,
            41400, 44800, 48400, 52200, 56200, 60400, 64800, 69400, 74200, 79200,
            84700, 90700, 97200, 104200, 111700, 119700, 128200, 137200, 146700,
            156700, 167700, 179700, 192700, 206700, 221700, 237700, 254700, 272700,
            291700, 311700, 333700, 357700, 383700, 411700, 441700, 476700, 516700,
            516700, 611700, 666700, 726700, 791700, 861700, 936700, 1016700, 1101700,
            1191700, 1286700, 1386700, 1496700, 1616700, 1746700, 1886700
    };


}
