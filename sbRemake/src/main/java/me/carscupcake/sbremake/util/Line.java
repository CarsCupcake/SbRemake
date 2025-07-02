package me.carscupcake.sbremake.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;

public record Line(Vec supportVec, Vec direction) {
    public boolean collidesWithBB(BoundingBox bb) {
        return !Objects.requireNonNull(getCollidePoints(bb)).isEmpty();
    }

    public Set<Vec> getCollidePoints(BoundingBox bb) {
        return getCollidePoints(bb, false);
    }

    public Set<Vec> getCollidePoints(BoundingBox bb, boolean onlyInFront) {
        Vec minXYZ = new Vec(bb.minX(), bb.minY(), bb.minZ());
        Vec maxXYZ = new Vec(bb.maxX(), bb.maxY(), bb.maxZ());
        Set<Plane> plane = Set.of(new Plane(minXYZ, new Vec(0, 1, 0),
                new Vec(1, 0, 0)), new Plane(minXYZ, new Vec(0, 1, 0),
                new Vec(0, 0, 1)), new Plane(minXYZ, new Vec(0, 0, 1),
                new Vec(1, 0, 0)), new Plane(maxXYZ, new Vec(0, 1, 0),
                new Vec(1, 0, 0)), new Plane(maxXYZ, new Vec(0, 1, 0),
                new Vec(0, 0, 1)), new Plane(maxXYZ, new Vec(0, 0, 1),
                new Vec(1, 0, 0)));
        Set<Vec> points = new HashSet<>();
        for (Plane p : plane) {
            Vec vec = p.collidePoint(this, onlyInFront);
            if (vec == null) continue;
            if (vec.x() <= bb.maxX() && vec.x() >= bb.minX() && vec.y() <= bb.maxY() && vec.y() >= bb.minY() && vec.z() <= bb.maxZ() && vec.z() >= bb.minZ())
                points.add(vec);
        }
        return points;
    }

    public Vec point(double r) {
        return supportVec.add(direction.mul(r));
    }

    public static Line from(Entity entity) {
        return new Line(entity.getPosition().add(0, entity.getEyeHeight(), 0).asVec(), entity.getPosition().direction());
    }
}
