package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.WarpLocation;
import me.carscupcake.sbremake.worlds.impl.Dungeon;
import me.carscupcake.sbremake.worlds.impl.dungeon.*;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;

@DebugCommand
public class DungeonTest extends Command {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public DungeonTest() {
        super("dungeontest");
        setDefaultExecutor((sender, _) -> {
            genDungeon((SkyblockPlayer) sender, null, 6, 0);
        });
        var seedArgument = new ArgumentInteger("seed");
        addSyntax((sender, context) -> {
            genDungeon((SkyblockPlayer) sender, context.get(seedArgument), 6, 0);
        });
    }

    private String getDoorColor(DoorType door) {
        if (door == null) return ANSI_RESET;
        return switch (door) {
            case Bridge -> ANSI_CYAN;
            case Blood -> ANSI_RED;
            case Fairy -> ANSI_PURPLE;
            case Wither -> ANSI_BLACK;
            case Start -> ANSI_GREEN;
            case Normal -> ANSI_YELLOW;
            case None -> ANSI_RESET;
        };
    }

    private void genDungeon(SkyblockPlayer player, Integer seed, int size, int retryCount) {
        boolean retry = false;
        var generator = seed != null ? new Generator(new Room[size][size], seed) : new Generator(new Room[size][size]);
        try {
            generator.generateDoors(new Pos2d(generator.getFairy().pos().x(), generator.getFairy().pos().z()));
            generator.determineIds();
        } catch (DungeonException e) {
            e.printStackTrace(System.err);
            retry = true;
        }
        for (int x = 0; x < generator.getRooms().length; x++) {
            for (int y = generator.getRooms()[x].length - 1; y >= 0; y--) {
                var z = generator.getRooms()[x][y];
                if (z.pos().z() != generator.getRooms()[x].length - 1) {
                    if (generator.getDoorsVertical()[z.pos().x()][z.pos().z()] == DoorType.None) {
                        System.out.print("|");
                    } else {
                        var type = generator.getDoorsVertical()[z.pos().x()][z.pos().z()];
                        System.out.print(getDoorColor(type));
                        System.out.print("-");
                        System.out.print(ANSI_RESET);
                    }
                }
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
            }
            System.out.println();
            for (int y = generator.getRooms()[x].length - 1; y >= 0; y--) {
                var z = generator.getRooms()[x][y];
                if (z.pos().x() == size - 1) continue;
                System.out.print("-".repeat(4));
                if (generator.getDoorsHorizontal()[z.pos().x()][z.pos().z()] != DoorType.None) {
                    var type = generator.getDoorsHorizontal()[z.pos().x()][z.pos().z()];
                    System.out.print(getDoorColor(type));
                    System.out.print("|");
                    System.out.print(ANSI_RESET);
                } else System.out.print("-");
                System.out.print("-".repeat(4));
                if (z.pos().z() == size - 1) continue;
                System.out.print("-");
            }
            System.out.println();
        }
        if (retry) {
            if (retryCount >= 3) {
                player.sendMessage("Failed to generate dungeon after 3 retries");
                return;
            }
            player.sendMessage("Failed to generate dungeon, retrying in 0.5 seconds...");
            new TaskScheduler() {
                @Override
                public void run() {
                    genDungeon(player, seed, size, retryCount + 1);
                }
            }.delayTask(10);
            return;
        }
        var watch = System.currentTimeMillis();
        var dungeon = new Dungeon(generator);
        dungeon.init(() -> {
            synchronized (player) {
                player.setWorldProvider(dungeon, WarpLocation.Dungeon);
                var milies = System.currentTimeMillis() - watch;
                Main.LOGGER.debug("Dungeon init time: {}s", ((double) milies) / 1000d);
            }
        });
    }
}
