package me.carscupcake.sbremake.entity.impl.hub;

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

public class GoldenGhoul extends SkyblockEntity implements SkillXpDropper {
    public GoldenGhoul() {
        super(EntityType.ZOMBIE, new LootTable<SbItemStack>().addLoot(new ItemLoot(SbItemStack.base(Material.ROTTEN_FLESH), 2, 2, 1))
                .addLoot(new ItemLoot(SbItemStack.base(Material.GOLD_INGOT), 1, 9, 1))
                .addLoot(new CoinLoot(100)));
        addAIGroup(zombieAiGroup(this, Hub.Region.HubCrypts));
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.2f);
        setBoots(ItemStack.of(Material.GOLDEN_BOOTS));
        setLeggings(ItemStack.of(Material.GOLDEN_LEGGINGS));
        setChestplate(ItemStack.of(Material.GOLDEN_CHESTPLATE));
        setItemInMainHand(ItemStack.of(Material.GOLDEN_SWORD));
    }

    @Override
    public float getMaxHealth() {
        return 45_000;
    }

    @Override
    public String getName() {
        return "Golden Ghoul";
    }

    @Override
    public double getDamage() {
        return 800;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 50;
    }
}
