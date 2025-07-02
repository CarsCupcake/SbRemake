package me.carscupcake.sbremake.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.adventure.audience.Audiences;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class PlayerBrodcastOutputStream extends OutputStream {
    private StringBuilder builder = new StringBuilder();
    private final PrintStream wrapped;

    public PlayerBrodcastOutputStream(PrintStream wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void write(int b) throws IOException {
        wrapped.write(b);
        char c = (char) b;
        if (c == '\r') return;
        if (c == '\n') {
            Audiences.players().sendMessage(Component.text("§c" + (builder.toString())));
            builder = new StringBuilder();
        } else {
            if (c == '\t') {
                builder.append("    ");
            } else
                builder.append(c);
        }
    }
}
