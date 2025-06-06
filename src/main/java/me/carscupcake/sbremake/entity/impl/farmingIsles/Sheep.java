package me.carscupcake.sbremake.entity.impl.farmingIsles;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import me.carscupcake.sbremake.worlds.impl.FarmingIsles;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.Material;

public class Sheep extends SkyblockEntity implements SkillXpDropper {
    public Sheep() {
        super(EntityType.SHEEP, new LootTable<SbItemStack>().addLoot(new ItemLoot(ISbItem.get(Material.MUTTON), 1, 1)).addLoot(new ItemLoot(ISbItem.get(Material.WHITE_WOOL), 1, 1)));
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.1);
    }

    @Override
    public void spawn() {
        addAIGroup(randomStroll(this, FarmingIsles.Region.Oasis, 4));
    }

    @Override
    public float getMaxHealth() {
        return 50;
    }

    @Override
    public String getName() {
        return "Sheep";
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
