package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.item.ItemStack;

public interface Requirement {
    boolean canUse(SkyblockPlayer player, ItemStack item);

    String display();
}
