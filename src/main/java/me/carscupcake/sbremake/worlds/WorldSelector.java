package me.carscupcake.sbremake.worlds;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface WorldSelector {
    @Nullable
    default SkyblockWorld.WorldProvider getWorldProvider(List<SkyblockWorld.WorldProvider> providers, SkyblockPlayer player) {
        SkyblockWorld.WorldProvider provider = null;
        for (SkyblockWorld.WorldProvider p : providers) {
            if (p.getPlayers().size() > 14) continue;
            provider = p;
            break;
        }
        return provider;
    }
}
