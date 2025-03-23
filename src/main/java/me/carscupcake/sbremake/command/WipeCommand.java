package me.carscupcake.sbremake.command;

import com.google.gson.JsonParser;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.entity.Player;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

public class WipeCommand extends Command {
    public WipeCommand() {
        super("wipe");
        ArgumentString arg = new ArgumentString("target");
        addSyntax((e, c) -> Thread.ofVirtual().start(() -> {
            HttpClient client = HttpClient.newHttpClient();
            try {
                var response = client.send(HttpRequest.newBuilder(URI.create("https://api.mojang.com/users/profiles/minecraft/" + (c.get(arg)) )).GET().build(), HttpResponse.BodyHandlers.ofString());
                client.close();
                if (response.statusCode() == 204) {
                    e.sendMessage("No player with the name " + (c.get(arg)) );
                    return;
                }
                if (response.statusCode() != 200) {
                    e.sendMessage("An unknown error occurred.");
                    return;
                }
                var uuid = UUID.fromString(JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsString().replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                ));
                Audiences.players().filterAudience((p) -> ((Player) p).getUuid().equals(uuid)).forEachAudience(audience -> {
                    SkyblockPlayer player = (SkyblockPlayer) audience;
                    player.kick("§cYou got wiped!", true);
                });
                removeRecursivley(ConfigFile.DATA_PATH.toPath().resolve(uuid.toString()));
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }), arg);
    }

    public void removeRecursivley(Path path) throws IOException {
        if (Files.isDirectory(path))
        {
            for (File f : Objects.requireNonNull(path.toFile().listFiles()))
                removeRecursivley(f.toPath());
            Files.deleteIfExists(path);
        } else {
            Files.deleteIfExists(path);
        }
    }
}
