package me.carscupcake.sbremake.worlds.impl.dungeon;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.carscupcake.sbremake.Main;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.IChunkLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public record DungeonWorldProvider(Generator generator, String[][] ids, Chunk[][] chunks,
                                   Block[][][] cachedPallets) implements IChunkLoader {
    public DungeonWorldProvider(Generator generator, String[][] ids) {
        this(generator, ids, new Chunk[generator.getRooms().length * 2][generator.getRooms()[0].length * 2], new Block[generator.getRooms().length][generator.getRooms()[0].length][]);
    }

    public static void loadPalette(int origin, JsonArray jsonPalette, Block[] blocks) {
        for (int i = 0; i < blocks.length; i++) {
            var object = jsonPalette.get(i).getAsJsonObject();
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
            var b = Block.fromKey(Key.key(object.get("id").getAsString())).withProperties(map);

            if (object.has("texture")) {
                var textures = CompoundBinaryTag.empty().putString("name", "textures").putString("value", object.get("texture").getAsString());
                var properties = ListBinaryTag.from(List.of(textures));
                var profile = CompoundBinaryTag.empty().putString("name", "CarsCupcake").put("properties", properties);
                b = b.withNbt(CompoundBinaryTag.empty().put("profile", profile));
            }
            blocks[i] = b;
        }
    }

    @Override
    public @Nullable Chunk loadChunk(@NotNull Instance instance, int chunkX, int chunkZ) {
        Room[][] rooms = generator.getRooms();
        if (chunkX > rooms.length * 2 || chunkZ > rooms[0].length * 2) return null;
        if (chunkX < 0 || chunkZ < 0) return null;
        if (chunks.length <= chunkX || chunks.length <= chunkZ) return null;
        if (chunks[chunkX][chunkZ] != null) {
            System.out.println("Load Existing " + chunkX + " " + chunkZ);
            return chunks[chunkX][chunkZ];
        }
        var chunk = instance.getChunkSupplier().createChunk(instance, chunkX, chunkZ);
        var room = rooms[chunkX / 2][chunkZ / 2];
        if (room == null) return null;
        if (room.parent() != null) room = generator.getFromPos(room.parent());
        var id = ids[room.pos().x()][room.pos().z()];
        if (id == null) {
            //id = room.shape().getSchematics().get()[generator.getRandom().nextInt(room.shape().getSchematics().get().length)];
            if (room.type() != RoomType.Room) {
                id = switch (room.type()) {
                    case Trap -> "trap-hard-4";
                    case Puzzle -> "blaze-room-1-high";
                    default -> null;
                };
            }


            if (id == null) return null;
            ids[room.pos().x()][room.pos().z()] = id;
        }
        try {
            var path = "assets/shematics/dungeon/rooms/" +(room.type().isSpecial() ? room.type().name().toLowerCase(Locale.ENGLISH)
                    : (room.type() == RoomType.Room ? room.shape().toString() : room.type().toString().toLowerCase()) + "/" + id);
            try (InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream(path)) {
                if (resourceAsStream != null)
                    try (GZIPInputStream gzipOut = new GZIPInputStream(Objects.requireNonNull(resourceAsStream))) {
                        var obj = JsonParser.parseReader(new InputStreamReader(gzipOut)).getAsJsonObject();
                        var origin = room.rotation().ordinal() - Rotation.fromName(obj.get("originRotation").getAsString()).ordinal();
                        if (origin < 0) origin += 4;
                        var jsonPallete = obj.get("pallete").getAsJsonArray();
                        Block[] blocks = cachedPallets[room.pos().x()][room.pos().z()];
                        if (blocks == null) {
                            blocks = new Block[jsonPallete.size()];
                            loadPalette(origin, jsonPallete, blocks);
                            if (room.shape() != RoomShape.ONE_BY_ONE) {
                                cachedPallets[room.pos().x()][room.pos().z()] = blocks;
                            }
                        }
                        var xArr = obj.get("blocks").getAsJsonArray();
                        for (int x = 0; x < 16; x++) {
                            for (int y = 0; y < 140; y++) {
                                for (int z = 0; z < 16; z++) {
                                    var pos = new Vec(x + (chunkX * 16), y, z + (chunkZ * 16));
                                    var undoRotation = room.shape().toRelative(room.pos(), pos, room.rotation());
                                    if (xArr.size() <= undoRotation.blockX() || undoRotation.blockX() < 0) {
                                        continue;
                                    }
                                    var yArr = xArr.get(undoRotation.blockX()).getAsJsonArray();
                                    if (yArr.size() <= undoRotation.blockY() || undoRotation.blockY() < 0) {
                                        continue;
                                    }
                                    var zArr = yArr.get(undoRotation.blockY()).getAsJsonArray();
                                    if (zArr.size() <= undoRotation.blockZ() || undoRotation.blockZ() < 0) {
                                        continue;
                                    }
                                    var block = blocks[zArr.get(undoRotation.blockZ()).getAsInt()];
                                    if (block.isAir()) {
                                        continue;
                                    }
                                    chunk.setBlock(x, y, z, block);
                                }
                            }
                        }

                        //Boarders
                        if (chunkX == 0) {
                            if (chunkZ % 2 == 0) {
                                setIfAir(chunk, 0, 69, 14);
                                setIfAir(chunk, 0, 69, 15);
                                setIfAir(chunk, 0, 70, 14);
                                setIfAir(chunk, 0, 70, 15);
                                setIfAir(chunk, 0, 71, 14);
                                setIfAir(chunk, 0, 71, 15);
                                setIfAir(chunk, 0, 72, 14);
                                setIfAir(chunk, 0, 72, 15);
                            } else {
                                setIfAir(chunk, 0, 69, 0);
                                setIfAir(chunk, 0, 70, 0);
                                setIfAir(chunk, 0, 71, 0);
                                setIfAir(chunk, 0, 72, 0);
                            }
                        }
                        if ((chunkX + 1) == rooms.length * 2) {
                            if (chunkZ % 2 == 0) {
                                setIfAir(chunk, 14, 69, 14);
                                setIfAir(chunk, 14, 69, 15);
                                setIfAir(chunk, 14, 70, 14);
                                setIfAir(chunk, 14, 70, 15);
                                setIfAir(chunk, 14, 71, 14);
                                setIfAir(chunk, 14, 71, 15);
                                setIfAir(chunk, 14, 72, 14);
                                setIfAir(chunk, 14, 72, 15);
                            } else {
                                setIfAir(chunk, 14, 69, 0);
                                setIfAir(chunk, 14, 70, 0);
                                setIfAir(chunk, 14, 71, 0);
                                setIfAir(chunk, 14, 72, 0);
                            }
                        }
                        if (chunkZ == 0) {
                            if (chunkX % 2 == 0) {
                                setIfAir(chunk, 14, 69, 0);
                                setIfAir(chunk, 15, 69, 0);
                                setIfAir(chunk, 14, 70, 0);
                                setIfAir(chunk, 15, 70, 0);
                                setIfAir(chunk, 14, 71, 0);
                                setIfAir(chunk, 15, 71, 0);
                                setIfAir(chunk, 14, 72, 0);
                                setIfAir(chunk, 15, 72, 0);
                            } else {
                                setIfAir(chunk, 0, 69, 0);
                                setIfAir(chunk, 0, 70, 0);
                                setIfAir(chunk, 0, 71, 0);
                                setIfAir(chunk, 0, 72, 0);
                            }
                        }
                        if (chunkZ + 1 == rooms[0].length * 2) {
                            if (chunkX % 2 == 0) {
                                setIfAir(chunk, 14, 69, 14);
                                setIfAir(chunk, 15, 69, 14);
                                setIfAir(chunk, 14, 70, 14);
                                setIfAir(chunk, 15, 70, 14);
                                setIfAir(chunk, 14, 71, 14);
                                setIfAir(chunk, 15, 71, 14);
                                setIfAir(chunk, 14, 72, 14);
                                setIfAir(chunk, 15, 72, 14);
                            } else {
                                setIfAir(chunk, 0, 69, 14);
                                setIfAir(chunk, 0, 70, 14);
                                setIfAir(chunk, 0, 71, 14);
                                setIfAir(chunk, 0, 72, 14);
                            }
                        }

                        //North Doors
                        if (chunkZ % 2 == 0 && room.pos().x() != 0) {
                            var northDoor = generator.getDoorsHorizontal()[room.pos().x() - 1][room.pos().z()];
                            if (northDoor != DoorType.Bridge) {
                                var block = getBlockFromDoor(northDoor);
                                if (chunkX % 2 == 0) {
                                    setIfAir(chunk, block, 14, 69, 0);
                                    setIfAir(chunk, block, 15, 69, 0);
                                    setIfAir(chunk, block, 14, 70, 0);
                                    setIfAir(chunk, block, 15, 70, 0);
                                    setIfAir(chunk, block, 14, 71, 0);
                                    setIfAir(chunk, block, 15, 71, 0);
                                    setIfAir(chunk, block, 14, 72, 0);
                                    setIfAir(chunk, block, 15, 72, 0);
                                } else {
                                    setIfAir(chunk, block, 0, 69, 0);
                                    setIfAir(chunk, block, 0, 70, 0);
                                    setIfAir(chunk, block, 0, 71, 0);
                                    setIfAir(chunk, block, 0, 72, 0);
                                }
                            }
                        }

                        //East Doors
                        if (chunkX % 2 != 0 && room.pos().z() != rooms[0].length - 1) {
                            var eastDoor = generator.getDoorsVertical()[room.pos().x()][room.pos().z()];
                            if (eastDoor != DoorType.Bridge) {
                                var block = getBlockFromDoor(eastDoor);
                                if (chunkZ % 2 == 0) {
                                    for (var x = 14; x <= 15; x++) {
                                        for (var y = 68; y <= 73; y++) {
                                            for (var z = 13; z <= 15; z++) {
                                                setIfAir(chunk, block, x, y, z);
                                            }
                                        }
                                    }
                                } else {
                                    setIfAir(chunk, block, 14, 69, 0);
                                    setIfAir(chunk, block, 14, 70, 0);
                                    setIfAir(chunk, block, 14, 71, 0);
                                    setIfAir(chunk, block, 14, 72, 0);


                                    setIfAir(chunk, block, 15, 68, 0);
                                    setIfAir(chunk, block, 15, 69, 0);
                                    setIfAir(chunk, block, 15, 70, 0);
                                    setIfAir(chunk, block, 15, 71, 0);
                                    setIfAir(chunk, block, 15, 72, 0);
                                    setIfAir(chunk, block, 15, 73, 0);


                                    setIfAir(chunk, block, 15, 69, 1);
                                    setIfAir(chunk, block, 15, 70, 1);
                                    setIfAir(chunk, block, 15, 71, 1);
                                    setIfAir(chunk, block, 15, 72, 1);
                                }
                            }
                            if (eastDoor == DoorType.None) {
                                //TODO Fill with air to make passage
                                if (chunkZ % 2 == 0) {
                                    setIfAir(chunk, 15, 69, 13);
                                    setIfAir(chunk, 15, 70, 13);
                                    setIfAir(chunk, 15, 71, 13);
                                    setIfAir(chunk, 15, 72, 13);

                                    setIfAir(chunk, 15, 68, 14);
                                    setIfAir(chunk, 15, 73, 14);

                                    setIfAir(chunk, 15, 68, 15);
                                    setIfAir(chunk, 15, 73, 15);

                                } else {
                                    setIfAir(chunk, 15, 68, 0);
                                    setIfAir(chunk, 15, 73, 0);

                                    setIfAir(chunk, 15, 69, 1);
                                    setIfAir(chunk, 15, 70, 1);
                                    setIfAir(chunk, 15, 71, 1);
                                    setIfAir(chunk, 15, 72, 1);
                                }
                            }
                        }

                        //South Doors
                        if (chunkZ % 2 != 0 && room.pos().x() != rooms.length - 1) {
                            var southDoor = generator.getDoorsHorizontal()[room.pos().x()][room.pos().z()];
                            if (southDoor != DoorType.Bridge) {
                                var block = getBlockFromDoor(southDoor);
                                if (chunkX % 2 == 0) {
                                    for (var x = 13; x <= 15; x++) {
                                        for (var y = 68; y <= 73; y++) {
                                            for (var z = 14; z <= 15; z++) {
                                                setIfAir(chunk, block, x, y, z);
                                            }
                                        }
                                    }
                                } else {
                                    setIfAir(chunk, block, 0, 69, 14);
                                    setIfAir(chunk, block, 0, 70, 14);
                                    setIfAir(chunk, block, 0, 71, 14);
                                    setIfAir(chunk, block, 0, 72, 14);


                                    setIfAir(chunk, block, 0, 68, 15);
                                    setIfAir(chunk, block, 1, 69, 15);
                                    setIfAir(chunk, block, 1, 70, 15);
                                    setIfAir(chunk, block, 1, 71, 15);
                                    setIfAir(chunk, block, 1, 72, 15);
                                    setIfAir(chunk, block, 0, 73, 15);


                                    setIfAir(chunk, block, 0, 69, 15);
                                    setIfAir(chunk, block, 0, 70, 15);
                                    setIfAir(chunk, block, 0, 71, 15);
                                    setIfAir(chunk, block, 0, 72, 15);
                                }
                            }
                            if (southDoor == DoorType.None) {
                                //TODO Fill with air to make passage
                                if (chunkX % 2 == 0) {
                                    setIfAir(chunk, 13, 69, 15);
                                    setIfAir(chunk, 13, 70, 15);
                                    setIfAir(chunk, 13, 71, 15);
                                    setIfAir(chunk, 13, 72, 15);

                                    setIfAir(chunk, 14, 68, 15);
                                    setIfAir(chunk, 14, 73, 15);

                                    setIfAir(chunk, 15, 68, 15);
                                    setIfAir(chunk, 15, 73, 15);

                                } else {
                                    setIfAir(chunk, 0, 68, 15);
                                    setIfAir(chunk, 0, 73, 15);

                                    setIfAir(chunk, 1, 69, 15);
                                    setIfAir(chunk, 1, 70, 15);
                                    setIfAir(chunk, 1, 71, 15);
                                    setIfAir(chunk, 1, 72, 15);
                                }
                            }
                        }

                        //West Doors
                        if (chunkX % 2 == 0 && room.pos().z() != 0) {
                            var westDoor = generator.getDoorsVertical()[room.pos().x()][room.pos().z() - 1];
                            if (westDoor != DoorType.Bridge) {
                                var block = getBlockFromDoor(westDoor);
                                if (chunkZ % 2 == 0) {
                                    setIfAir(chunk, block, 0, 69, 14);
                                    setIfAir(chunk, block, 0, 69, 15);
                                    setIfAir(chunk, block, 0, 70, 14);
                                    setIfAir(chunk, block, 0, 70, 15);
                                    setIfAir(chunk, block, 0, 71, 14);
                                    setIfAir(chunk, block, 0, 71, 15);
                                    setIfAir(chunk, block, 0, 72, 14);
                                    setIfAir(chunk, block, 0, 72, 15);
                                } else {
                                    setIfAir(chunk, block, 0, 69, 0);
                                    setIfAir(chunk, block, 0, 70, 0);
                                    setIfAir(chunk, block, 0, 71, 0);
                                    setIfAir(chunk, block, 0, 72, 0);
                                }
                            }
                        }
                    }
            }

        } catch (Exception e) {
            Main.LOGGER.error("Failed to load room with id  {}", id);
            throw new RuntimeException(e);
        }
        chunks[chunkX][chunkZ] = chunk;
        return chunk;
    }

    private void setIfAir(Chunk chunk, int x, int y, int z) {
        var block = chunk.getBlock(x, y, z);
        if (block.isAir() || block == Block.COAL_BLOCK || block == Block.RED_TERRACOTTA) {
            chunk.setBlock(x, y, z, Block.STONE);
        }
    }

    private void setIfAir(Chunk chunk, Block block, int x, int y, int z) {
        var b = chunk.getBlock(x, y, z);
        if (b.isAir() || b == Block.COAL_BLOCK || b == Block.RED_TERRACOTTA) {
            chunk.setBlock(x, y, z, block);
        }
    }

    private Block getBlockFromDoor(DoorType door) {
        return switch (door) {
            case Normal -> Block.STONE;
            case Blood -> Block.RED_TERRACOTTA;
            case Wither -> Block.COAL_BLOCK;
            case Start -> Block.CHISELED_STONE_BRICKS;
            default -> Block.AIR;
        };
    }

    @Override
    public void saveChunk(@NotNull Chunk chunk) {
        //NOOP
    }
}
