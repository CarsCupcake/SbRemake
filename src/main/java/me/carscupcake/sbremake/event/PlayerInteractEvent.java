package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PlayerInteractEvent(SkyblockPlayer player, @Nullable Point pos, @Nullable BlockFace face,
                                  @Nullable Entity entity) implements PlayerEvent {
    public PlayerInteractEvent(SkyblockPlayer player) {
        this(player, null, null);
    }

    public PlayerInteractEvent(SkyblockPlayer player, @Nullable Point pos, @Nullable BlockFace face) {
        this(player, pos, face, null);
    }

    public PlayerInteractEvent(SkyblockPlayer player, @Nullable Entity entity) {
        this(player, null, null, entity);
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
