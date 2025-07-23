package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.worlds.impl.Dungeon;
import me.carscupcake.sbremake.worlds.impl.dungeon.Generator;
import me.carscupcake.sbremake.worlds.impl.dungeon.Paster;
import me.carscupcake.sbremake.worlds.impl.dungeon.Room;
import me.carscupcake.sbremake.worlds.impl.dungeon.RoomType;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            var dungeon = new Dungeon(generator);
            dungeon.init(() -> ((SkyblockPlayer) sender).setWorldProvider(dungeon));
            for (int x = 0; x < generator.getRooms().length; x++) {
                for (int y = generator.getRooms()[x].length - 1; y >= 0; y--) {
                    var z =  generator.getRooms()[x][y];
                    if (z.parent() == null){
                        var entity = new LivingEntity(EntityType.SHULKER);
                        entity.setInvisible(true);
                        entity.setNoGravity(true);
                        entity.setGlowing(true);
                        entity.setGlowColor(NamedTextColor.AQUA);
                        entity.setInstance(dungeon.container, new Pos2d(x, y).asPos().add(0, 140, 0));
                    }
                    else {
                        var entity = new LivingEntity(EntityType.SHULKER);
                        entity.setInvisible(true);
                        entity.setNoGravity(true);
                        entity.setGlowing(true);
                        entity.setGlowColor(NamedTextColor.GRAY);
                        entity.setInstance(dungeon.container, new Pos2d(x, y).asPos().add(0, 140, 0));
                    }
                    var s = z.shape().toString();
                    var len = 7 - s.length();
                    if (len / 2 != 0) System.out.print("  ".repeat(len / 2));
                    if (z.type() == RoomType.Blood) System.out.print(ANSI_RED);
                    if (z.type() == RoomType.Entrance) System.out.print(ANSI_GREEN);
                    if (z.type() == RoomType.Fairy)  System.out.print(ANSI_PURPLE);
                    if (z.type() == RoomType.Trap) System.out.print(ANSI_WHITE);
                    if (z.type() == RoomType.Mini)   System.out.print(ANSI_YELLOW);
                    if (z.type() == RoomType.Room && z.parent() == null) System.out.print(ANSI_BLUE);
                    System.out.print("[" + s + "]");
                    if (len / 2 != 0) System.out.print(" ".repeat(len / 2));
                    if (z.type() != RoomType.Room) System.out.print(ANSI_RESET);
                    if (z.type() == RoomType.Room && z.parent() == null) System.out.print(ANSI_RESET);
                }
                System.out.println();
            }
        });
    }
}
