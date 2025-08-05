package me.carscupcake.sbremake.worlds.impl.dungeon;

import com.google.gson.JsonParser;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.util.Lazy;
import me.carscupcake.sbremake.util.Pos2d;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ItemEntity;
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
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public record DungeonWorldProvider(Generator generator, String[][] ids, Chunk[][] chunks) implements IChunkLoader {
    public DungeonWorldProvider(Generator generator, String[][] ids) {
        this(generator, ids, new Chunk[generator.getRooms().length * 2][generator.getRooms()[0].length * 2]);
    }

    @Override
    public @Nullable Chunk loadChunk(@NotNull Instance instance, int chunkX, int chunkZ) {
        if (chunkX > generator.getRooms().length * 2 || chunkZ > generator.getRooms()[0].length * 2) return null;
        if (chunkX < 0 || chunkZ < 0) return null;
        if (chunks.length <= chunkX || chunks.length <= chunkZ) return null;
        if (chunks[chunkX][chunkZ] != null) {
            System.out.println("Load Existing " + chunkX + " " + chunkZ);
            return chunks[chunkX][chunkZ];
        }
        var chunk = instance.getChunkSupplier().createChunk(instance, chunkX, chunkZ);
        var originRoomPos = new Pos2d(chunkX / 2, chunkZ / 2);
        var room = generator.getRooms()[chunkX / 2][chunkZ / 2];
        if (room == null) return null;
        if (room.parent() != null) room = generator.getFromPos(room.parent());
        var id = ids[room.pos().x()][room.pos().z()];
        if (id == null) {
            var typess = room.shape().getSchematics();
            id = room.shape().getSchematics().get()[generator.getRandom().nextInt(room.shape().getSchematics().get().length)];
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
            var path = "assets/shematics/dungeon/rooms/" + (room.type() == RoomType.Room ? room.shape().toString() : room.type().toString().toLowerCase()) + "/" + id;
            try (InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream(path)) {
                try (GZIPInputStream gzipOut = new GZIPInputStream(Objects.requireNonNull(resourceAsStream))) {
                    var obj = JsonParser.parseReader(new InputStreamReader(gzipOut)).getAsJsonObject();
                    var origin = room.rotation().ordinal() - Rotation.fromName(obj.get("originRotation").getAsString()).ordinal();
                    if (origin < 0) origin += 4;
                    var jsonPallete = obj.get("pallete").getAsJsonArray();
                    Block[] blocks = new Block[jsonPallete.size()];
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
                        var b = Block.fromKey(Key.key(object.get("id").getAsString())).withProperties(map);

                        if (object.has("texture")) {
                            var textures = CompoundBinaryTag.empty().putString("name", "textures").putString("value", object.get("texture").getAsString());
                            var properties = ListBinaryTag.from(List.of(textures));
                            var profile = CompoundBinaryTag.empty().putString("name", "CarsCupcake").put("properties", properties);
                            b = b.withNbt(CompoundBinaryTag.empty().put("profile", profile));
                        }
                        blocks[i] = b;
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
                    if (chunkX % 2 != 0 && chunkZ % 2 == 0) {
                        if (generator.getDoorsHorizontal().length > chunkX / 2 && generator.getDoorsHorizontal()[0].length > chunkZ / 2) {
                            var type = generator.getDoorsHorizontal()[chunkX / 2][chunkZ / 2];
                            var blockType = type == DoorType.Normal ? Block.STONE : type == DoorType.Wither ? Block.COAL_BLOCK : Block.AIR;
                            chunk.setBlock(15, 68, 15, blockType);
                            chunk.setBlock(15, 68, 14, blockType);
                            chunk.setBlock(15, 69, 13, blockType);
                            chunk.setBlock(15, 70, 13, blockType);
                            chunk.setBlock(15, 71, 13, blockType);
                            chunk.setBlock(15, 72, 13, blockType);
                            chunk.setBlock(15, 73, 15, blockType);
                            chunk.setBlock(15, 73, 14, blockType);
                            blockType = type == DoorType.Normal ? Block.AIR : type == DoorType.Wither ? Block.COAL_BLOCK : Block.STONE;
                            for (int x = 15; x >= 14; x--) {
                                for (int y = 69; y < 73; y++)
                                    for (int z = 15; z > 13; z--) {
                                        var block = chunk.getBlock(x, y, z);
                                        if (!block.isAir() && block.registry().equals(Block.IRON_BARS.registry()) && blockType == Block.STONE) break;
                                        chunk.setBlock(x, y, z, blockType);
                                }
                            }
                        }
                    }
                    if (chunkX % 2 != 0 && chunkZ % 2 != 0) {
                        if (generator.getDoorsHorizontal().length > chunkX / 2 && generator.getDoorsHorizontal()[0].length > chunkZ / 2) {
                            var type = generator.getDoorsHorizontal()[chunkX / 2][chunkZ / 2];
                            var blockType = type == DoorType.Normal ? Block.STONE : type == DoorType.Wither ? Block.COAL_BLOCK : Block.AIR;
                            chunk.setBlock(15, 68, 0, blockType);
                            chunk.setBlock(15, 69, 1, blockType);
                            chunk.setBlock(15, 70, 1, blockType);
                            chunk.setBlock(15, 71, 1, blockType);
                            chunk.setBlock(15, 72, 1, blockType);
                            chunk.setBlock(15, 73, 0, blockType);
                        }
                    }
                    if (chunkX % 2 == 0 && chunkZ % 2 != 0) {
                        if (generator.getDoorsVertical().length > chunkX / 2 && generator.getDoorsVertical()[0].length > chunkZ / 2) {
                            var type = generator.getDoorsVertical()[chunkX / 2][chunkZ / 2];
                            var blockType = type == DoorType.Normal ? Block.STONE : type == DoorType.Wither ? Block.COAL_BLOCK : Block.AIR;
                            chunk.setBlock(15, 68, 15, blockType);
                            chunk.setBlock(14, 68, 15, blockType);
                            chunk.setBlock(13, 69, 15, blockType);
                            chunk.setBlock(13, 70, 15, blockType);
                            chunk.setBlock(13, 71, 15, blockType);
                            chunk.setBlock(13, 72, 15, blockType);
                            chunk.setBlock(15, 73, 15, blockType);
                            chunk.setBlock(14, 73, 15, blockType);
                        }
                    }
                    if (chunkX % 2 != 0 && chunkZ % 2 != 0) {
                        if (generator.getDoorsVertical().length > chunkX / 2 && generator.getDoorsVertical()[0].length > chunkZ / 2) {
                            var type = generator.getDoorsVertical()[chunkX / 2][chunkZ / 2];
                            var blockType = type == DoorType.Normal ? Block.STONE : type == DoorType.Wither ? Block.COAL_BLOCK : Block.AIR;
                            chunk.setBlock(0, 68, 15, blockType);
                            chunk.setBlock(1, 69, 15, blockType);
                            chunk.setBlock(1, 70, 15, blockType);
                            chunk.setBlock(1, 71, 15, blockType);
                            chunk.setBlock(1, 72, 15, blockType);
                            chunk.setBlock(0, 73, 15, blockType);
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        chunks[chunkX][chunkZ] = chunk;
        return chunk;
    }

    @Override
    public void saveChunk(@NotNull Chunk chunk) {
        //NOOP
    }
}
