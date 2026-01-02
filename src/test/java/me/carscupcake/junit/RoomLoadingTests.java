package me.carscupcake.junit;

import me.carscupcake.sbremake.worlds.impl.dungeon.Generator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.ArrayList;
import java.util.stream.Stream;

public class RoomLoadingTests {
    @ParameterizedTest
    @ArgumentsSource(BooleanCombinations.class)
    public void testRoomDoorwaysPassable(boolean north,
                                         boolean east,
                                         boolean south,
                                         boolean west) {
        Assertions.assertTrue(new Generator.DoorwaysModel(true, true, true, true).passable(new Generator.DoorwaysModel(north, east, south, west)));
    }

    @Test
    public void testRoomDoorwaysRotation() {
        var doorways = new Generator.DoorwaysModel(true, false, true, false);
        doorways.rotate();
        Assertions.assertFalse(doorways.north);
        Assertions.assertTrue(doorways.east);
        Assertions.assertFalse(doorways.south);
        Assertions.assertTrue(doorways.west);
        doorways.rotate();
        Assertions.assertTrue(doorways.north);
        Assertions.assertFalse(doorways.east);
        Assertions.assertTrue(doorways.south);
        Assertions.assertFalse(doorways.west);
    }

    @Test
    public void testNotFitting() {
        Assertions.assertFalse(new Generator.DoorwaysModel(true, false, false, false).passable(new Generator.DoorwaysModel(true, false, true, false)));
    }

    static class BooleanCombinations implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            var args = new ArrayList<Arguments>();
            for (int i = 0; i < 0b10000; i++) {
                args.add(Arguments.of(
                        (i & 1) != 0,
                        (i & 2) != 0,
                        (i & 4) != 0,
                        (i & 8) != 0
                ));
            }
            return args.stream();
        }
    }
}
