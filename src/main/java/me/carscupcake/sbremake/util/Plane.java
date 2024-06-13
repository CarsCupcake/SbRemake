package me.carscupcake.sbremake.util;

import net.minestom.server.coordinate.Vec;

public record Plane(Vec supportVec, Vec dirA, Vec dirB) {
    public Vec collidePoint(Line line) {
        return collidePoint(line, false);
    }

    public Vec collidePoint(Line line, boolean onlyInFront) {
        Vec normalVec = dirA.cross(dirB);
        //Parallel Check
        if (normalVec.dot(line.direction()) == 0) return null;
        double d = normalVec.x() * supportVec.x() + normalVec.y() * supportVec.y() + normalVec.z() * supportVec.z();
        double r = (line.supportVec().x() * normalVec.x() + line.supportVec().y() * normalVec.y() + line.supportVec().z() * normalVec.z() - d) /
                (- line.direction().x() * normalVec.x() - line.direction().y() * normalVec.y() - line.direction().z() * normalVec.z());
        if (onlyInFront && r < 0) return null;
        return line.point(r);
    }

    public static Plane from(Vec p1, Vec p2, Vec p3) {
        return new Plane(p1, p2.sub(p1), p3.sub(p1));
    }
}
