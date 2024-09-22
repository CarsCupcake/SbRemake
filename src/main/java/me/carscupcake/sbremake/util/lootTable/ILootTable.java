package me.carscupcake.sbremake.util.lootTable;

import me.carscupcake.sbremake.player.SkyblockPlayer;

import java.util.Set;

public interface ILootTable<T> {
    Set<T> loot(SkyblockPlayer player);
}
