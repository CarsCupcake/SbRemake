package me.carscupcake.sbremake.worlds.impl.dungeon;

import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.util.Pos2d;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

public class Paster {

    @SneakyThrows
    public Paster(Room[][] rooms, Instance instance) {
        var threads = new ArrayList<Thread>();
        AtomicInteger i = new AtomicInteger(0);
        if (rooms.length == 0) return;
        final int total = rooms.length * rooms[0].length;
        for (Room[] r : rooms) {
            for (Room room : r) {
                if (room.parent() != null || room.shape() != RoomShape.L_SHAPE) {
                    continue;
                }
                threads.add(Thread.startVirtualThread(() -> {
                    paste(room.pos(), room.rotation(), room.shape(), "dino-dig-site-4", room.type(), instance);
                    Main.LOGGER.debug("{}/{}", i.addAndGet(1), total);

                }));
            }
        }
        for (var t : threads)
            try {
                t.join();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        threads.clear();
        System.gc();
        String[] puzzles = {"blaze-room-1-high", "boxes-room", "ice-path", "ice-silverfish-room", "three-chests", "trivia-room", "water-puzzle"};
        for (Room[] r : rooms) {
            for (Room room : r) {
                if (room.parent() != null || room.shape() == RoomShape.L_SHAPE) {
                    i.addAndGet(1);
                    continue;
                }

                if (room.type() == RoomType.Trap) {
                    final var isHard = new Random().nextBoolean();
                    threads.add(Thread.startVirtualThread(() -> {
                        paste(room.pos(), room.rotation(), room.shape(), isHard ? "trap-very-hard-3" : "trap-hard-4", room.type(), instance);
                        Main.LOGGER.debug("Trap: {}/{}", i.addAndGet(1), total);
                        //System.gc();
                    }));
                    continue;
                }

                if (room.type() == RoomType.Puzzle) {
                    final var puzzle = puzzles[new Random().nextInt(puzzles.length)];
                    threads.add(Thread.startVirtualThread(() -> {
                        paste(room.pos(), room.rotation(), room.shape(), puzzle, room.type(), instance);
                        Main.LOGGER.debug("Puzzle: {}/{}", i.addAndGet(1), total);
                        //System.gc();
                    }));
                    continue;
                }

                if (room.type() != RoomType.Room) {
                    i.addAndGet(1);
                    continue;
                }
                threads.add(Thread.startVirtualThread(() -> {
                    paste(room.pos(), room.rotation(), room.shape(), switch (room.shape()) {
                        case ONE_BY_ONE -> "overgrown-3";
                        case ONE_BY_TWO -> "pedestal-5";
                        case ONE_BY_THREE -> "wizard-4";
                        case ONE_BY_FOUR -> "mossy-4";
                        case TWO_BY_TWO -> "mithril-cave-10";
                        default -> throw new IllegalStateException("Unexpected value: " + room.shape());
                    }, room.type(), instance);
                    Main.LOGGER.debug("{}/{}", i.addAndGet(1), total);
                    //System.gc();
                }));
            }
        }
        for (var t : threads)
            try {
                t.join();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        System.out.println("Done Pasting!");
        System.gc();
    }

    public static void paste(Pos2d pos2d, Rotation rotation, RoomShape shape, String id, RoomType type, Instance instance) {
        try {
            var path = "assets/schematics/dungeon/rooms/" + (type.isSpecial() ? type.name().toLowerCase(Locale.ENGLISH)
                    : (type == RoomType.Room ?  shape.toString() : type.toString().toLowerCase()) + "/" + id);
            try (InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream(path)) {
                try (GZIPInputStream gzipOut = new GZIPInputStream(Objects.requireNonNull(resourceAsStream))) {
                    var obj = JsonParser.parseReader(new InputStreamReader(gzipOut)).getAsJsonObject();
                    var origin = rotation.ordinal() - Rotation.fromName(obj.get("originRotation").getAsString()).ordinal();
                    if (origin < 0) origin += 4;
                    var jsonPallete = obj.get("pallete").getAsJsonArray();
                    Block[] blocks = new Block[jsonPallete.size()];
                    DungeonWorldProvider.loadPalette(origin, jsonPallete, blocks);
                    var xArr = obj.get("blocks").getAsJsonArray();
                    for (var x = 0; x < xArr.size(); x++) {
                        var yArr = xArr.get(x).getAsJsonArray();
                        for (var y = 0; y < yArr.size(); y++) {
                            var zArr = yArr.get(y).getAsJsonArray();
                            for (var z = 0; z < zArr.size(); z++) {
                                instance.setBlock(shape.toActual(pos2d, new Vec(x, y, z), rotation), blocks[zArr.get(z).getAsInt()], false);
                            }
                        }
                    }
                /*var entities = obj.get("entities").getAsJsonArray();
                System.out.println("entities: " + entities.size());
                for (var entity : entities) {
                    var o = entity.getAsJsonObject();
                    var test = new LivingEntity(EntityType.ARMOR_STAND);
                    test.set(DataComponents.CUSTOM_NAME, Component.text(o.get("type").getAsString() + " " + o.get("starred").getAsBoolean()));
                    test.setCustomNameVisible(true);
                    test.setNoGravity(true);
                    var p = o.get("pos").getAsJsonObject();
                    test.setInstance(instance, new Pos(p.get("x").getAsInt(), p.get("y").getAsInt(), p.get("z").getAsInt()));
                }*/
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void paste(Pos2d pos2d, String path, Instance instance) {
        try {
            try (InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream(path)) {
                try (GZIPInputStream gzipOut = new GZIPInputStream(Objects.requireNonNull(resourceAsStream))) {
                    var obj = JsonParser.parseReader(new InputStreamReader(gzipOut)).getAsJsonObject();
                    var jsonPallete = obj.get("pallete").getAsJsonArray();
                    Block[] blocks = new Block[jsonPallete.size()];
                    DungeonWorldProvider.loadPalette(0, jsonPallete, blocks);
                    var xArr = obj.get("blocks").getAsJsonArray();
                    for (var x = 0; x < xArr.size(); x++) {
                        var yArr = xArr.get(x).getAsJsonArray();
                        for (var y = 0; y < yArr.size(); y++) {
                            var zArr = yArr.get(y).getAsJsonArray();
                            for (var z = 0; z < zArr.size(); z++) {
                                instance.setBlock(new Vec(pos2d.x() * 32 + x, y, pos2d.z() * 32 + z), blocks[zArr.get(z).getAsInt()], false);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
