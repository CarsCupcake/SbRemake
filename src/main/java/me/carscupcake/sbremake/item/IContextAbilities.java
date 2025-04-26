package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface IContextAbilities {
    Collection<Ability> getAbilities(@Nullable SkyblockPlayer player, @NotNull SbItemStack item);
}
