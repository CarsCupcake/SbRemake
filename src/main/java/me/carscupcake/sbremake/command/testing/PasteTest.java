package me.carscupcake.sbremake.command.testing;

import com.google.gson.JsonParser;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.PalletItem;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.ArrayBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.minestom.server.command.builder.Command;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.block.Block;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

@DebugCommand
public class PasteTest extends Command {
    public PasteTest() {
        super("pastetest");
        setDefaultExecutor((sender, _) -> {
            try {
                var instance = ((SkyblockPlayer) sender).getInstance();
                try (GZIPInputStream gzipOut = new GZIPInputStream(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("assets/shematics/dungeon/rooms/L-shape/dino-dig-site-4")))) {
                    var obj = JsonParser.parseReader(new InputStreamReader(gzipOut)).getAsJsonObject();
                    var jsonPallete = obj.get("pallete").getAsJsonArray();
                    PalletItem[] blocks = new PalletItem[jsonPallete.size()];
                    for (int i = 0; i < blocks.length; i++) {
                        var object = jsonPallete.get(i).getAsJsonObject();
                        var map = new HashMap<String, String>();
                        for (var e : object.get("properties").getAsJsonArray()) {
                            var prop = e.getAsJsonObject();
                            map.put(prop.get("name").getAsString(), prop.get("value").getAsString().toLowerCase());
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
                                instance.setBlock(x, y, z, block, false);

                            }
                        }
                    }
                    var entities = obj.get("entities").getAsJsonArray();
                    System.out.println("entities: " + entities.size());
                    for (var entity : entities) {
                        var o = entity.getAsJsonObject();
                        var test = new LivingEntity(EntityType.ARMOR_STAND);
                        test.set(DataComponents.CUSTOM_NAME, Component.text(o.get("type").getAsString() + " " + o.get("starred").getAsBoolean()));
                        test.setCustomNameVisible(true);
                        test.setNoGravity(true);
                        var p = o.get("pos").getAsJsonObject();
                        test.setInstance(instance, new Pos(p.get("x").getAsInt(), p.get("y").getAsInt(), p.get("z").getAsInt()));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
}
