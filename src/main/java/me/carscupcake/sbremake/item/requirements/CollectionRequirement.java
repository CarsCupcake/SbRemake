package me.carscupcake.sbremake.item.requirements;

import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.item.ItemStack;

public record CollectionRequirement(String id, int level) implements Requirement {
    @Override
    public boolean canUse(SkyblockPlayer player, ItemStack item) {
        for (Collection collection : player.getCollections()) {
            if (collection.getId().equals(id))
                return collection.getLevel() >= level;
        }
        return false;
    }

    @Override
    public String display() {
        return STR."§4❣ §cRequires §a\{id} Collection \{level}.";
    }
}
