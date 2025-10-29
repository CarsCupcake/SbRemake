package me.carscupcake.sbremake.entity.slayer.zombie.minibosses;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.RevenantFlesh;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.item.Material;

public class AtonedRevenant extends SkyblockEntity implements SkillXpDropper {
    public AtonedRevenant() {
        super(EntityType.ZOMBIE, new LootTable<SbItemStack>().addLoot(new ItemLoot(ISbItem.get(RevenantFlesh.class), 5, 1)), MobType.Undead);
        setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setGlint(true).setLeatherColor(new Color(0xFFFFFF)).build());
        setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setGlint(true).setLeatherColor(new Color(0xFFFFFF)).build());
        setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setGlint(true).setLeatherColor(new Color(0xFFFFFF)).build());
        setItemInHand(PlayerHand.MAIN, new ItemBuilder(Material.IRON_SWORD).setGlint(true).build());
        addAIGroup(zombieAiGroup(this, false));
    }

    @Override
    public float getMaxHealth() {
        return 2_400_000;
    }

    @Override
    public double getDamage() {
        return 1_600;
    }

    @Override
    public int getLevel() {
        return 770;
    }

    @Override
    public String getName() {
        return "Atoned Revenant";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 1_600;
    }

    @Override
    public void damage(SkyblockPlayer player) {
        super.damage(player);
    }
}
