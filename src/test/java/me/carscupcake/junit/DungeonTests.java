package me.carscupcake.junit;

import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.worlds.impl.dungeon.Generator;
import me.carscupcake.sbremake.worlds.impl.dungeon.Room;
import me.carscupcake.sbremake.worlds.impl.dungeon.RoomType;
import org.junit.Assert;
import org.junit.Test;

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

    @Test
    public void testDungeon() {
        var generation = new Generator(new Room[6][6], 0);
        for (var x : generation.getRooms()) {
            for (var z : x) {
                var s = z.shape().toString();
                var len = 7 - s.length();
                if (len / 2 != 0) System.out.print("-".repeat(len / 2));
                if (z.type() == RoomType.Blood) System.out.print(ANSI_RED);
                if (z.type() == RoomType.Entrance) System.out.print(ANSI_GREEN);
                if (z.type() == RoomType.Fairy)  System.out.print(ANSI_PURPLE);
                if (z.type() == RoomType.Trap) System.out.print(ANSI_WHITE);
                if (z.type() == RoomType.Mini)   System.out.print(ANSI_YELLOW);
                if (z.type() == RoomType.Puzzle)   System.out.print(ANSI_BLUE);
                System.out.print("[" + s + "]");
                if (z.type() != RoomType.Room) System.out.print(ANSI_RESET);
                if (len / 2 != 0) System.out.print("-".repeat(len / 2));
            }
            System.out.println();
        }
    }

    @Test
    public void testIsDeterministic() {
        var gen1  = new Generator(new Room[6][6], 1);
        var gen2  = new Generator(new Room[6][6], 1);
        Assert.assertArrayEquals(gen1.getRooms(), gen2.getRooms());
    }
}
