package me.carscupcake.sbremake.command.testing;

import com.google.gson.JsonParser;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.impl.PrivateIsle;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class ResetPrivateIsle extends Command {
    public ResetPrivateIsle() {
        super("resetprivateisle", "resetis");
        ArgumentString arg = new ArgumentString("target");
        addSyntax((commandSender, ignored) -> resetIsle(((SkyblockPlayer) commandSender).getUuid()));
        addSyntax((e, c) -> {
            HttpClient client = HttpClient.newHttpClient();
            try {
                var response = client.send(HttpRequest.newBuilder(URI.create("https://api.mojang.com/users/profiles/minecraft/" + (c.get(arg)))).GET().build(), HttpResponse.BodyHandlers.ofString());
                client.close();
                if (response.statusCode() == 204) {
                    e.sendMessage("No player with the name " + (c.get(arg)));
                    return;
                }
                if (response.statusCode() != 200) {
                    e.sendMessage("An unknown error occurred.");
                    Main.LOGGER.error(response.body());
                    return;
                }
                var uuid = UUID.fromString(JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString().replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                ));
                resetIsle(uuid);
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private static void resetIsle(UUID playerUUID) {
        var opt = SkyblockWorld.getWorlds(SkyblockWorld.PrivateIsle).stream().filter(worldProvider -> worldProvider != null && ((PrivateIsle) worldProvider).getOwner().equals(playerUUID)).findFirst();
        opt.ifPresent(SkyblockWorld.WorldProvider::remove);
        File dir = new File(ConfigFile.DATA_PATH, "/" + (playerUUID.toString()) + "/private_isle");
        if (dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
