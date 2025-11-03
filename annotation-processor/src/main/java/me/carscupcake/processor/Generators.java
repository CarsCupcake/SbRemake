package me.carscupcake.processor;

import java.nio.file.Path;

public class Generators {
    static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: <target folder>");
            return;
        }
        Path outputFolder = Path.of(args[0]);
        new ConfigConstantGenerator().process(outputFolder);
    }
}
