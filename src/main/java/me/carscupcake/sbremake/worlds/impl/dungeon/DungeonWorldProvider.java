package me.carscupcake.sbremake.worlds.impl.dungeon;

import com.google.gson.JsonParser;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.util.PalletItem;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.minestom.server.coordinate.Pos;
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

public record DungeonWorldProvider(Generator generator, String[][] ids) implements IChunkLoader {

    @Override
    public @Nullable Chunk loadChunk(@NotNull Instance instance, int chunkX, int chunkZ) {
        if (chunkX > generator.getRooms().length * 2 || chunkZ > generator.getRooms()[0].length * 2) return null;
        var chunk = instance.getChunkSupplier().createChunk(instance, chunkX, chunkZ);
        var room = generator.getRooms()[chunkX / 2][chunkZ / 2];
        if (room.parent() != null) room = generator.getFromPos(room.parent());
        var id = ids[room.pos().x()][room.pos().z()];
        if (id == null) {
            id = switch (room.shape()) {
                case ONE_BY_ONE -> "overgrown-3";
                case ONE_BY_TWO -> "pedestal-5";
                case ONE_BY_THREE -> "wizard-4";
                case ONE_BY_FOUR -> "mossy-4";
                case TWO_BY_TWO -> "mithril-cave-10";
                case L_SHAPE -> "dino-dig-site-4";
            };
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
            var path = "assets/shematics/dungeon/rooms/" + (room.type() == RoomType.Room ?  room.shape().toString() : room.type().toString().toLowerCase()) + "/" + id;
            try (InputStream resourceAsStream = Main.class.getClassLoader().getResourceAsStream(path)) {
                try (GZIPInputStream gzipOut = new GZIPInputStream(Objects.requireNonNull(resourceAsStream))) {
                    var obj = JsonParser.parseReader(new InputStreamReader(gzipOut)).getAsJsonObject();
                    var origin = room.rotation().ordinal() - Rotation.fromName(obj.get("originRotation").getAsString()).ordinal();
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
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 140; y++) {
                            for (int z = 0; z < 16; z++) {
                                var pos = new Pos(x + (chunkX * 16), y, z * (chunkZ * 16));
                                var offset = room.shape().withRotationOffset(Pos.ZERO, room.rotation());
                                var undoRotation = room.rotation().toRelative(room.pos(), pos.sub(offset));
                                if (xArr.size() >= undoRotation.blockX()) continue;
                                var yArr = xArr.get(x).getAsJsonArray();
                                if (yArr.size() >= undoRotation.blockY()) continue;
                                var zArr = yArr.get(y).getAsJsonArray();
                                if (zArr.size() >= undoRotation.blockZ()) continue;
                                var item = blocks[zArr.get(undoRotation.blockZ()).getAsInt()];
                                var block = item.block().withProperties(item.propertyMap());
                                if (item.headValue() != null) {
                                    var textures = CompoundBinaryTag.empty().putString("name", "textures").putString("value", item.headValue());
                                    var properties = ListBinaryTag.from(List.of(textures));
                                    var profile = CompoundBinaryTag.empty().putString("name", "CarsCupcake").put("properties", properties);
                                    block = block.withNbt(CompoundBinaryTag.empty().put("profile", profile));
                                }
                                chunk.setBlock(x, y, z, block);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Load " + chunkX + " " + chunkZ);
        return chunk;
    }

    @Override
    public void saveChunk(@NotNull Chunk chunk) {
        //NOOP
    }
}
