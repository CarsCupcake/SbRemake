package me.carscupcake.sbremake.item.requirements;

import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.item.ItemStack;

public record CollectionRequirement(String id, int level) implements Requirement {
    @Override
    public boolean canUse(SkyblockPlayer player, ItemStack item) {
        if (player == null) return false;
        var collection = player.getCollections().get(id);
        return collection.getLevel() >= level;
    }

    @Override
    public String display() {
        return "§4❣ §cRequires §a" + (id) + " Collection " + (level) + ".";
    }
}
