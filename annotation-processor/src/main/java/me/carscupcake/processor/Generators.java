package me.carscupcake.processor;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

public class Generators {
    static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: <target folder>");
            return;
        }
        Path outputFolder = Path.of(args[0]);
        new ConfigConstantGenerator().process(resource("constants.json"), outputFolder);
        new ShardGenerator().process(resource("shards.json"), outputFolder);
    }

    private static InputStream resource(String name) {
        return Objects.requireNonNull(Generators.class.getResourceAsStream("/" + name), "Cannot find resource: %s".formatted(name));
    }
}
