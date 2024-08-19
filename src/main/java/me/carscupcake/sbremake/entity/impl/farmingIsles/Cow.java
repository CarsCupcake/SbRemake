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

public class Cow extends SkyblockEntity implements SkillXpDropper {
    public Cow() {
        super(EntityType.COW, new LootTable<SbItemStack>().addLoot(new ItemLoot(ISbItem.get(Material.BEEF), 1, 1)).addLoot(new ItemLoot(ISbItem.get(Material.LEATHER), 1, 1)));
        addAIGroup(randomStroll(this, 4));
    }

    @Override
    public float getMaxHealth() {
        return 50;
    }

    @Override
    public String getName() {
        return "Cow";
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
