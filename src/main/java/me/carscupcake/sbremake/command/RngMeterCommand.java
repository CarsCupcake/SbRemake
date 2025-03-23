package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;

public class RngMeterCommand extends Command {
    public RngMeterCommand() {
        super("rngmeter", "rng");
        ArgumentEnum<Slayers> slayersArgumentEnum = new ArgumentEnum<>("slayer", Slayers.class);
        addSyntax((sender, context) -> {
            ((SkyblockPlayer) sender).getSlayers().get(context.get(slayersArgumentEnum)).openRngMeterMenu();
        }, slayersArgumentEnum);
    }
}
