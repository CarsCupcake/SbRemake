package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.gui.ItemBuilder;

public interface Cost {
    boolean canPay(SkyblockPlayer player);

    void pay(SkyblockPlayer player);

    ItemBuilder appendToLore(ItemBuilder builder);

    String toString();
}
