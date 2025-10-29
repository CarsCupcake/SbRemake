package me.carscupcake.junit;

import me.carscupcake.sbremake.util.ComponentUtil;
import me.carscupcake.sbremake.util.TypeUtil;
import me.carscupcake.sbremake.worlds.region.CuboidRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.Range;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.*;
import java.util.*;

public class WeardBugTests {
    @ParameterizedTest
    @CsvSource({"-402.6499994036308,95.00000035545827,-518.3500006713642"})
    public void testBB(double x, double y, double z) {
        var aabb = new CuboidRegion(null, Set.of(new BoundingBox(new Vec(-432, 86, -533), new Vec(-384, 113, -483))));
        Assertions.assertTrue(aabb.isInRegion(new Pos(x, y, z)));
    }
}
