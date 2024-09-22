package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;

public class SlayerCommand extends Command {
    public SlayerCommand() {
        super("slayer");
        ArgumentEnum<Slayers> slayer = new ArgumentEnum<>("slayer", Slayers.class);
        ArgumentNumber<Integer> level = new ArgumentInteger("level");
        addSyntax((sender, context) -> {
            ISlayer s = context.get(slayer);
            s.getEntity(context.get(level), (SkyblockPlayer) sender).setInstance(((SkyblockPlayer) sender).getInstance(), ((SkyblockPlayer) sender).getPosition());
        }, slayer, level);
    }
}
