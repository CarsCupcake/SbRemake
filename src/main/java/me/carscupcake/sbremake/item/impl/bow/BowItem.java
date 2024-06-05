package me.carscupcake.sbremake.item.impl.bow;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.Vec;

import java.util.ArrayList;
import java.util.List;

public interface BowItem {
    default int arrowsToShoot(SbItemStack stack) {
        return 1;
    }

    default void onBowShoot(SkyblockPlayer player, SbItemStack stack) {

    }

    static List<Vec> getShootVectors(Vec origin, int arrowsToShoot) {
        if (arrowsToShoot == 1) return List.of(origin);
        List<Vec> vs = new ArrayList<>();
        boolean b = (arrowsToShoot - 1) % 2 == 0;
        int loopCycles = (arrowsToShoot - ((b) ? 1 : 0)) / 2;
        for (int i = 0; i < loopCycles; i++) {
            double degree = (i + 1d) * 5;
            vs.add(origin.rotateAroundY(Math.toRadians(degree)));
            if (loopCycles - 1 != i)
                vs.add(origin.rotateAroundY(-Math.toRadians(degree)));
            else if (b)
                vs.add(origin.rotateAroundY(-Math.toRadians(degree)));
        }
        return vs;
    }
}
