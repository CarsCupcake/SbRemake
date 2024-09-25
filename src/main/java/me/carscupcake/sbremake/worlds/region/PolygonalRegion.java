package me.carscupcake.sbremake.worlds.region;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;

public record PolygonalRegion(String name, Pos[] points, int highestY, int lowestY) implements Region {

    @Override
    public boolean isInRegion(Point pos) {
        if (pos.y() < lowestY || pos.y() > highestY) return false;
        double minX = points[0].x();
        double maxX = points[0].x();
        double minz = points[0].z();
        double maxz = points[0].z();
        for (int i = 1; i < points.length; i++) {
            Pos q = points[i];
            minX = Math.min(q.x(), minX);
            maxX = Math.max(q.x(), maxX);
            minz = Math.min(q.z(), minz);
            maxz = Math.max(q.z(), maxz);
        }
        if (pos.x() < minX || pos.x() > maxX || pos.z() < minz || pos.z() > maxz) return false;
        boolean inside = false;
        for (int i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].z() > pos.z()) != (points[j].z() > pos.z()) && pos.x() < (points[j].x() - points[i].x()) * (pos.z() - points[i].z()) / (points[j].z() - points[i].z()) + points[i].x()) {
                inside = !inside;
            }
        }
        return inside;
    }
}
