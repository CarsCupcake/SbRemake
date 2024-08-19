package me.carscupcake.sbremake.entity.impl.farmingIsles;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;

public class Chicken extends SkyblockEntity implements SkillXpDropper {
    public Chicken() {
        super(EntityType.CHICKEN, new LootTable<SbItemStack>().addLoot(new ItemLoot(ISbItem.get(Material.FEATHER), 1, 1)).addLoot(new ItemLoot(ISbItem.get(Material.CHICKEN), 1, 1)));
        addAIGroup(randomStroll(this, 4));
    }

    @Override
    public float getMaxHealth() {
        return 50;
    }

    @Override
    public String getName() {
        return "Chicken";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 3;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
