package me.carscupcake.sbremake.item.impl.sword.dungeons;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class DreadlordSword extends DungeonItem implements NpcSellable {
    public DreadlordSword() {
        super(Map.of(
                Stat.Damage, new Number[]{52,56,60,65,70,75,80,86,93,100},
                Stat.Strength, new Number[]{23,25,27,29,31,34,36,39,42,45},
                Stat.Intelligence, new Number[]{85,92,100,109,119,129,140,152,166,180}
        ));
    }

    @Override
    public String getId() {
        return "CRYPT_DREADLORD_SWORD";
    }

    @Override
    public String getName() {
        return "Dreadlord Sword";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public int sellPrice() {
        return 200;
    }

    private static final List<Requirement> requirements = List.of(new SkillRequirement(Skill.Dungeneering, 3));

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }

    @Override
    public Essence getEssence() {
        return Essence.Undead;
    }
}
