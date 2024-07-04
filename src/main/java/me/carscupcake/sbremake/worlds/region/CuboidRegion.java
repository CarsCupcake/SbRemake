package me.carscupcake.sbremake.worlds.region;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;

import java.util.Set;
import java.util.function.Consumer;

public record CuboidRegion(String name, Set<BoundingBox> cuboids, Consumer<SkyblockPlayer> onEnter,
                           Consumer<SkyblockPlayer> onLeave) implements Region {
    public CuboidRegion(String name, Set<BoundingBox> cuboids) {
        this(name, cuboids, null, null);
    }

    @Override
    public boolean isInRegion(Point pos) {
        for (BoundingBox boundingBox : cuboids)
            if (containsBB(boundingBox, pos)) return true;
        return false;
    }

    public static boolean containsBB(BoundingBox bb, Point point) {
        return bb.minX() < point.x() && bb.maxX() > point.x() && bb.minY() < point.y() && bb.maxY() > point.y() && bb.minZ() < point.z() && bb.maxZ() > point.z();
    }

    @Override
    public void onEnter(SkyblockPlayer player) {
        if (onEnter != null) onEnter.accept(player);
        Region.super.onEnter(player);
    }

    @Override
    public void onExit(SkyblockPlayer player) {
        if (onLeave != null) onLeave.accept(player);
        Region.super.onExit(player);
    }

}
