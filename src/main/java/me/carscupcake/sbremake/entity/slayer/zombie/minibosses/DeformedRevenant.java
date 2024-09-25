package me.carscupcake.sbremake.entity.slayer.zombie.minibosses;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.RevenantFlesh;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.DyedItemColor;

public class DeformedRevenant extends SkyblockEntity implements SkillXpDropper {
    public DeformedRevenant() {
        super(EntityType.ZOMBIE, new LootTable<SbItemStack>().addLoot(new ItemLoot(ISbItem.get(RevenantFlesh.class), 5, 1)));
        setBoots(ItemStack.of(Material.DIAMOND_BOOTS));
        setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(new DyedItemColor(0xFF0000)).setGlint(true).build());
        setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
        setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setLeatherColor(new DyedItemColor(0xFF0000)).setGlint(true).build());
        setItemInHand(Player.Hand.MAIN, new ItemBuilder(Material.GOLDEN_SWORD).setGlint(true).build());
        addAIGroup(zombieAiGroup(this));
    }

    @Override
    public float getMaxHealth() {
        return 360_000;
    }

    @Override
    public double getDamage() {
        return 1_600;
    }

    @Override
    public int getLevel() {
        return 300;
    }

    @Override
    public String getName() {
        return "Deformed Revenant";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 1_200;
    }

    @Override
    public void damage(SkyblockPlayer player) {
        super.damage(player);
    }
}
