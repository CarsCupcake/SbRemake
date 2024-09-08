package me.carscupcake.sbremake.player;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.pets.IPet;
import me.carscupcake.sbremake.item.impl.pets.Pet;
import me.carscupcake.sbremake.item.impl.pets.PetItem;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.tag.Tag;

import java.util.UUID;

@Getter
@Setter
public class StoredPet {
    private final IPet pet;
    private double xp;
    private int level;
    private ItemRarity rarity;
    private PetItem petItem;
    private int petCandyUsed;
    private final UUID uuid;
    private double requiredXp = 0;

    public StoredPet(IPet pet, double xp, ItemRarity rarity, PetItem petItem, int petCandyUsed, UUID uuid) {
        this.pet = pet;
        this.xp = xp;
        this.rarity = rarity;
        this.petItem = petItem;
        this.petCandyUsed = petCandyUsed;
        this.uuid = uuid;
        int level = 1;
        double exp = xp;
        for (int rXp : switch (rarity) {
            case COMMON -> Pet.common;
            case UNCOMMON -> Pet.uncommon;
            case RARE -> Pet.rare;
            case EPIC -> Pet.epic;
            default -> Pet.legendary;
        }) {
            requiredXp += rXp;
            if (exp >= rXp) {
                level++;
                exp -= rXp;
            } else break;
        }
        this.level = level;
    }

    public SbItemStack toItem() {
        return pet.create(rarity, xp, petItem, petCandyUsed);
    }

    public static StoredPet to(SbItemStack item) {
        Pet.PetInfo info = item.getModifier(Modifier.PET_INFO);
        return new StoredPet(info.pet(), info.exp(), info.rarity(), info.petItem(), info.petCandyUsed(), UUID.fromString(((CompoundBinaryTag) item.item().getTag(Tag.NBT("ExtraAttributes"))).getString("uuid")));
    }

    public void addXp(double amount) {
        xp += amount;
        while (xp >= requiredXp && level != pet.getMaxLevel()) {
            int[] i = switch (rarity) {
                case COMMON -> Pet.common;
                case UNCOMMON -> Pet.uncommon;
                case RARE -> Pet.rare;
                case EPIC -> Pet.epic;
                default -> Pet.legendary;
            };
            requiredXp += i[level++];
        }
    }
    public Pet.PetInfo toPetInfo() {
        return new Pet.PetInfo(pet, rarity, level, xp, petItem, petCandyUsed);
    }
}
