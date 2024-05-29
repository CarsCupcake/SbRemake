package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.Main;
import net.minestom.server.event.player.PlayerTickEvent;

import java.util.function.Consumer;

public class Tick implements Consumer<PlayerTickEvent> {
    @Override
    public void accept(PlayerTickEvent playerTickEvent) {
        System.out.println("Tick");
    }
}
