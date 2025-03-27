package me.carscupcake.junit;

import me.carscupcake.sbremake.util.Line;
import me.carscupcake.sbremake.util.Plane;
import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaneTests {
    @Test
    void planeLineSimple() {
        Vec v = new Plane(new Vec(0, 1, 0), new Vec(1, 0, 0), new Vec(0, 0, 1)).collidePoint(new Line(Vec.ZERO, new Vec(0, 1, 0)));
        Assertions.assertEquals(new Vec(0, 1, 0), v);
    }

    @Test
    void planeLineComplex() {
        Vec v = new Plane(new Vec(0, 1, 0), new Vec(1, 0, 0), new Vec(0, 0, 1)).collidePoint(new Line(Vec.ZERO, new Vec(1, 0.5, 0)));
        Assertions.assertEquals(new Vec(2, 1, 0), v);
    }
}
