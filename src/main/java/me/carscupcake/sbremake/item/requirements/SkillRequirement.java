package me.carscupcake.sbremake.item.requirements;

import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.item.ItemStack;

@SuppressWarnings("preview")
public record SkillRequirement(Skill skill, int level) implements Requirement {
    @Override
    public boolean canUse(SkyblockPlayer player, ItemStack item) {
        if (player == null) return false;
        return player.getSkill(skill).getLevel() >= level;
    }

    @Override
    public String display() {
        return "§4❣ §cRequires §a" + (skill.name()) + " Skill " + (level) + ".";
    }
}
