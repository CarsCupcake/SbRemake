package me.carscupcake.sbremake.worlds.impl.dungeon;

import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.util.PalletItem;
import me.carscupcake.sbremake.util.Pos2d;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;

public class Paster {
    private final Instance instance;

    @SneakyThrows
    public Paster(Room[][] rooms, Instance instance) {
        this.instance = instance;
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
                    paste(room.pos(), room.rotation(), room.shape(), "dino-dig-site-4", room.type());
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
                        paste(room.pos(), room.rotation(), room.shape(), isHard ? "trap-very-hard-3" : "trap-hard-4", room.type());
                        Main.LOGGER.debug("Trap: {}/{}", i.addAndGet(1), total);
                        System.gc();
                    }));
                    continue;
                }

                if (room.type() == RoomType.Puzzle) {
                    final var puzzle = puzzles[new Random().nextInt(puzzles.length)];
                    threads.add(Thread.startVirtualThread(() -> {
                        paste(room.pos(), room.rotation(), room.shape(), puzzle, room.type());
                        Main.LOGGER.debug("Puzzle: {}/{}", i.addAndGet(1), total);
                        System.gc();
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
                    }, room.type());
                    Main.LOGGER.debug("{}/{}", i.addAndGet(1), total);
                    System.gc();
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

    public void paste(Pos2d pos2d, Rotation rotation, RoomShape shape, String id, RoomType type) {
        try {
            var path = "assets/shematics/dungeon/rooms/" + (type == RoomType.Room ?  shape.toString() : type.toString().toLowerCase()) + "/" + id;
            try (InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream(path)) {
                try (GZIPInputStream gzipOut = new GZIPInputStream(Objects.requireNonNull(resourceAsStream))) {
                    var obj = JsonParser.parseReader(new InputStreamReader(gzipOut)).getAsJsonObject();
                    var origin = rotation.ordinal() - Rotation.fromName(obj.get("originRotation").getAsString()).ordinal();
                    if (origin < 0) origin += 4;
                    var jsonPallete = obj.get("pallete").getAsJsonArray();
                    PalletItem[] blocks = new PalletItem[jsonPallete.size()];
                    for (int i = 0; i < blocks.length; i++) {
                        var object = jsonPallete.get(i).getAsJsonObject();
                        var map = new HashMap<String, String>();
                        for (var e : object.get("properties").getAsJsonArray()) {
                            var prop = e.getAsJsonObject();
                            var name = prop.get("name").getAsString();
                            var value = prop.get("value").getAsString().toLowerCase();
                            if (name.equals("rotation")) {
                                var intValue = Integer.parseInt(value);
                                value = String.valueOf((intValue + 4) % 16);
                            } else if (name.equals("facing")) {
                                var en = FacingDirection.fromId(value);
                                for (int j = 0; j < origin; j++) {
                                    en = en.next();
                                }
                                value = en.getId();
                            } else if (name.equals("axis") && !value.equals("y")) {
                                if (origin % 2 != 0) value = value.equals("x") ? "z" : "x";
                            } else {
                                if (prop.get("type").getAsString().equals("class_4778")) {
                                    var en = FacingDirection.fromId(name);
                                    for (int j = 0; j < origin; j++) {
                                        en = en.next();
                                    }
                                    name = en.getId();
                                } else if (prop.get("type").getAsString().equals("Boolean")) {
                                    var en = FacingDirection.fromId(name);
                                    if (en != null) {
                                        for (int j = 0; j < origin; j++) {
                                            en = en.next();
                                        }
                                        name = en.getId();
                                    }
                                }
                            }
                            map.put(name, value.toLowerCase());
                        }
                        blocks[i] = new PalletItem(Block.fromKey(Key.key(object.get("id").getAsString())), map, object.get("id").getAsString().equals("minecraft:player_head") ?
                                ((object.has("texture")) ? object.get("texture").getAsString() : null) : null);
                    }
                    var xArr = obj.get("blocks").getAsJsonArray();
                    for (var x = 0; x < xArr.size(); x++) {
                        var yArr = xArr.get(x).getAsJsonArray();
                        for (var y = 0; y < yArr.size(); y++) {
                            var zArr = yArr.get(y).getAsJsonArray();
                            for (var z = 0; z < zArr.size(); z++) {
                                var item = blocks[zArr.get(z).getAsInt()];
                                var block = item.block().withProperties(item.propertyMap());
                                if (item.headValue() != null) {
                                    var textures = CompoundBinaryTag.empty().putString("name", "textures").putString("value", item.headValue());
                                    var properties = ListBinaryTag.from(List.of(textures));
                                    var profile = CompoundBinaryTag.empty().putString("name", "CarsCupcake").put("properties", properties);
                                    block = block.withNbt(CompoundBinaryTag.empty().put("profile", profile));
                                }
                                var pos = rotation.toActual(pos2d, new Vec(x, y, z));
                                instance.setBlock(shape.withRotationOffset(pos, rotation), block, false);
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
}
