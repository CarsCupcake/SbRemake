package me.carscupcake.sbremake.item.impl.pets;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.ability.PetAbility;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.PlayerPet;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.StoredPet;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public interface IPet {
    String getName();

    String getId();

    String skullValue();

    PetAbility[] getAbility(ItemRarity rarity);

    double getStat(Stat stat, Pet.PetInfo petInfo);

    PetType getPetType();

    default int getLevel(ItemRarity rarity, double exp) {
        int level = 1;
        for (int xp : switch (rarity) {
            case COMMON -> Pet.common;
            case UNCOMMON -> Pet.uncommon;
            case RARE -> Pet.rare;
            case EPIC -> Pet.epic;
            default -> Pet.legendary;
        }) {
            if (exp >= xp) {
                level++;
                exp -= xp;
            } else break;
        }
        return level;
    }

    default int getMaxLevel() {
        return 100;
    }

    default void spawnPet(SkyblockPlayer player, StoredPet petInfo) {
        player.setPetTask(new PlayerPet(petInfo, player));
    }

    default void despawnPet(SkyblockPlayer player, StoredPet petInfo) {
        if (player.getPetTask() != null)
            player.getPetTask().cancel();
    }

    default SbItemStack create(ItemRarity rarity) {
        return create(rarity, 0, null, 0);
    }

    default SbItemStack create(ItemRarity rarity, double exp, @Nullable PetItem petItem, int petCandy) {
        SbItemStack item = ISbItem.get(Pet.class).create();
        return item.withModifier(Modifier.PET_INFO, new Pet.PetInfo(this, rarity, exp, petItem, petCandy));
    }

    HashMap<String, IPet> pets = new HashMap<>();
}
