package me.carscupcake.sbremake.player;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.config.*;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.pets.IPet;
import me.carscupcake.sbremake.item.impl.pets.Pet;
import me.carscupcake.sbremake.item.impl.pets.PetItem;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import net.minestom.server.component.DataComponents;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class StoredPet implements DefaultConfigItem {
    @ConfigField
    private final IPet pet;
    @ConfigField
    private final UUID uuid;
    @ConfigField
    private double xp;
    @ConfigField
    private int level;
    @ConfigField
    private ItemRarity rarity;
    @ConfigField
    private PetItem petItem;
    @ConfigField
    private int petCandyUsed;
    private double requiredXp = 0;

    @ConfigConstructor
    public StoredPet(@ConfigParameter("pet") IPet pet,
                     @ConfigParameter("xp") double xp,
                     @ConfigParameter("rarity") ItemRarity rarity,
                     @ConfigParameter("petItem") PetItem petItem,
                     @ConfigParameter("petCandyUsed") int petCandyUsed,
                     @ConfigParameter("uuid") UUID uuid) {
        this.pet = pet;
        this.xp = xp;
        this.rarity = rarity;
        this.petItem = petItem;
        this.petCandyUsed = petCandyUsed;
        this.uuid = uuid;
        this.level = Pet.PetInfo.calculateLevel(xp, rarity, pet.getLevelingType());
        for (int i = 1; i < level + 1; i++) {
            requiredXp += Pet.PetInfo.nextLevelXp(rarity, i, pet.getLevelingType());
        }
    }

    public static StoredPet to(SbItemStack item) {
        Pet.PetInfo info = item.getModifier(Modifier.PET_INFO);
        return new StoredPet(info.pet(), info.exp(), info.rarity(), info.petItem(), info.petCandyUsed(), UUID.fromString(Objects.requireNonNull(item.item().get(DataComponents.CUSTOM_DATA)).nbt().getString("uuid")));
    }

    public SbItemStack toItem() {
        return pet.create(rarity, xp, petItem, petCandyUsed);
    }

    public void addXp(double amount) {
        xp += amount;
        while (xp >= requiredXp && level < pet.getLevelingType().getMaxLevel()) {
            requiredXp += Pet.PetInfo.nextLevelXp(rarity, ++level, pet.getLevelingType());
        }
    }

    public Pet.PetInfo toPetInfo() {
        return new Pet.PetInfo(pet, rarity, xp, petItem, petCandyUsed);
    }
}
