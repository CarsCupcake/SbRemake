package me.carscupcake.sbremake.command.testing;

import com.google.common.base.Stopwatch;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.worlds.WarpLocation;
import me.carscupcake.sbremake.worlds.impl.Dungeon;
import me.carscupcake.sbremake.worlds.impl.dungeon.*;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.IChunkLoader;
import net.minestom.server.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.ChronoUnit;
import java.util.List;

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
        setDefaultExecutor((sender, context) -> {
            var generator = new Generator(new Room[6][6]);
            generator.generateDoors(new Pos2d(generator.getFairy().pos().x(), generator.getFairy().pos().z()));
            for (int x = 0; x < generator.getRooms().length; x++) {
                for (int y = generator.getRooms()[x].length - 1; y >= 0; y--) {
                    var z =  generator.getRooms()[x][y];
                    if (z.pos().z() != generator.getRooms()[x].length - 1) {
                        if (generator.getDoorsVertical()[z.pos().x()][z.pos().z()] == null) {
                            System.out.print("|");
                        } else {
                            var type = generator.getDoorsVertical()[z.pos().x()][z.pos().z()];
                            if (type == DoorType.Wither || type == DoorType.Fairy) System.out.print(ANSI_BLACK);
                            if (type == DoorType.Bridge) System.out.print(ANSI_CYAN);
                            System.out.print("-");
                            if (type == DoorType.Wither || type == DoorType.Fairy || type == DoorType.Bridge) System.out.print(ANSI_RESET);
                        }
                    }
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
                for (int y = generator.getRooms()[x].length - 1; y >= 0; y--) {
                    var z =  generator.getRooms()[x][y];
                    if (z.pos().x() == 5) continue;
                    System.out.print("-".repeat(4));
                    if (generator.getDoorsHorizontal()[z.pos().x()][z.pos().z()] != null) {
                        var type = generator.getDoorsHorizontal()[z.pos().x()][z.pos().z()];
                        if (type == DoorType.Wither || type == DoorType.Fairy) System.out.print(ANSI_BLACK);
                        if (type == DoorType.Bridge) System.out.print(ANSI_CYAN);
                        System.out.print("|");
                        if (type == DoorType.Wither || type == DoorType.Fairy || type == DoorType.Bridge) System.out.print(ANSI_RESET);
                    } else System.out.print("-");
                    System.out.print("-".repeat(4));
                    if (z.pos().z() == 5) continue;
                    System.out.print("-");
                }
                System.out.println();
            }
            var watch = System.currentTimeMillis();
            var dungeon = new Dungeon(generator);
            var player = ((SkyblockPlayer) sender);
            dungeon.init(() -> {
                synchronized (player) {
                    player.setWorldProvider(dungeon, WarpLocation.Dungeon);
                    var milies = System.currentTimeMillis() - watch;
                    Main.LOGGER.debug("Dungeon init time: {}s", ((double) milies) / 1000d);
                }
            });
        });
    }
}
