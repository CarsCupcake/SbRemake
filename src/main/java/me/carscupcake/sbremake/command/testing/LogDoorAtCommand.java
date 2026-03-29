package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.impl.Dungeon;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;

public class LogDoorAtCommand extends Command {
    public LogDoorAtCommand() {
        super("doorat");
        var x = new ArgumentInteger("x");
        var z = new ArgumentInteger("z");
        var isHorizontal = new ArgumentBoolean("isHorizontal");
        addSyntax((sender, context) -> {
            var current = ((SkyblockPlayer) sender).getWorldProvider();
            if (!(current instanceof Dungeon d)) {
                sender.sendMessage("§cYou need to be in a dungeon!");
                return;
            }
            var gen = d.getGenerator();
            var door = context.get(isHorizontal) ? gen.getDoorsHorizontal()[context.get(x)][context.get(z)] : gen.getDoorsVertical()[context.get(x)][context.get(z)];
            sender.sendMessage("§aThe door type is " + door);
        }, x, z, isHorizontal);
    }
}
