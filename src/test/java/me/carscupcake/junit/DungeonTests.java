package me.carscupcake.junit;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.util.SkyblockSimpleLogger;
import me.carscupcake.sbremake.worlds.impl.dungeon.*;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Marker;
import org.slf4j.event.Level;

public class DungeonTests {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    @BeforeClass
    public static void oneTimeSetup() {
        Main.LOGGER = new SkyblockSimpleLogger() {
            @Override
            protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {
                System.out.printf((messagePattern) + "%n", arguments);
            }
        };
    }

    @Test
    public void testDungeon() {
        var generation = new Generator(new Room[6][6]);
        generation.generateDoors(new Pos2d(generation.getFairy().pos().x(), generation.getFairy().pos().z()));
        for (var x : generation.getRooms()) {
            for (var z : x) {
                var s = z.shape().toString();
                var len = 7 - s.length();
                if (len / 2 != 0) System.out.print("-".repeat(len / 2));
                if (z.type() == RoomType.Blood) System.out.print(ANSI_RED);
                if (z.type() == RoomType.Entrance) System.out.print(ANSI_GREEN);
                if (z.type() == RoomType.Fairy) System.out.print(ANSI_PURPLE);
                if (z.type() == RoomType.Trap) System.out.print(ANSI_WHITE);
                if (z.type() == RoomType.Mini) System.out.print(ANSI_YELLOW);
                if (z.type() == RoomType.Puzzle) System.out.print(ANSI_BLUE);
                System.out.print("[" + s + "]");
                if (z.type() != RoomType.Room) System.out.print(ANSI_RESET);
                if (len / 2 != 0) System.out.print("-".repeat(len / 2));
                if (z.pos().z() == 5) continue;
                if (generation.getDoorsVertical()[z.pos().x()][z.pos().z()] == null) {
                    System.out.print("|");
                } else {
                    var type = generation.getDoorsVertical()[z.pos().x()][z.pos().z()];
                    if (type == DoorType.Wither || type == DoorType.Fairy) System.out.print(ANSI_BLACK);
                    System.out.print("-");
                    if (type == DoorType.Wither || type == DoorType.Fairy) System.out.print(ANSI_RESET);
                }
            }

            System.out.println();
            for (var z : x) {
                if (z.pos().x() == 5) continue;
                System.out.print("-".repeat(4));
                if (generation.getDoorsHorizontal()[z.pos().x()][z.pos().z()] != null) {
                    var type = generation.getDoorsHorizontal()[z.pos().x()][z.pos().z()];
                    if (type == DoorType.Wither || type == DoorType.Fairy) System.out.print(ANSI_BLACK);
                    System.out.print("|");
                    if (type == DoorType.Wither || type == DoorType.Fairy) System.out.print(ANSI_RESET);
                } else System.out.print("-");
                System.out.print("-".repeat(4));
                if (z.pos().z() == 5) continue;
                System.out.print("-");
            }
            System.out.println();
        }
    }

    @Test
    public void testIsDeterministic() {
        var gen1 = new Generator(new Room[6][6], 1);
        var gen2 = new Generator(new Room[6][6], 1);
        Assert.assertArrayEquals(gen1.getRooms(), gen2.getRooms());
    }

    @Test
    public void testRelativeCalculation() {
        for (int x = 0; x < 20; x++)
            for (int y = 0; y < 20; y++)
                for (int z = 0; z < 20; z++) {
                    for (int mapX = 0; mapX < 6; mapX++) {
                        for (int mapZ = 0; mapZ < 6; mapZ++)
                            for (var shape : RoomShape.values()) {
                                for (var dir : Rotation.values()) {
                                    var pos = new Vec(x, y, z);
                                    Assert.assertEquals(pos, shape.toRelative(new Pos2d(1, 2), shape.toActual(new Pos2d(1, 2), pos, dir), dir));
                                }
                            }
                    }
                }
    }

    @Test
    public void testDirectionCalculations() {
        for (int x = 0; x < 20; x++)
            for (int y = 0; y < 20; y++)
                for (int z = 0; z < 20; z++) {
                    for (var dir : Rotation.values()) {
                        var pos = new Vec(x, y, z);
                        Assert.assertEquals(pos, dir.toRelative(new Pos2d(1, 2), dir.toActual(new Pos2d(1, 2), pos)));
                    }
                }
    }


}
