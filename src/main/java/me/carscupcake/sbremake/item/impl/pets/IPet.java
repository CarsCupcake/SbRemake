package me.carscupcake.sbremake.item.impl.pets;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.KeyClass;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.ability.PetAbility;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.PlayerPet;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.StoredPet;
import org.jetbrains.annotations.Nullable;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public interface IPet extends KeyClass {
    @Override
    default String key() {
        return getId();
    }
    String getName();

    String getId();

    String skullValue();

    PetAbility[] getAbility(ItemRarity rarity);

    double getStat(Stat stat, Pet.PetInfo petInfo);

    PetType getPetType();

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

    @SuppressWarnings("unused")
    static IPet fromKey(String key) {
        return pets.get(key);
    }


    default LevelingType getLevelingType() {
        return LevelingType.Normal;
    }

    @Getter
    enum LevelingType {
        Normal(100),
        Dragon(200);
        private final int maxLevel;
        LevelingType(int maxLevel) {
            this.maxLevel = maxLevel;
        }

        public Iterable<Double> xpIterator(ItemRarity rarity) {
            return () -> new Iterator<>() {

                int i = 1;

                @Override
                public boolean hasNext() {
                    return i < maxLevel;
                }

                @Override
                public Double next() {
                    return Pet.PetInfo.nextLevelXp(rarity, i++, LevelingType.this);
                }
            };
        }
    }
}
