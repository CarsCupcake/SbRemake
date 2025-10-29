package me.carscupcake.sbremake.util.gui;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.NumberUtil;
import net.kyori.adventure.nbt.*;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.BlockEntityDataPacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public record InputGui(List<String> prompt) {
    public static final InputFormater<String> STRING_FORMAT = List::getFirst;
    public static final InputFormater<Integer> SIMPLE_INTEGER_FORMAT = (input) -> Integer.parseInt(input.getFirst());
    public static final InputFormater<Long> SHORTENED_LONG_FORMAT = (input -> NumberUtil.fromShortenedString(input.getFirst()));
    public static final InputFormater<Double> SIMPLE_DOUBLE_FORMAT = (input) -> Double.parseDouble(input.getFirst());
    public static final Map<SkyblockPlayer, Consumer<List<String>>> inputGuis = new HashMap<>();
    public static final EventNode<@NotNull Event> LISTENER = EventNode.all("signgui")
            .addListener(PlayerPacketEvent.class, event -> {
                if (event.getPacket() instanceof ClientUpdateSignPacket(var pos, _, List<String> lines) && inputGuis.containsKey((SkyblockPlayer) event.getPlayer())) {
                    event.getPlayer().sendPacket(new BlockChangePacket(pos, event.getPlayer().getInstance().getBlock(pos)));
                    inputGuis.get((SkyblockPlayer) event.getPlayer()).accept(lines);
                    inputGuis.remove((SkyblockPlayer) event.getPlayer());
                    event.setCancelled(true);
                }
            });
    public InputGui {
        if (prompt.size() != 4) throw new IllegalStateException("Length has to be 4");
        prompt = List.copyOf(prompt);
    }

    public InputGui(String... strings) {
        this(List.of(strings));
    }


    /**
     * Prompts the player with the input
     * @param player the player to prompt
     * @param format the format to parse the input with
     * @param callback the callback to run when the input is parsed passes null if the player sends an invalid input
     * @param <T> the type of the parsed input
     */
    public <T> void show(SkyblockPlayer player, InputFormater<T> format, Consumer<@Nullable T> callback) {
        show(player, lines -> {callback.accept(format.format(lines));});
    }

    public void show(SkyblockPlayer player, Consumer<List<String>> callback) {
        if (inputGuis.containsKey(player)) {
            throw new IllegalStateException("Player already has an input gui!");
        }
        inputGuis.put(player, callback);
        var pos = player.getPosition().add(0, 3, 0);
        var nbt = CompoundBinaryTag.builder()
                .putBoolean("has_glowing_text", false)
                .putString("color", "black")
                .put("messages", ListBinaryTag.from(prompt.stream().map(s -> {
                    var c = CompoundBinaryTag.builder()
                            .putBoolean("italic", false)
                            .putString("text", "");
                    if (!s.isBlank())
                        c.put("extra", ListBinaryTag.from(List.of(CompoundBinaryTag.builder()
                                .putString("text", s)
                                .build())));
                    return c.build();
                }).toList()))
                .build();
        var full = CompoundBinaryTag.builder()
                .put("front_text", nbt)
                .put("back_text", CompoundBinaryTag.builder()
                        .putString("color", "black")
                        .putBoolean("has_glowing_text", false)
                        .put("messages", ListBinaryTag.from(List.of(StringBinaryTag.stringBinaryTag(""), StringBinaryTag.stringBinaryTag(""), StringBinaryTag.stringBinaryTag(""), StringBinaryTag.stringBinaryTag(""))))
                        .build())
                .putString("id", "Sign")
                .putInt("x", pos.blockX())
                .putInt("y", pos.blockY())
                .putInt("z", pos.blockZ())
                .build();
        player.sendPacket(new BlockChangePacket(pos, Block.OAK_SIGN.withNbt(full)));
        player.sendPacket(new BlockEntityDataPacket(pos, 7, full));
        player.sendPacket(new OpenSignEditorPacket(pos, true));
    }
    public interface InputFormater<T> {
        T format(List<String> input) throws IllegalArgumentException;
    }
}
