package me.carscupcake.sbremake.item.requirements;

import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.item.ItemStack;

public record SlayerRequirement(ISlayer slayer, int level) implements Requirement {
    @Override
    public boolean canUse(SkyblockPlayer player, ItemStack item) {
        if (player == null) return false;
        return player.getSlayers().get(slayer).getLevel() >= level;
    }

    @Override
    public String display() {
        return "§4☠ §cRequires §5" + (slayer.getName()) + " " + (level) + "§c.";
    }
}
