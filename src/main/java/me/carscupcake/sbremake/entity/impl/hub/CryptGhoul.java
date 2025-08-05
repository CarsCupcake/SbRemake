package me.carscupcake.sbremake.entity.impl.hub;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.pets.Pet;
import me.carscupcake.sbremake.item.impl.pets.Pets;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.lootTable.CoinLoot;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import me.carscupcake.sbremake.worlds.impl.Hub;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class CryptGhoul extends SkyblockEntity implements SkillXpDropper {
    public CryptGhoul() {
        super(EntityType.ZOMBIE, new LootTable<SbItemStack>().addLoot(new ItemLoot(SbItemStack.base(Material.ROTTEN_FLESH), 1, 2, 1))
                .addLoot(new CoinLoot(13)).addLoot(new ItemLoot(SbItemStack.from(Pet.class).withModifier(Modifier.PET_INFO, new Pet.PetInfo(Pets.Ghoul, ItemRarity.EPIC, 0)), 1, 1, 0.00003, true, true, ItemLoot.NormalMessages.RNGesus))
                .addLoot(new ItemLoot(SbItemStack.from(Pet.class).withModifier(Modifier.PET_INFO, new Pet.PetInfo(Pets.Ghoul, ItemRarity.LEGENDARY, 0)), 1, 1, 0.00001, true, true, ItemLoot.NormalMessages.RNGesus)),
                MobType.Undead);
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.2f);
        setBoots(ItemStack.of(Material.CHAINMAIL_BOOTS));
        setLeggings(ItemStack.of(Material.CHAINMAIL_LEGGINGS));
        setChestplate(ItemStack.of(Material.CHAINMAIL_CHESTPLATE));
        setItemInMainHand(ItemStack.of(Material.IRON_SWORD));
    }

    @Override
    public void spawn() {
        addAIGroup(zombieAiGroup(this, Hub.Region.HubCrypts, true));
    }

    @Override
    public float getMaxHealth() {
        return 2_000;
    }

    @Override
    public String getName() {
        return "Crypt Ghoul";
    }

    @Override
    public double getDamage() {
        return 350;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 32;
    }
}
