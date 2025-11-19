package me.carscupcake.sbremake.worlds;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface WorldSelector {
    @Nullable
    default WorldProvider getWorldProvider(List<WorldProvider> providers, SkyblockPlayer player) {
        WorldProvider provider = null;
        for (WorldProvider p : providers) {
            if (p.getPlayers().size() > 14) continue;
            provider = p;
            break;
        }
        return provider;
    }
}
