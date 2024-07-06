package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PlayerInteractEvent(SkyblockPlayer player, @Nullable Point block, @Nullable BlockFace face,
                                  @Nullable Entity entity, Interaction interaction) implements PlayerEvent {
    public PlayerInteractEvent(SkyblockPlayer player, Interaction interaction) {
        this(player, null, null, interaction);
    }

    public PlayerInteractEvent(SkyblockPlayer player, @Nullable Point pos, @Nullable BlockFace face, Interaction interaction) {
        this(player, pos, face, null, interaction);
    }

    public PlayerInteractEvent(SkyblockPlayer player, @Nullable Entity entity, Interaction interaction) {
        this(player, null, null, entity, interaction);
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    public enum Interaction {
        Right,
        Left
    }
}
